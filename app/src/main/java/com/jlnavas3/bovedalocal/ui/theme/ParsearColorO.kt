/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Parsea un código hexadecimal (#RRGGBB o #AARRGGBB) a [Color] con valor por defecto seguro ante errores.
 */
fun parsearColorO(hex: String, porDefecto: Color): Color {
    if (hex.isBlank()) return porDefecto
    return try {
        val limpio = hex.removePrefix("#").trim()
        val valorLong = limpio.toLong(16)
        when (limpio.length) {
            6 -> Color((0xFF000000 or valorLong).toInt())
            8 -> Color(valorLong)
            else -> porDefecto
        }
    } catch (_: Exception) {
        porDefecto
    }
}
