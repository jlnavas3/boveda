package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.LruCache
import com.jlnavas3.bovedalocal.autofill.AutofillUtiles

/**
 * Resuelve el paquete y el icono de la aplicación nativa instalada que maneja un dominio web,
 * consultando los Android App Links registrados en el sistema de manera jerárquica con caché LRU.
 */
object ResolverIconoAppDominio {

    private val cachePaquetes = LruCache<String, String>(50)
    private val cacheBitmaps = LruCache<String, Bitmap>(50)

    fun resolverPaquete(contexto: Context, dominio: String): String? {
        val clave = dominio.trim().lowercase()
        if (clave.isBlank()) return null

        synchronized(cachePaquetes) {
            cachePaquetes.get(clave)?.let { return it }
        }

        val normalizado = NormalizadorDominioWeb.normalizar(clave)
        val candidatos = normalizado?.hostsCandidatos ?: listOf(clave)
        val navegadores = FiltroNavegadoresWeb.obtenerNavegadores(contexto)
        val pm = contexto.packageManager
        val flags = PackageManager.MATCH_ALL

        var paqueteEncontrado: String? = null

        for (host in candidatos) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://$host")).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                }
                val resoluciones = pm.queryIntentActivities(intent, flags)
                val appInfo = resoluciones.firstOrNull { res ->
                    val pkg = res.activityInfo?.packageName ?: ""
                    pkg.isNotBlank() && pkg !in navegadores && pkg != contexto.packageName
                }?.activityInfo?.packageName

                if (appInfo != null && LanzadorEnlaces.estaInstalada(contexto, appInfo)) {
                    paqueteEncontrado = appInfo
                    break
                }
            } catch (_: Exception) {}
        }

        if (paqueteEncontrado == null) {
            val domRaiz = normalizado?.dominioRaiz ?: Dominios.raiz(clave)
            val paquetePopular = MapeadorPaquetesPopulares.obtenerPaquete(domRaiz)
            if (paquetePopular != null && LanzadorEnlaces.estaInstalada(contexto, paquetePopular)) {
                paqueteEncontrado = paquetePopular
            }
        }

        if (paqueteEncontrado != null) {
            synchronized(cachePaquetes) {
                cachePaquetes.put(clave, paqueteEncontrado)
            }
        }

        return paqueteEncontrado
    }

    fun resolverBitmapIcono(contexto: Context, dominio: String): Bitmap? {
        val clave = dominio.trim().lowercase()
        if (clave.isBlank()) return null

        synchronized(cacheBitmaps) {
            cacheBitmaps.get(clave)?.let { return it }
        }

        val paquete = resolverPaquete(contexto, clave) ?: return null
        val bitmap = AutofillUtiles.obtenerBitmapIcono(contexto, paquete)
        if (bitmap != null) {
            synchronized(cacheBitmaps) {
                cacheBitmaps.put(clave, bitmap)
            }
        }
        return bitmap
    }
}
