package com.jlnavas3.bovedalocal.autofill

import android.content.Context
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.FiltroNavegadoresWeb
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import com.jlnavas3.bovedalocal.util.MapeadorPaquetesPopulares
import com.jlnavas3.bovedalocal.util.ResolverIconoAppDominio

/**
 * Detecta y estructura los datos sugeridos (título, URL e icono) a partir del formulario
 * y la aplicación solicitante para precargar una nueva entrada.
 */
object DetectorDatosSugeridosAutofill {

    fun detectar(
        contexto: Context,
        paquete: String,
        dominioWeb: String?,
        navegadoresPersonalizados: List<String> = emptyList(),
        mapeoPersonalizado: Map<String, String> = emptyMap()
    ): DatosSugeridosAutofill {
        val domMapeado = if (paquete.isNotBlank()) MapeadorPaquetesPopulares.obtenerDominio(paquete, mapeoPersonalizado) else null
        val dom = dominioWeb?.takeIf { it.isNotBlank() }?.let { Dominios.raiz(it) } ?: domMapeado

        val paqueteDeDominio = dom?.let { ResolverIconoAppDominio.resolverPaquete(contexto, it) }
        val esNavegador = FiltroNavegadoresWeb.esNavegador(contexto, paquete, navegadoresPersonalizados) || !dominioWeb.isNullOrBlank()

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
            domMapeado != null -> "https://$domMapeado"
            paqueteEfectivo != null -> "android://$paqueteEfectivo"
            !esNavegador && paquete.isNotBlank() -> "android://$paquete"
            else -> ""
        }

        val icono = ResolverIconoGlifoTeclado.resolver(
            contexto = contexto,
            titulo = titulo,
            urls = if (url.isNotBlank()) listOf(url) else emptyList(),
            paquete = paqueteEfectivo,
            dominioWeb = dominioWeb
        )

        return DatosSugeridosAutofill(
            titulo = titulo,
            url = url,
            iconoBitmap = icono
        )
    }
}
