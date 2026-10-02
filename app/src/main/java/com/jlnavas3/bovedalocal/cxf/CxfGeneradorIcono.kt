package com.jlnavas3.bovedalocal.cxf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.jlnavas3.bovedalocal.R

/**
 * Generador modular de mapa de bits para el icono de exportación FIDO CXF.
 * Garantiza la correcta renderización de iconos adaptativos en el selector del sistema.
 */
object CxfGeneradorIcono {

    fun generar(context: Context, tamanoDp: Int = 96): Bitmap {
        return try {
            val drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
                ?: context.packageManager.getApplicationIcon(context.packageName)

            val tamanoPx = (tamanoDp * context.resources.displayMetrics.density).toInt().coerceAtLeast(96)
            val bitmap = Bitmap.createBitmap(tamanoPx, tamanoPx, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (_: Exception) {
            Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888)
        }
    }
}
