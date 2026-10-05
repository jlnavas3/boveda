package com.jlnavas3.bovedalocal.autofill

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.autofill.FillResponse
import android.view.autofill.AutofillId
import android.widget.inline.InlinePresentationSpec
import com.jlnavas3.bovedalocal.ui.MainActivity

/**
 * Registra una acción interactiva en el FillResponse para permitir al usuario
 * crear una nueva entrada cuando no existan credenciales guardadas para el sitio o app.
 */
object CreadorAccionNuevaEntradaAutofill {

    @Suppress("DEPRECATION")
    fun aplicar(
        contexto: Context,
        respuesta: FillResponse.Builder,
        ids: Array<AutofillId>,
        datosSugeridos: DatosSugeridosAutofill,
        inlineSpec: InlinePresentationSpec?
    ) {
        val intent = Intent(contexto, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra("accion_shortcut", "nueva_entrada")
            putExtra("titulo_inicial", datosSugeridos.titulo)
            putExtra("url_inicial", datosSugeridos.url)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendiente = PendingIntent.getActivity(
            contexto,
            1002,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val presentacion = AutofillUtiles.presentacion(
            contexto = contexto,
            titulo = "Agregar a Bóveda local",
            subtitulo = "Crear entrada para ${datosSugeridos.titulo}",
            iconoBitmap = datosSugeridos.iconoBitmap
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && inlineSpec != null) {
            val inlineAccion = CreadorInlineSuggestion.crear(
                contexto = contexto,
                spec = inlineSpec,
                titulo = "Agregar a Bóveda",
                subtitulo = datosSugeridos.titulo,
                iconoBitmap = datosSugeridos.iconoBitmap
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                respuesta.setAuthentication(ids, pendiente.intentSender, presentacion, inlineAccion)
            } else {
                @Suppress("DEPRECATION")
                respuesta.setAuthentication(ids, pendiente.intentSender, presentacion, inlineAccion)
            }
        } else {
            @Suppress("DEPRECATION")
            respuesta.setAuthentication(ids, pendiente.intentSender, presentacion)
        }
    }
}
