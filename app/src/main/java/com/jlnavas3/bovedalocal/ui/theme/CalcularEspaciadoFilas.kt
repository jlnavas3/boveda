/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp

/**
 * Calcula el espaciado vertical entre filas según la densidad de la lista y la configuración global de Formas y Bordes.
 * Permite 0.dp cuando el usuario configura espaciado a cero.
 */
fun calcularEspaciadoFilas(densidad: String = "estandar", base: Dp = EspaciadoComponentes): Dp {
    if (base <= 0.dp) return 0.dp
    return when (densidad) {
        "compacta" -> (base * 0.45f).coerceAtLeast(0.dp)
        "comoda" -> (base * 0.7f).coerceAtLeast(0.dp)
        else -> base
    }
}
