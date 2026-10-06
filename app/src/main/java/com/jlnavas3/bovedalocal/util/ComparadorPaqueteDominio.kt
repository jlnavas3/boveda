package com.jlnavas3.bovedalocal.util

/**
 * Comparador especializado que determina si un destino guardado en una entrada
 * (URL web, dominio o paquete Android) es compatible con un contexto solicitado
 * (paquete de aplicación nativa o dominio web) por el framework de autocompletado.
 */
object ComparadorPaqueteDominio {

    private val tokensGenericos = setOf(
        "com", "org", "net", "edu", "gov", "mil", "io", "co", "app", "apps", "dev", "ai", "me",
        "android", "google", "mobile", "client", "tablet", "phone", "wear", "tv",
        "lite", "free", "pro", "plus", "beta", "debug", "release", "official", "main",
        "service", "services", "auth", "login", "account", "accounts", "core", "ui",
        "http", "https", "www", "mail", "api", "local", "localhost"
    )

    fun coincide(guardado: String, solicitado: String, mapeoPersonalizado: Map<String, String> = emptyMap()): Boolean {
        if (guardado.isBlank() || solicitado.isBlank()) return false
        val g = guardado.trim().lowercase()
        val s = solicitado.trim().lowercase()

        // 1. Coincidencia directa de cadenas
        if (g == s) return true

        // 2. Coincidencia directa de paquetes Android
        val paqG = LanzadorEnlaces.extraerPaquete(g)
        val paqS = LanzadorEnlaces.extraerPaquete(s)
        if (paqG != null && paqS != null && paqG.equals(paqS, ignoreCase = true)) return true

        val esPaqueteDirecto = { v: String -> LanzadorEnlaces.esNombrePaquete(v) }
        if (esPaqueteDirecto(g) && esPaqueteDirecto(s) && g == s) return true

        // 3. Coincidencia a través de catálogo de apps populares
        val paqueteObjetivo = paqS ?: (if (esPaqueteDirecto(s)) s else null)
        if (paqueteObjetivo != null) {
            val dominioMapeado = MapeadorPaquetesPopulares.obtenerDominio(paqueteObjetivo, mapeoPersonalizado)
            if (dominioMapeado != null) {
                val raizGuardado = Dominios.raiz(g)
                val raizMapeado = Dominios.raiz(dominioMapeado)
                if (raizGuardado.isNotEmpty() && raizGuardado == raizMapeado) return true
            }
        }

        // 4. Coincidencia inversa de catálogo si la entrada guardó un paquete
        val paqueteGuardado = paqG ?: (if (esPaqueteDirecto(g)) g else null)
        if (paqueteGuardado != null) {
            val dominioMapeado = MapeadorPaquetesPopulares.obtenerDominio(paqueteGuardado, mapeoPersonalizado)
            if (dominioMapeado != null) {
                val raizSolicitado = Dominios.raiz(s)
                val raizMapeado = Dominios.raiz(dominioMapeado)
                if (raizSolicitado.isNotEmpty() && raizSolicitado == raizMapeado) return true
            }
        }

        // 5. Coincidencia clásica por dominio raíz registrable
        val raizG = Dominios.raiz(g)
        val raizS = Dominios.raiz(s)
        if (paqG == null && paqS == null && !esPaqueteDirecto(g) && !esPaqueteDirecto(s)) {
            if (raizG.isNotEmpty() && raizG == raizS) return true
        }

        // 6. Heurística de marca para apps Android sin mapeo estático
        val algunoEsAppAndroid = paqueteObjetivo != null || paqueteGuardado != null ||
            g.startsWith("android://") || s.startsWith("android://") ||
            g.contains(".android.com") || s.contains(".android.com")

        if (algunoEsAppAndroid) {
            val tokensG = extraerTokensSignificativos(g)
            val tokensS = extraerTokensSignificativos(s)
            val interseccion = tokensG.intersect(tokensS)
            if (interseccion.isNotEmpty()) {
                return true
            }
        }

        return false
    }

    private fun extraerTokensSignificativos(texto: String): Set<String> {
        val limpio = if (texto.startsWith("android://")) {
            LanzadorEnlaces.extraerPaquete(texto) ?: texto.removePrefix("android://")
        } else {
            texto
        }
        return limpio
            .split('.', '/', ':', '@', '-', '_', '?')
            .map { it.trim().lowercase() }
            .filter { it.length >= 4 && it !in tokensGenericos }
            .toSet()
    }
}
