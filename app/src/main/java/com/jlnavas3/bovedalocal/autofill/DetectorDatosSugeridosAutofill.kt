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

    private val PAQUETES_NAVEGADORES = setOf(
        "com.brave.browser",
        "com.android.chrome",
        "org.mozilla.firefox",
        "com.microsoft.emmx",
        "com.opera.browser",
        "com.opera.mini.native",
        "com.duckduckgo.mobile.android",
        "com.sec.android.app.sbrowser",
        "com.vivaldi.browser"
    )

    fun detectar(contexto: Context, paquete: String, dominioWeb: String?): DatosSugeridosAutofill {
        val dom = dominioWeb?.takeIf { it.isNotBlank() }?.let { Dominios.raiz(it) }
        val paqueteDeDominio = dom?.let { MapeadorPaquetesPopulares.obtenerPaquete(it) }
        val esNavegador = paquete in PAQUETES_NAVEGADORES || !dominioWeb.isNullOrBlank()

        val paqueteEfectivo = when {
            paqueteDeDominio != null && LanzadorEnlaces.estaInstalada(contexto, paqueteDeDominio) -> paqueteDeDominio
            !esNavegador && paquete.isNotBlank() && paquete != "android" && paquete != contexto.packageName -> paquete
            else -> null
        }

        val nombreApp = if (paqueteEfectivo != null && LanzadorEnlaces.estaInstalada(contexto, paqueteEfectivo)) {
            LanzadorEnlaces.obtenerNombreApp(contexto, paqueteEfectivo)
        } else null

        val titulo = when {
            !nombreApp.isNullOrBlank() -> nombreApp
            dom != null && dom.isNotBlank() -> dom.replaceFirstChar { it.uppercase() }
            paqueteEfectivo != null -> Dominios.dominioDePaquete(paqueteEfectivo)
            !esNavegador && paquete.isNotBlank() && LanzadorEnlaces.estaInstalada(contexto, paquete) -> {
                LanzadorEnlaces.obtenerNombreApp(contexto, paquete)?.ifBlank { null } ?: "Nueva entrada"
            }
            else -> "Nueva entrada"
        }

        val url = when {
            !dominioWeb.isNullOrBlank() -> "https://$dominioWeb"
            paqueteEfectivo != null -> "android://$paqueteEfectivo"
            !esNavegador && paquete.isNotBlank() -> "android://$paquete"
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
