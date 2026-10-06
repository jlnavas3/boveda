package com.jlnavas3.bovedalocal.autofill

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.service.autofill.InlinePresentation
import android.widget.inline.InlinePresentationSpec
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.jlnavas3.bovedalocal.R
import com.jlnavas3.bovedalocal.ui.MainActivity

/**
 * Constructor especializado de sugerencias en línea (InlinePresentation)
 * para la barra superior del teclado (Android 11+ / API 30+).
 */
object CreadorInlineSuggestion {

    @Suppress("DEPRECATION")
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

            val icono = if (iconoBitmap != null) {
                Icon.createWithBitmap(iconoBitmap)
            } else {
                Icon.createWithBitmap(obtenerBitmapVector(contexto, R.drawable.ic_candado_boveda))
            }

            val contentBuilder = androidx.autofill.inline.v1.InlineSuggestionUi.newContentBuilder(intentAtribucion)
                .setTitle(titulo)
                .setStartIcon(icono)
                .setContentDescription(titulo)

            if (!subtitulo.isNullOrBlank()) {
                contentBuilder.setSubtitle(subtitulo)
            }

            val slice = contentBuilder.build().slice
            val specEfectiva = removerTinteDeSpec(contexto, spec)
            val pres = InlinePresentation(slice, specEfectiva, fijado)
            android.util.Log.i("BovedaAutofill", "CreadorInlineSuggestion OK: titulo=$titulo, tieneIconoBitmap=${iconoBitmap != null}")
            pres
        } catch (e: Exception) {
            android.util.Log.e("BovedaAutofill", "Error creando inline suggestion: ${e.javaClass.simpleName} - ${e.message}", e)
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun removerTinteDeSpec(contexto: Context, spec: InlinePresentationSpec): InlinePresentationSpec {
        return try {
            val estiloOriginal = spec.style
            estiloOriginal.classLoader = contexto.classLoader
            val estiloSanitizado = sanitizarEstiloSinTinte(estiloOriginal, contexto.classLoader)
            InlinePresentationSpec.Builder(spec.minSize, spec.maxSize)
                .setStyle(estiloSanitizado)
                .build()
        } catch (_: Exception) {
            spec
        }
    }

    @Suppress("DEPRECATION")
    private fun sanitizarEstiloSinTinte(bundle: Bundle, classLoader: ClassLoader): Bundle {
        bundle.classLoader = classLoader
        val copia = Bundle(bundle)
        copia.classLoader = classLoader
        val clavesTinte = listOf(
            "image_tint_list",
            "tint_list",
            "tint",
            "tint_mode",
            "image_tint_mode"
        )
        for (clave in clavesTinte) {
            copia.remove(clave)
        }
        for (clave in bundle.keySet()) {
            val valor = bundle.get(clave)
            if (valor is Bundle) {
                copia.putBundle(clave, sanitizarEstiloSinTinte(valor, classLoader))
            }
        }
        return copia
    }

    private fun obtenerBitmapVector(contexto: Context, resId: Int, tamanoPx: Int = 72): Bitmap {
        val drawable = androidx.core.content.ContextCompat.getDrawable(contexto, resId)
            ?: return Bitmap.createBitmap(tamanoPx, tamanoPx, Bitmap.Config.ARGB_8888)
        return drawable.toBitmap(
            width = tamanoPx,
            height = tamanoPx,
            config = Bitmap.Config.ARGB_8888
        )
    }
}
