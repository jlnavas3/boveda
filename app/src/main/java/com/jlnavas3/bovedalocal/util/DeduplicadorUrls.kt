/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

/**
 * Deduplica y normaliza listas de URLs y referencias a aplicaciones,
 * evitando entradas redundantes como 'ejemplo.com' frente a 'https://ejemplo.com',
 * o 'com.app.ejemplo' frente a 'android://...@com.app.ejemplo/'.
 */
object DeduplicadorUrls {

    /**
     * Devuelve una lista deduplicada de URLs preservando el orden original
     * y conservando siempre la versión más completa (con protocolo https://, hash de firma, etc.).
     */
    fun deduplicar(urls: List<String>): List<String> {
        if (urls.isEmpty()) return emptyList()

        val mapa = linkedMapOf<String, String>()

        for (raw in urls) {
            val u = raw.trim()
            if (u.isBlank()) continue

            val clave = calcularClaveCanonica(u)
            val existente = mapa[clave]
            if (existente == null) {
                mapa[clave] = u
            } else {
                mapa[clave] = seleccionarMejorUrl(existente, u)
            }
        }

        return mapa.values.toList()
    }

    /**
     * Calcula una clave canónica para identificar URLs o apps equivalentes.
     */
    fun calcularClaveCanonica(url: String): String {
        val u = url.trim().lowercase()

        // 1. App de Android explícita o implícita
        val paquete = LanzadorEnlaces.extraerPaquete(u)
        if (paquete != null) {
            return "app:$paquete"
        }

        // 2. Sitio web o IP
        val sinProtocolo = u
            .removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("//")
            .trim()

        val partesRuta = sinProtocolo.split('/', limit = 2)
        val hostConPuerto = partesRuta[0].substringBefore('?').substringBefore('#').trim()
        val hostSinPuerto = hostConPuerto.substringBefore(':')
        val hostNormalizado = hostSinPuerto.removePrefix("www.")

        val rutaConQuery = if (partesRuta.size > 1) {
            val r = "/" + partesRuta[1].trimEnd('/')
            if (r == "/") "" else r
        } else {
            val despuesHost = sinProtocolo.substringAfter(hostConPuerto, "")
            if (despuesHost.startsWith("?") || despuesHost.startsWith("#")) despuesHost else ""
        }

        return "web:$hostNormalizado$rutaConQuery"
    }

    /**
     * Compara dos variantes de la misma URL y devuelve la de mayor calidad o completitud:
     * - Prioriza https:// sobre http:// y sobre nombres sin protocolo.
     * - Prioriza android:// con hash de firma de Google Autofill sobre nombres de paquete planos.
     */
    private fun seleccionarMejorUrl(urlA: String, urlB: String): String {
        return if (puntuacionCalidad(urlB) > puntuacionCalidad(urlA)) urlB else urlA
    }

    private fun puntuacionCalidad(url: String): Int {
        var puntos = 0
        val u = url.trim().lowercase()

        if (u.startsWith("https://")) puntos += 30
        else if (u.startsWith("http://")) puntos += 15

        if (u.startsWith("android://")) {
            puntos += 30
            if (u.contains("@")) puntos += 20 // Firma digital de Play Integrity / Autofill
        }

        if (u.contains("www.")) puntos += 2
        puntos += url.length
        return puntos
    }
}
