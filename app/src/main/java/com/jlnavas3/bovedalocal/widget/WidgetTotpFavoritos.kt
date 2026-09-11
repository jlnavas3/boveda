package com.jlnavas3.bovedalocal.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.MainActivity

class WidgetTotpFavoritos : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (id in appWidgetIds) {
            actualizarWidget(context, appWidgetManager, id)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_COPIAR_TOTP -> {
                val codigo = intent.getStringExtra(EXTRA_CODIGO) ?: return
                val titulo = intent.getStringExtra(EXTRA_TITULO) ?: "2FA"
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                if (cm != null) {
                    val clip = android.content.ClipData.newPlainText("Código TOTP", codigo)
                    cm.setPrimaryClip(clip)
                }
                ejecutarVibracion(context)
                Toast.makeText(context, "$titulo: $codigo copiado", Toast.LENGTH_SHORT).show()
            }
            ACTION_ACTUALIZAR -> {
                actualizarTodos(context)
            }
        }
    }

    companion object {
        const val ACTION_COPIAR_TOTP = "com.jlnavas3.bovedalocal.widget.COPIAR_TOTP"
        const val ACTION_ACTUALIZAR = "com.jlnavas3.bovedalocal.widget.ACTUALIZAR"
        const val EXTRA_CODIGO = "extra_codigo"
        const val EXTRA_TITULO = "extra_titulo"

        fun actualizarTodos(context: Context) {
            try {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, WidgetTotpFavoritos::class.java))
                for (id in ids) {
                    actualizarWidget(context, appWidgetManager, id)
                }
            } catch (_: Exception) {
            }
        }

        private fun actualizarWidget(context: Context, manager: AppWidgetManager, widgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_totp)
            val repo = VaultRepository.obtener(context)
            val estado = repo.estado.value

            // Intent para abrir MainActivity al pulsar sobre el widget
            val intentAbrir = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingAbrir = PendingIntent.getActivity(
                context, 0, intentAbrir,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Intent para botón de refresco
            val intentRefresco = Intent(context, WidgetTotpFavoritos::class.java).apply {
                action = ACTION_ACTUALIZAR
            }
            val pendingRefresco = PendingIntent.getBroadcast(
                context, 999, intentRefresco,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_btn_recargar, pendingRefresco)

            if (estado !is EstadoBoveda.Desbloqueada) {
                // Estado bloqueado
                views.setViewVisibility(R.id.widget_vista_bloqueada, View.VISIBLE)
                views.setViewVisibility(R.id.widget_vista_vacia, View.GONE)
                views.setViewVisibility(R.id.widget_vista_desbloqueada, View.GONE)
                views.setOnClickPendingIntent(R.id.widget_vista_bloqueada, pendingAbrir)
            } else {
                // Estado desbloqueado
                val entradasConTotp = estado.entradas.filter {
                    it.favorito && !it.secretoTotp.isNullOrBlank()
                }

                if (entradasConTotp.isEmpty()) {
                    views.setViewVisibility(R.id.widget_vista_bloqueada, View.GONE)
                    views.setViewVisibility(R.id.widget_vista_vacia, View.VISIBLE)
                    views.setViewVisibility(R.id.widget_vista_desbloqueada, View.GONE)
                    views.setOnClickPendingIntent(R.id.widget_vista_vacia, pendingAbrir)
                } else {
                    views.setViewVisibility(R.id.widget_vista_bloqueada, View.GONE)
                    views.setViewVisibility(R.id.widget_vista_vacia, View.GONE)
                    views.setViewVisibility(R.id.widget_vista_desbloqueada, View.VISIBLE)

                    val ahora = System.currentTimeMillis() / 1000
                    val filas = listOf(
                        Triple(R.id.widget_item_1, R.id.widget_item_titulo_1, Pair(R.id.widget_item_codigo_1, R.id.widget_item_tiempo_1)),
                        Triple(R.id.widget_item_2, R.id.widget_item_titulo_2, Pair(R.id.widget_item_codigo_2, R.id.widget_item_tiempo_2)),
                        Triple(R.id.widget_item_3, R.id.widget_item_titulo_3, Pair(R.id.widget_item_codigo_3, R.id.widget_item_tiempo_3))
                    )

                    filas.forEachIndexed { i, (layoutId, tituloId, codTiempo) ->
                        if (i < entradasConTotp.size) {
                            val entrada = entradasConTotp[i]
                            val secreto = entrada.secretoTotp!!
                            val periodo = entrada.totpPeriodo.toLong().coerceAtLeast(10L)
                            val segundosRestantes = Totp.segundosRestantes(ahora, periodo)
                            val codigo = try {
                                Totp.codigo(
                                    secreto = Base32.decodificar(secreto),
                                    segundosUnix = ahora,
                                    digitos = entrada.totpDigitos,
                                    periodo = periodo,
                                    algoritmo = entrada.totpAlgoritmo
                                )
                            } catch (_: Exception) {
                                "------"
                            }

                            val codigoFormateado = if (codigo.length == 6) "${codigo.take(3)} ${codigo.drop(3)}" else codigo

                            views.setViewVisibility(layoutId, View.VISIBLE)
                            views.setTextViewText(tituloId, entrada.titulo.ifBlank { "Cuenta" })
                            views.setTextViewText(codTiempo.first, codigoFormateado)
                            views.setTextViewText(codTiempo.second, "${segundosRestantes}s")

                            // PendingIntent para copiar al tocar la fila
                            val intentCopiar = Intent(context, WidgetTotpFavoritos::class.java).apply {
                                action = ACTION_COPIAR_TOTP
                                putExtra(EXTRA_CODIGO, codigo)
                                putExtra(EXTRA_TITULO, entrada.titulo)
                            }
                            val pendingCopiar = PendingIntent.getBroadcast(
                                context, i + 100, intentCopiar,
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                            )
                            views.setOnClickPendingIntent(layoutId, pendingCopiar)
                        } else {
                            views.setViewVisibility(layoutId, View.GONE)
                        }
                    }
                }
            }

            manager.updateAppWidget(widgetId, views)
        }

        private fun ejecutarVibracion(context: Context) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    vm?.defaultVibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                } else {
                    @Suppress("DEPRECATION")
                    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                    @Suppress("DEPRECATION")
                    v?.vibrate(30)
                }
            } catch (_: Exception) {
            }
        }
    }
}
