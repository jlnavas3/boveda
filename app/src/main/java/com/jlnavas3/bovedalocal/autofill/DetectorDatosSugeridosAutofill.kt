package com.jlnavas3.bovedalocal.autofill

import android.content.Context
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import com.jlnavas3.bovedalocal.util.MapeadorPaquetesPopulares

/**
 * Detecta y estructura los datos sugeridos (título, URL e icono) a partir del formulario
 * y la aplicación solicitante para precargar una nueva entrada.
 */
object DetectorDatosSugeridosAutofill {

    fun detectar(contexto: Context, paquete: String, dominioWeb: String?): DatosSugeridosAutofill {
        val dom = dominioWeb?.takeIf { it.isNotBlank() }?.let { Dominios.raiz(it) }
        val paqueteDeDominio = dom?.let { MapeadorPaquetesPopulares.obtenerPaquete(it) }
        val paqueteEfectivo = when {
            paquete.isNotBlank() && paquete != "android" && paquete != contexto.packageName -> paquete
            paqueteDeDominio != null && LanzadorEnlaces.estaInstalada(contexto, paqueteDeDominio) -> paqueteDeDominio
            else -> null
        }

        val nombreApp = if (paqueteEfectivo != null && LanzadorEnlaces.estaInstalada(contexto, paqueteEfectivo)) {
            LanzadorEnlaces.obtenerNombreApp(contexto, paqueteEfectivo)
        } else null

        val titulo = when {
            !nombreApp.isNullOrBlank() -> nombreApp
            !dom.isNullOrBlank() -> dom
            paqueteEfectivo != null -> Dominios.dominioDePaquete(paqueteEfectivo)
            else -> "Nueva entrada"
        }

        val url = when {
            !dominioWeb.isNullOrBlank() -> "https://$dominioWeb"
            paqueteEfectivo != null -> "android://$paqueteEfectivo"
            else -> ""
        }

        val icono = if (paqueteEfectivo != null && LanzadorEnlaces.estaInstalada(contexto, paqueteEfectivo)) {
            AutofillUtiles.obtenerBitmapIconoCircular(contexto, paqueteEfectivo)
        } else null

        return DatosSugeridosAutofill(
            titulo = titulo,
            url = url,
            iconoBitmap = icono
        )
    }
}
