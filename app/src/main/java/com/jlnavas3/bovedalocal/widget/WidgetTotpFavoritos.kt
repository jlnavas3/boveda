package com.jlnavas3.bovedalocal.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.MainActivity
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class WidgetTotpFavoritos : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (id in appWidgetIds) {
            actualizarWidget(context, appWidgetManager, id)
        }
        iniciarTickerSiCorresponde(context)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        // Invalidar caché de fondo para que se adapte al nuevo tamaño
        fondoCacheMap.remove(appWidgetId)
        actualizarWidget(context, appWidgetManager, appWidgetId)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        iniciarTickerSiCorresponde(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        detenerTicker()
        removerScreenReceiver(context)
        fondoCacheMap.clear()
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_COPIAR_TOTP -> {
                val codigo = intent.getStringExtra(EXTRA_CODIGO) ?: return
                val titulo = intent.getStringExtra(EXTRA_TITULO) ?: "2FA"
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                if (cm != null) {
                    val clip = ClipData.newPlainText("Código TOTP", codigo)
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

        private var jobTicker: Job? = null
        private var screenReceiverRegistrado = false
        private var pantallaEncendida = true

        // Estructura para caché del bitmap de fondo
        private data class FondoCacheKey(
            val widthPx: Int,
            val heightPx: Int,
            val grosorDp: Float,
            val curvaturaDp: Float,
            val opacidad: Float,
            val colorBorde: String,
            val vidrioEsmerilado: Boolean,
            val esmeriladoIntensidad: Float,
            val esmeriladoLuz: Float
        )
        private val fondoCacheMap = mutableMapOf<Int, Pair<FondoCacheKey, Bitmap>>()

        private val screenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_SCREEN_ON -> {
                        pantallaEncendida = true
                        iniciarTickerSiCorresponde(context)
                    }
                    Intent.ACTION_SCREEN_OFF -> {
                        pantallaEncendida = false
                        detenerTicker()
                    }
                }
            }
        }

        private fun asegurarScreenReceiver(context: Context) {
            if (!screenReceiverRegistrado) {
                try {
                    val filter = IntentFilter().apply {
                        addAction(Intent.ACTION_SCREEN_ON)
                        addAction(Intent.ACTION_SCREEN_OFF)
                    }
                    context.applicationContext.registerReceiver(screenReceiver, filter)
                    screenReceiverRegistrado = true
                } catch (_: Exception) {}
            }
        }

        private fun removerScreenReceiver(context: Context) {
            if (screenReceiverRegistrado) {
                try {
                    context.applicationContext.unregisterReceiver(screenReceiver)
                } catch (_: Exception) {}
                screenReceiverRegistrado = false
            }
        }

        @Synchronized
        fun iniciarTickerSiCorresponde(context: Context) {
            val app = context.applicationContext
            val repo = try { VaultRepository.obtener(app) } catch (_: Exception) { return }
            if (!repo.estaDesbloqueada || !pantallaEncendida) {
                detenerTicker()
                return
            }
            val appWidgetManager = AppWidgetManager.getInstance(app)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(app, WidgetTotpFavoritos::class.java))
            if (ids == null || ids.isEmpty()) {
                detenerTicker()
                return
            }

            asegurarScreenReceiver(app)

            if (jobTicker?.isActive == true) return

            jobTicker = CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
                while (isActive && repo.estaDesbloqueada && pantallaEncendida) {
                    actualizarTodos(app)
                    delay(1000L)
                }
                detenerTicker()
            }
        }

        @Synchronized
        fun detenerTicker() {
            jobTicker?.cancel()
            jobTicker = null
        }

        fun actualizarTodos(context: Context) {
            try {
                val app = context.applicationContext
                val appWidgetManager = AppWidgetManager.getInstance(app)
                val ids = appWidgetManager.getAppWidgetIds(ComponentName(app, WidgetTotpFavoritos::class.java))
                if (ids == null || ids.isEmpty()) {
                    detenerTicker()
                    return
                }
                for (id in ids) {
                    actualizarWidget(app, appWidgetManager, id)
                }

                val repo = VaultRepository.obtener(app)
                if (repo.estaDesbloqueada && pantallaEncendida) {
                    if (jobTicker?.isActive != true) {
                        iniciarTickerSiCorresponde(app)
                    }
                } else {
                    detenerTicker()
                }
            } catch (_: Exception) {
            }
        }

        private fun actualizarWidget(context: Context, manager: AppWidgetManager, widgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_totp)
            val repo = VaultRepository.obtener(context)
            val ajustes = repo.ajustes.actual
            val estado = repo.estado.value

            // 1. Renderizar fondo personalizable según sliders
            val options = manager.getAppWidgetOptions(widgetId)
            val isLandscape = context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            val widthDp = if (isLandscape) {
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, 260)
            } else {
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 260)
            }.let { if (it <= 0) 260 else it }

            val heightDp = if (isLandscape) {
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 140)
            } else {
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, 140)
            }.let { if (it <= 0) 140 else it }

            val density = context.resources.displayMetrics.density
            val widthPx = (widthDp * density).roundToInt()
            val heightPx = (heightDp * density).roundToInt()

            val fondoKey = FondoCacheKey(
                widthPx = widthPx,
                heightPx = heightPx,
                grosorDp = ajustes.widgetGrosorBordeDp,
                curvaturaDp = ajustes.widgetCurvaturaEsquinasDp,
                opacidad = ajustes.widgetTransparenciaFondo,
                colorBorde = ajustes.widgetColorBorde,
                vidrioEsmerilado = ajustes.widgetTotpVidrioEsmerilado,
                esmeriladoIntensidad = ajustes.widgetTotpEsmeriladoIntensidad,
                esmeriladoLuz = ajustes.widgetTotpEsmeriladoLuz
            )

            val cachedFondo = fondoCacheMap[widgetId]
            val fondoBitmap = if (cachedFondo != null && cachedFondo.first == fondoKey && !cachedFondo.second.isRecycled) {
                cachedFondo.second
            } else {
                val nuevo = generarFondoBitmap(
                    anchoPx = widthPx,
                    altoPx = heightPx,
                    grosorDp = ajustes.widgetGrosorBordeDp,
                    curvaturaDp = ajustes.widgetCurvaturaEsquinasDp,
                    opacidadFondo = ajustes.widgetTransparenciaFondo,
                    colorBordeHex = ajustes.widgetColorBorde,
                    density = density,
                    vidrioEsmerilado = ajustes.widgetTotpVidrioEsmerilado,
                    esmeriladoIntensidad = ajustes.widgetTotpEsmeriladoIntensidad,
                    esmeriladoLuz = ajustes.widgetTotpEsmeriladoLuz
                )
                fondoCacheMap[widgetId] = Pair(fondoKey, nuevo)
                nuevo
            }
            views.setImageViewBitmap(R.id.widget_fondo, fondoBitmap)

            // 2. Renderizar fondos de las filas / tarjeta bloqueada
            val opacidadFilas = ajustes.widgetTransparenciaFilas
            val filaAnchoPx = (widthPx - (24 * density).roundToInt()).coerceAtLeast(100)
            val filaAltoPx = (38 * density).roundToInt().coerceAtLeast(40)

            if (opacidadFilas <= 0.001f) {
                views.setViewVisibility(R.id.widget_item_fondo_1, View.GONE)
                views.setViewVisibility(R.id.widget_item_fondo_2, View.GONE)
                views.setViewVisibility(R.id.widget_item_fondo_3, View.GONE)
                views.setViewVisibility(R.id.widget_tarjeta_bloqueada_fondo, View.GONE)
            } else {
                val filaBitmap = generarFondoFilaBitmap(
                    anchoPx = filaAnchoPx,
                    altoPx = filaAltoPx,
                    curvaturaDp = 6f,
                    opacidadFila = opacidadFilas,
                    colorFilaHex = ajustes.widgetColorFilas,
                    density = density
                )
                views.setViewVisibility(R.id.widget_item_fondo_1, View.VISIBLE)
                views.setViewVisibility(R.id.widget_item_fondo_2, View.VISIBLE)
                views.setViewVisibility(R.id.widget_item_fondo_3, View.VISIBLE)
                views.setViewVisibility(R.id.widget_tarjeta_bloqueada_fondo, View.VISIBLE)

                views.setImageViewBitmap(R.id.widget_item_fondo_1, filaBitmap)
                views.setImageViewBitmap(R.id.widget_item_fondo_2, filaBitmap)
                views.setImageViewBitmap(R.id.widget_item_fondo_3, filaBitmap)
                views.setImageViewBitmap(R.id.widget_tarjeta_bloqueada_fondo, filaBitmap)
            }

            // Colores de cabecera: Título e Ícono
            val colorTituloIconoInt = try {
                Color.parseColor(ajustes.widgetColorTituloIcono.ifBlank { AjustesDefaults.WidgetTotp.COLOR_TITULO_ICONO })
            } catch (_: Exception) {
                Color.parseColor(AjustesDefaults.WidgetTotp.COLOR_TITULO_ICONO)
            }
            views.setTextColor(R.id.widget_titulo, colorTituloIconoInt)
            views.setInt(R.id.widget_icono_cabecera, "setColorFilter", colorTituloIconoInt)

            // Intent para abrir MainActivity al pulsar sobre el widget
            val intentAbrir = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingAbrir = PendingIntent.getActivity(
                context, 0, intentAbrir,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_raiz, pendingAbrir)

            if (estado !is EstadoBoveda.Desbloqueada) {
                // Estado bloqueado: candado grande de 2 líneas y tarjeta centrada
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

                    val colorCodigoInt = try {
                        Color.parseColor(ajustes.widgetColorCodigo.ifBlank { AjustesDefaults.WidgetTotp.COLOR_CODIGO })
                    } catch (_: Exception) {
                        Color.parseColor(AjustesDefaults.WidgetTotp.COLOR_CODIGO)
                    }

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

                            val codigoFormateado = if (ajustes.totpSepararDigitos && codigo.length == 6) {
                                "${codigo.take(3)} ${codigo.drop(3)}"
                            } else {
                                codigo
                            }

                            views.setViewVisibility(layoutId, View.VISIBLE)
                            views.setTextViewText(tituloId, entrada.titulo.ifBlank { "Cuenta" })
                            views.setTextViewText(codTiempo.first, codigoFormateado)
                            views.setTextColor(codTiempo.first, colorCodigoInt)

                            // Indicador circular tipo tarta estilo Google Authenticator
                            val tartaBitmap = generarBitmapTartaTotp(
                                segundosRestantes = segundosRestantes,
                                periodo = periodo,
                                density = density,
                                colorContadorHex = ajustes.widgetColorContador
                            )
                            views.setImageViewBitmap(codTiempo.second, tartaBitmap)

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

        private fun generarFondoBitmap(
            anchoPx: Int,
            altoPx: Int,
            grosorDp: Float,
            curvaturaDp: Float,
            opacidadFondo: Float,
            colorBordeHex: String,
            density: Float,
            vidrioEsmerilado: Boolean = false,
            esmeriladoIntensidad: Float = 0.60f,
            esmeriladoLuz: Float = 0.40f
        ): Bitmap {
            val w = anchoPx.coerceIn(40, 1600)
            val h = altoPx.coerceIn(40, 1600)
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val radioPx = curvaturaDp * density
            val grosorPx = grosorDp * density
            val rectFondo = RectF(0f, 0f, w.toFloat(), h.toFloat())

            // Fondo con opacidad configurable
            val alphaInt = (opacidadFondo.coerceIn(0f, 1f) * 255).roundToInt()
            if (alphaInt > 0) {
                val paintFondo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    if (vidrioEsmerilado) {
                        val baseR = 0x1C
                        val baseG = 0x1C
                        val baseB = 0x1E
                        val luz = esmeriladoLuz.coerceIn(0f, 1f)
                        val intensidad = esmeriladoIntensidad.coerceIn(0.1f, 1f)
                        val alphaTop = ((alphaInt * 0.45f) + (65 * luz * intensidad)).roundToInt().coerceIn(0, 255)
                        val rTop = (baseR + ((255 - baseR) * 0.35f * luz)).roundToInt().coerceIn(0, 255)
                        val gTop = (baseG + ((255 - baseG) * 0.35f * luz)).roundToInt().coerceIn(0, 255)
                        val bTop = (baseB + ((255 - baseB) * 0.35f * luz)).roundToInt().coerceIn(0, 255)
                        val colorTop = Color.argb(alphaTop, rTop, gTop, bTop)
                        val colorMid = Color.argb(alphaInt, baseR, baseG, baseB)
                        val colorBot = Color.argb(
                            alphaInt,
                            (baseR * 0.88f).roundToInt(),
                            (baseG * 0.88f).roundToInt(),
                            (baseB * 0.88f).roundToInt()
                        )
                        shader = LinearGradient(
                            0f, 0f, 0f, h.toFloat(),
                            intArrayOf(colorTop, colorMid, colorBot),
                            floatArrayOf(0f, 0.40f, 1f),
                            Shader.TileMode.CLAMP
                        )
                    } else {
                        color = Color.argb(alphaInt, 0x1A, 0x18, 0x15)
                    }
                    style = Paint.Style.FILL
                }
                canvas.drawRoundRect(rectFondo, radioPx, radioPx, paintFondo)

                // Reflejo cenital en la parte superior si está activo el vidrio
                if (vidrioEsmerilado && esmeriladoLuz > 0.05f) {
                    val paintSheen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        val alphaSheen = (70 * esmeriladoLuz * esmeriladoIntensidad).roundToInt().coerceIn(0, 255)
                        shader = LinearGradient(
                            0f, 0f, 0f, h.toFloat() * 0.35f,
                            Color.argb(alphaSheen, 255, 255, 255),
                            Color.TRANSPARENT,
                            Shader.TileMode.CLAMP
                        )
                        style = Paint.Style.FILL
                    }
                    canvas.drawRoundRect(rectFondo, radioPx, radioPx, paintSheen)
                }
            }

            // Borde perimetral si grosor > 0
            if (grosorPx > 0.2f) {
                val colorBordeBase = try {
                    Color.parseColor(colorBordeHex.ifBlank { AjustesDefaults.WidgetTotp.COLOR_BORDE })
                } catch (_: Exception) {
                    Color.parseColor(AjustesDefaults.WidgetTotp.COLOR_BORDE)
                }
                val alphaBorde = (alphaInt.coerceAtLeast(140)).coerceAtMost(255)
                val paintBorde = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    if (vidrioEsmerilado) {
                        val alphaBordeTop = ((alphaBorde * 0.6f) + (85 * esmeriladoLuz)).roundToInt().coerceIn(0, 255)
                        val colorBordeTop = Color.argb(alphaBordeTop, 255, 255, 255)
                        val colorBordeBot = Color.argb(
                            alphaBorde,
                            Color.red(colorBordeBase),
                            Color.green(colorBordeBase),
                            Color.blue(colorBordeBase)
                        )
                        shader = LinearGradient(
                            0f, 0f, 0f, h.toFloat(),
                            colorBordeTop, colorBordeBot,
                            Shader.TileMode.CLAMP
                        )
                    } else {
                        color = Color.argb(
                            alphaBorde,
                            Color.red(colorBordeBase),
                            Color.green(colorBordeBase),
                            Color.blue(colorBordeBase)
                        )
                    }
                    style = Paint.Style.STROKE
                    strokeWidth = grosorPx
                }
                val medioGrosor = grosorPx / 2f
                val rectBorde = RectF(
                    medioGrosor,
                    medioGrosor,
                    w.toFloat() - medioGrosor,
                    h.toFloat() - medioGrosor
                )
                val radioBorde = (radioPx - medioGrosor).coerceAtLeast(0f)
                canvas.drawRoundRect(rectBorde, radioBorde, radioBorde, paintBorde)
            }

            return bitmap
        }

        private fun generarFondoFilaBitmap(
            anchoPx: Int,
            altoPx: Int,
            curvaturaDp: Float,
            opacidadFila: Float,
            colorFilaHex: String,
            density: Float
        ): Bitmap {
            val w = anchoPx.coerceIn(20, 1600)
            val h = altoPx.coerceIn(20, 800)
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val radioPx = curvaturaDp * density
            val colorBase = try {
                if (colorFilaHex.isBlank() || colorFilaHex == AjustesDefaults.WidgetTotp.COLOR_FILAS) {
                    Color.parseColor(AjustesDefaults.WidgetTotp.COLOR_FILAS_DEFECTO)
                } else {
                    Color.parseColor(colorFilaHex)
                }
            } catch (_: Exception) {
                Color.parseColor(AjustesDefaults.WidgetTotp.COLOR_FILAS_DEFECTO)
            }

            val alphaInt = (opacidadFila.coerceIn(0f, 1f) * 255).roundToInt()
            if (alphaInt > 0) {
                val paintFondo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.argb(
                        alphaInt,
                        Color.red(colorBase),
                        Color.green(colorBase),
                        Color.blue(colorBase)
                    )
                    style = Paint.Style.FILL
                }
                val rectFondo = RectF(0f, 0f, w.toFloat(), h.toFloat())
                canvas.drawRoundRect(rectFondo, radioPx, radioPx, paintFondo)
            }

            return bitmap
        }

        private fun generarBitmapTartaTotp(
            segundosRestantes: Long,
            periodo: Long,
            density: Float,
            colorContadorHex: String = AjustesDefaults.WidgetTotp.COLOR_CONTADOR
        ): Bitmap {
            val tamanoPx = (16 * density).roundToInt().coerceAtLeast(32)
            val bitmap = Bitmap.createBitmap(tamanoPx, tamanoPx, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val periodoValido = periodo.coerceAtLeast(1L)
            val fraccion = (segundosRestantes.toFloat() / periodoValido).coerceIn(0f, 1f)

            val colorInt = try {
                Color.parseColor(colorContadorHex.ifBlank { AjustesDefaults.WidgetTotp.COLOR_CONTADOR })
            } catch (_: Exception) {
                Color.parseColor(AjustesDefaults.WidgetTotp.COLOR_CONTADOR)
            }

            val centro = tamanoPx / 2f
            val radio = centro - 1f

            // 1. Círculo translúcido de fondo
            val paintFondo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.argb(
                    (0.22f * 255).roundToInt(),
                    Color.red(colorInt),
                    Color.green(colorInt),
                    Color.blue(colorInt)
                )
                style = Paint.Style.FILL
            }
            canvas.drawCircle(centro, centro, radio, paintFondo)

            // 2. Arco relleno estilo Google Authenticator
            if (fraccion > 0.001f) {
                val paintArco = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = colorInt
                    style = Paint.Style.FILL
                }
                val rect = RectF(centro - radio, centro - radio, centro + radio, centro + radio)
                canvas.drawArc(rect, -90f, 360f * fraccion, true, paintArco)
            }

            return bitmap
        }

        private fun ejecutarVibracion(context: Context) {
            try {
                val repo = VaultRepository.obtener(context.applicationContext)
                val ajustes = repo.ajustes.actual
                Haptica.vibrarExterno(
                    context = context,
                    activo = ajustes.widgetHaptica,
                    intensidad = ajustes.widgetHapticaIntensidad
                )
            } catch (_: Exception) {
            }
        }
    }
}
