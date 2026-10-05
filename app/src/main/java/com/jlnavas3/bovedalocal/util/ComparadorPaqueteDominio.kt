package com.jlnavas3.bovedalocal.util

/**
 * Comparador especializado que determina si un destino guardado en una entrada
 * (URL web, dominio o paquete Android) es compatible con un contexto solicitado
 * (paquete de aplicación nativa o dominio web) por el framework de autocompletado.
 */
object ComparadorPaqueteDominio {

    fun coincide(guardado: String, solicitado: String): Boolean {
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
            val dominioMapeado = MapeadorPaquetesPopulares.obtenerDominio(paqueteObjetivo)
            if (dominioMapeado != null) {
                val raizGuardado = Dominios.raiz(g)
                val raizMapeado = Dominios.raiz(dominioMapeado)
                if (raizGuardado.isNotEmpty() && raizGuardado == raizMapeado) return true
            }
        }

        // 4. Coincidencia inversa de catálogo si la entrada guardó un paquete
        val paqueteGuardado = paqG ?: (if (esPaqueteDirecto(g)) g else null)
        if (paqueteGuardado != null) {
            val dominioMapeado = MapeadorPaquetesPopulares.obtenerDominio(paqueteGuardado)
            if (dominioMapeado != null) {
                val raizSolicitado = Dominios.raiz(s)
                val raizMapeado = Dominios.raiz(dominioMapeado)
                if (raizSolicitado.isNotEmpty() && raizSolicitado == raizMapeado) return true
            }
        }

        // 5. Coincidencia clásica por dominio raíz registrable
        val raizG = Dominios.raiz(g)
        val raizS = Dominios.raiz(s)
        if (raizG.isNotEmpty() && raizG == raizS) return true

        // 6. Heurística de marca para apps Android sin mapeo estático
        // Aplica únicamente cuando uno es paquete nativo y el otro es URL/dominio web
        if (paqueteObjetivo != null && esPaqueteDirecto(paqueteObjetivo) && !esPaqueteDirecto(g) && raizG.isNotEmpty()) {
            val marcaWeb = Dominios.marca(g)
            if (marcaWeb.length >= 4) {
                val segmentos = paqueteObjetivo.split('.')
                if (segmentos.any { it.equals(marcaWeb, ignoreCase = true) }) {
                    return true
                }
            }
            val candidatoDominio = Dominios.dominioDePaquete(paqueteObjetivo)
            if (Dominios.raiz(candidatoDominio) == raizG) {
                return true
            }
        }

        return false
    }
}
