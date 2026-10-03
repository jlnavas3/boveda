package com.jlnavas3.bovedalocal.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.quicksettings.GeneradorRapidoHelper
import kotlin.math.roundToInt

class WidgetGeneradorRapido : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (id in appWidgetIds) {
            actualizarWidget(context, appWidgetManager, id)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        actualizarWidget(context, appWidgetManager, appWidgetId)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_GENERAR) {
            GeneradorRapidoHelper.generar(context, "Widget 1x1")
        }
    }

    companion object {
        const val ACTION_GENERAR = "com.jlnavas3.bovedalocal.widget.GENERAR_RAPIDO"

        fun actualizarWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_generador_rapido)
            val repo = VaultRepository.obtener(context)
            val ajustes = repo.ajustes.actual

            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val isLandscape = context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            val widthDp = if (isLandscape) {
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, 70)
            } else {
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 70)
            }.let { if (it <= 0) 70 else it }

            val heightDp = if (isLandscape) {
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 70)
            } else {
                options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, 70)
            }.let { if (it <= 0) 70 else it }

            val density = context.resources.displayMetrics.density
            val cellWidthPx = (widthDp * density).roundToInt().coerceAtLeast(60)
            val cellHeightPx = (heightDp * density).roundToInt().coerceAtLeast(60)

            val anchoPx = (ajustes.widget1x1AnchoDp * density).roundToInt().coerceIn(20, cellWidthPx)
            val altoPx = (ajustes.widget1x1AltoDp * density).roundToInt().coerceIn(20, cellHeightPx)

            val baseLeft = when (ajustes.widget1x1Alineamiento.lowercase()) {
                "abajo" -> (cellWidthPx - anchoPx) / 2f
                "centro" -> (cellWidthPx - anchoPx) / 2f
                "izquierda" -> 0f
                "derecha" -> (cellWidthPx - anchoPx).toFloat()
                else /* "arriba" */ -> (cellWidthPx - anchoPx) / 2f
            }

            val baseTop = when (ajustes.widget1x1Alineamiento.lowercase()) {
                "abajo" -> (cellHeightPx - altoPx).toFloat()
                "centro" -> (cellHeightPx - altoPx) / 2f
                "izquierda" -> (cellHeightPx - altoPx) / 2f
                "derecha" -> (cellHeightPx - altoPx) / 2f
                else /* "arriba" */ -> 0f
            }

            val offsetX = ajustes.widget1x1OffsetX * density
            val offsetY = ajustes.widget1x1OffsetY * density

            val left = (baseLeft + offsetX).coerceIn(0f, (cellWidthPx - anchoPx).toFloat().coerceAtLeast(0f))
            val top = (baseTop + offsetY).coerceIn(0f, (cellHeightPx - altoPx).toFloat().coerceAtLeast(0f))

            val bitmap = Bitmap.createBitmap(cellWidthPx, cellHeightPx, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val radioPx = ajustes.widget1x1CurvaturaEsquinasDp * density
            val grosorPx = ajustes.widget1x1GrosorBordeDp * density
            val rectBoton = RectF(left, top, left + anchoPx, top + altoPx)
            val vidrioActivo = ajustes.widget1x1VidrioEsmerilado
            val esmIntensidad = ajustes.widget1x1EsmeriladoIntensidad.coerceIn(0.1f, 1f)
            val esmLuz = ajustes.widget1x1EsmeriladoLuz.coerceIn(0f, 1f)

            // Fondo
            val colorFondoBase = try {
                Color.parseColor(ajustes.widget1x1ColorFondo.ifBlank { AjustesDefaults.Widget1x1.COLOR_FONDO })
            } catch (_: Exception) {
                Color.parseColor(AjustesDefaults.Widget1x1.COLOR_FONDO)
            }
            val alphaFondo = (ajustes.widget1x1TransparenciaFondo.coerceIn(0f, 1f) * 255).roundToInt()
            if (alphaFondo > 0) {
                val paintFondo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    if (vidrioActivo) {
                        val r = Color.red(colorFondoBase)
                        val g = Color.green(colorFondoBase)
                        val b = Color.blue(colorFondoBase)
                        val alphaTop = ((alphaFondo * 0.45f) + (65 * esmLuz * esmIntensidad)).roundToInt().coerceIn(0, 255)
                        val rTop = (r + ((255 - r) * 0.40f * esmLuz)).roundToInt().coerceIn(0, 255)
                        val gTop = (g + ((255 - g) * 0.40f * esmLuz)).roundToInt().coerceIn(0, 255)
                        val bTop = (b + ((255 - b) * 0.40f * esmLuz)).roundToInt().coerceIn(0, 255)
                        val colorTop = Color.argb(alphaTop, rTop, gTop, bTop)
                        val colorMid = Color.argb(alphaFondo, r, g, b)
                        val colorBot = Color.argb(
                            alphaFondo,
                            (r * 0.90f).roundToInt(),
                            (g * 0.90f).roundToInt(),
                            (b * 0.90f).roundToInt()
                        )
                        shader = LinearGradient(
                            rectBoton.left, rectBoton.top, rectBoton.left, rectBoton.bottom,
                            intArrayOf(colorTop, colorMid, colorBot),
                            floatArrayOf(0f, 0.40f, 1f),
                            Shader.TileMode.CLAMP
                        )
                    } else {
                        color = Color.argb(
                            alphaFondo,
                            Color.red(colorFondoBase),
                            Color.green(colorFondoBase),
                            Color.blue(colorFondoBase)
                        )
                    }
                    style = Paint.Style.FILL
                }
                canvas.drawRoundRect(rectBoton, radioPx, radioPx, paintFondo)

                // Reflejo cenital sutil en la parte superior del vidrio
                if (vidrioActivo && esmLuz > 0.05f) {
                    val paintSheen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        val alphaSheen = (75 * esmLuz * esmIntensidad).roundToInt().coerceIn(0, 255)
                        shader = LinearGradient(
                            rectBoton.left, rectBoton.top,
                            rectBoton.left, rectBoton.top + (rectBoton.height() * 0.38f),
                            Color.argb(alphaSheen, 255, 255, 255),
                            Color.TRANSPARENT,
                            Shader.TileMode.CLAMP
                        )
                        style = Paint.Style.FILL
                    }
                    canvas.drawRoundRect(rectBoton, radioPx, radioPx, paintSheen)
                }
            }

            // Borde
            if (grosorPx > 0.2f) {
                val colorBordeBase = try {
                    Color.parseColor(ajustes.widget1x1ColorBorde.ifBlank { AjustesDefaults.Widget1x1.COLOR_BORDE })
                } catch (_: Exception) {
                    Color.parseColor(AjustesDefaults.Widget1x1.COLOR_BORDE)
                }
                val alphaBorde = (alphaFondo.coerceAtLeast(140)).coerceAtMost(255)
                val paintBorde = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    if (vidrioActivo) {
                        val alphaBordeTop = ((alphaBorde * 0.6f) + (90 * esmLuz)).roundToInt().coerceIn(0, 255)
                        val colorBordeTop = Color.argb(alphaBordeTop, 255, 255, 255)
                        val colorBordeBot = Color.argb(
                            alphaBorde,
                            Color.red(colorBordeBase),
                            Color.green(colorBordeBase),
                            Color.blue(colorBordeBase)
                        )
                        shader = LinearGradient(
                            rectBoton.left, rectBoton.top, rectBoton.left, rectBoton.bottom,
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
                    left + medioGrosor,
                    top + medioGrosor,
                    left + anchoPx - medioGrosor,
                    top + altoPx - medioGrosor
                )
                val radioBorde = (radioPx - medioGrosor).coerceAtLeast(0f)
                canvas.drawRoundRect(rectBorde, radioBorde, radioBorde, paintBorde)
            }

            // Ícono
            val colorIconoBase = try {
                Color.parseColor(ajustes.widget1x1ColorIcono.ifBlank { AjustesDefaults.Widget1x1.COLOR_ICONO })
            } catch (_: Exception) {
                Color.parseColor(AjustesDefaults.Widget1x1.COLOR_ICONO)
            }
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_widget_generador)?.mutate()
            if (drawable != null) {
                DrawableCompat.setTint(drawable, colorIconoBase)
                val minLado = minOf(anchoPx, altoPx)
                val iconSize = (minLado * 0.52f).roundToInt().coerceAtLeast(14)
                val iconLeft = (left + (anchoPx - iconSize) / 2f).roundToInt()
                val iconTop = (top + (altoPx - iconSize) / 2f).roundToInt()
                drawable.setBounds(iconLeft, iconTop, iconLeft + iconSize, iconTop + iconSize)
                drawable.draw(canvas)
            }

            views.setImageViewBitmap(R.id.widget_generador_imagen, bitmap)

            val intent = Intent(context, WidgetGeneradorRapido::class.java).apply {
                action = ACTION_GENERAR
            }
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, flags)
            views.setOnClickPendingIntent(R.id.widget_generador_root, pendingIntent)
            views.setOnClickPendingIntent(R.id.widget_generador_imagen, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun actualizarTodos(context: Context) {
            val manager = AppWidgetManager.getInstance(context) ?: return
            val ids = manager.getAppWidgetIds(ComponentName(context, WidgetGeneradorRapido::class.java))
            for (id in ids) {
                actualizarWidget(context, manager, id)
            }
        }
    }
}
