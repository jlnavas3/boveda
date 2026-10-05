package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import com.jlnavas3.bovedalocal.autofill.AutofillUtiles

/**
 * Resuelve el paquete y el icono de la aplicación nativa instalada que maneja un dominio web,
 * consultando los Android App Links registrados en el sistema.
 */
object ResolverIconoAppDominio {

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

    fun resolverPaquete(contexto: Context, dominio: String): String? {
        val domLimpio = Dominios.raiz(dominio)
        val url = if (domLimpio.startsWith("http")) domLimpio else "https://$domLimpio"
        return try {
            val pm = contexto.packageManager
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addCategory(Intent.CATEGORY_BROWSABLE)
            }
            val resoluciones = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            val paqueteEncontrado = resoluciones.firstOrNull { res ->
                val pkg = res.activityInfo?.packageName ?: ""
                pkg.isNotBlank() && pkg !in PAQUETES_NAVEGADORES && !esNavegadorPredeterminado(pm, pkg)
            }?.activityInfo?.packageName

            if (paqueteEncontrado != null && LanzadorEnlaces.estaInstalada(contexto, paqueteEncontrado)) {
                paqueteEncontrado
            } else {
                val paquetePopular = MapeadorPaquetesPopulares.obtenerPaquete(domLimpio)
                if (paquetePopular != null && LanzadorEnlaces.estaInstalada(contexto, paquetePopular)) {
                    paquetePopular
                } else null
            }
        } catch (_: Exception) {
            val paquetePopular = MapeadorPaquetesPopulares.obtenerPaquete(domLimpio)
            if (paquetePopular != null && LanzadorEnlaces.estaInstalada(contexto, paquetePopular)) {
                paquetePopular
            } else null
        }
    }

    fun resolverBitmapIcono(contexto: Context, dominio: String): Bitmap? {
        val paquete = resolverPaquete(contexto, dominio) ?: return null
        return AutofillUtiles.obtenerBitmapIconoCircular(contexto, paquete)
    }

    private fun esNavegadorPredeterminado(pm: PackageManager, packageName: String): Boolean {
        return try {
            val intentGenerico = Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"))
            val res = pm.resolveActivity(intentGenerico, PackageManager.MATCH_DEFAULT_ONLY)
            res?.activityInfo?.packageName == packageName
        } catch (_: Exception) {
            false
        }
    }
}
