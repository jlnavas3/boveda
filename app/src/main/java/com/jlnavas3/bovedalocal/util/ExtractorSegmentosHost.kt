/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

object ExtractorSegmentosHost {

    /**
     * Extrae los segmentos significativos de una URL o dominio (excluyendo protocolo, puertos,
     * www y TLD/sufijo público) formateados para usarse como sugerencias de nombre de servicio.
     *
     * Ejemplos:
     * - pornhub.app.com -> ["Pornhub", "App"]
     * - piccolericette.alternativeindustries.com -> ["Piccolericette", "Alternativeindustries"]
     * - api-dashboard.search.brave.com -> ["Api-Dashboard", "Search", "Brave"]
     * - google.com -> ["Google"]
     */
    fun extraer(
        urlODominio: String,
        tldsConfigurados: List<String> = emptyList()
    ): List<String> {
        val entrada = urlODominio.trim()
        if (entrada.isBlank()) return emptyList()

        val host = Dominios.host(entrada)
        if (host.isBlank() || !host.contains('.')) return emptyList()

        // Si es una dirección IP numérica, no extraer segmentos de marca
        if (host.split('.').all { it.toIntOrNull() != null }) return emptyList()

        var base = Dominios.sitioAgrupacion(entrada)
        if (base.isBlank()) base = host

        // Solo si PSL no encontró un sufijo público (base == host), podar TLDs manuales
        // Esto evita podar erróneamente dominios legítimos como 'app' en 'pornhub.app.com' cuando el TLD real ya era '.com'
        if (base == host) {
            val tldsLimpios = tldsConfigurados.map { it.lowercase().removePrefix(".") }.filter { it.isNotBlank() }
            while (base.contains('.')) {
                val ultimo = base.substringAfterLast('.').lowercase()
                if (tldsLimpios.contains(ultimo)) {
                    val restante = base.substringBeforeLast('.')
                    if (restante.contains('.')) {
                        base = restante
                    } else {
                        break
                    }
                } else {
                    break
                }
            }
        }

        val partes = base.split('.').filter { it.isNotBlank() && !it.equals("www", ignoreCase = true) }
        if (partes.isEmpty()) return emptyList()

        val segmentos = mutableListOf<String>()
        val vistos = mutableSetOf<String>()

        for (p in partes) {
            val formateado = formatearSegmento(p)
            if (formateado.isNotBlank() && vistos.add(formateado.lowercase())) {
                segmentos.add(formateado)
            }
        }

        return segmentos
    }

    private fun formatearSegmento(segmento: String): String {
        return segmento.split('-', '_')
            .filter { it.isNotBlank() }
            .joinToString("-") { palabra ->
                palabra.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
    }
}
