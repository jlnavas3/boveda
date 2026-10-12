/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Agrupa entradas por título (case-insensitive) preservando entradas con títulos equivalentes en un solo grupo.
 */
fun claveAgrupacionPorTitulo(entrada: Entrada): String {
    val t = entrada.titulo.trim()
    if (t.isNotBlank()) return t.lowercase()
    val sitio = claveAgrupacionSitio(entrada)
    if (!sitio.isNullOrBlank()) return sitio.lowercase()
    return "sin título"
}
