package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build

/**
 * Detecta y filtra paquetes correspondientes a navegadores web en el dispositivo,
 * combinando categorías del sistema, esquemas genéricos, lista conocida de respaldo
 * y navegadores personalizados por el usuario.
 */
object FiltroNavegadoresWeb {

    val NAVEGADORES_CONOCIDOS = setOf(
        "com.brave.browser",
        "com.android.chrome",
        "org.mozilla.firefox",
        "com.microsoft.emmx",
        "com.opera.browser",
        "com.opera.mini.native",
        "com.duckduckgo.mobile.android",
        "com.sec.android.app.sbrowser",
        "com.vivaldi.browser",
        "mark.via.gp",
        "org.bromite.bromite"
    )

    @Volatile
    private var cachePaquetes: Set<String>? = null
    @Volatile
    private var timestampCache: Long = 0L
    private const val TTL_CACHE_MS = 60_000L

    fun obtenerNavegadores(contexto: Context? = null, navegadoresPersonalizados: List<String> = emptyList()): Set<String> {
        val ahora = System.currentTimeMillis()
        val actual = cachePaquetes
        val base = if (actual != null && ahora - timestampCache < TTL_CACHE_MS) {
            actual
        } else {
            val resultado = mutableSetOf<String>()
            resultado.addAll(NAVEGADORES_CONOCIDOS)

            val pm = contexto?.packageManager
            val flags = PackageManager.MATCH_ALL

            if (pm != null) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val intentCategoria = Intent(Intent.ACTION_VIEW, Uri.parse("https://")).apply {
                            addCategory(Intent.CATEGORY_APP_BROWSER)
                        }
                        pm.queryIntentActivities(intentCategoria, flags).forEach { info ->
                            info.activityInfo?.packageName?.let { resultado.add(it) }
                        }
                    }
                } catch (_: Exception) {}

                try {
                    val intentGenerico = Intent(Intent.ACTION_VIEW, Uri.parse("https://")).apply {
                        addCategory(Intent.CATEGORY_BROWSABLE)
                    }
                    pm.queryIntentActivities(intentGenerico, flags).forEach { info ->
                        info.activityInfo?.packageName?.let { resultado.add(it) }
                    }
                } catch (_: Exception) {}
            }

            cachePaquetes = resultado
            timestampCache = ahora
            resultado
        }

        return if (navegadoresPersonalizados.isEmpty()) {
            base
        } else {
            base + navegadoresPersonalizados.map { it.trim().lowercase() }
        }
    }

    fun esNavegador(contexto: Context? = null, paquete: String, navegadoresPersonalizados: List<String> = emptyList()): Boolean {
        if (paquete.isBlank()) return false
        val p = paquete.trim().lowercase()
        if (navegadoresPersonalizados.any { it.trim().equals(p, ignoreCase = true) }) return true
        return p in obtenerNavegadores(contexto, navegadoresPersonalizados)
    }
}
