/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Calcula un gris puro a partir de un valor de luminosidad entre 0.0f y 1.0f.
 */
fun crearGris(luminosidad: Float): Color {
    val l = luminosidad.coerceIn(0f, 1f)
    return Color(red = l, green = l, blue = l, alpha = 1f)
}
