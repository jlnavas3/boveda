package com.jlnavas3.bovedalocal.autofill

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Build
import android.service.autofill.InlinePresentation
import android.widget.inline.InlinePresentationSpec
import androidx.annotation.RequiresApi
import androidx.autofill.inline.v1.InlineSuggestionUi
import android.content.Intent
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.ui.MainActivity

/**
 * Constructor especializado de sugerencias en línea (InlinePresentation)
 * para la barra superior del teclado (Android 11+ / API 30+).
 */
object CreadorInlineSuggestion {

    @RequiresApi(Build.VERSION_CODES.R)
    fun crear(
        contexto: Context,
        spec: InlinePresentationSpec,
        titulo: String,
        subtitulo: String? = null,
        iconoBitmap: Bitmap? = null,
        intencionPendiente: PendingIntent? = null,
        fijado: Boolean = false
    ): InlinePresentation? {
        return try {
            val intentAtribucion = intencionPendiente ?: PendingIntent.getActivity(
                contexto,
                0,
                Intent(contexto, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val contentBuilder = InlineSuggestionUi.newContentBuilder(intentAtribucion)
                .setTitle(titulo)
                .setContentDescription(titulo)

            if (!subtitulo.isNullOrBlank()) {
                contentBuilder.setSubtitle(subtitulo)
            }

            val icono = if (iconoBitmap != null) {
                Icon.createWithBitmap(iconoBitmap)
            } else {
                Icon.createWithBitmap(obtenerBitmapVector(contexto, R.drawable.ic_candado_boveda))
            }
            contentBuilder.setStartIcon(icono)

            val slice = contentBuilder.build().slice
            val pres = InlinePresentation(slice, spec, fijado)
            android.util.Log.i("BovedaAutofill", "CreadorInlineSuggestion OK: titulo=$titulo, pres=$pres")
            pres
        } catch (e: Exception) {
            android.util.Log.e("BovedaAutofill", "Error creando inline suggestion: ${e.javaClass.simpleName} - ${e.message}", e)
            null
        }
    }

    private fun obtenerBitmapVector(contexto: Context, resId: Int, tamanoPx: Int = 72): Bitmap {
        val drawable = androidx.core.content.ContextCompat.getDrawable(contexto, resId)
            ?: return Bitmap.createBitmap(tamanoPx, tamanoPx, Bitmap.Config.ARGB_8888)
        val bitmap = Bitmap.createBitmap(tamanoPx, tamanoPx, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, tamanoPx, tamanoPx)
        drawable.draw(canvas)
        return bitmap
    }
}
