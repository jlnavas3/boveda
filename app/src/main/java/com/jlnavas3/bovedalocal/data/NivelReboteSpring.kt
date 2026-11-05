/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.animation.core.Spring

/**
 * Representa los niveles de amortiguación (rebote) de la física elástica Spring.
 */
enum class NivelReboteSpring(
    val id: String,
    val etiqueta: String,
    val descripcion: String,
    val dampingRatio: Float
) {
    SUTIL(
        id = "SUTIL",
        etiqueta = "Sutil",
        descripcion = "Frenado suave con un toque elástico apenas perceptible.",
        dampingRatio = Spring.DampingRatioLowBouncy
    ),
    EQUILIBRADO(
        id = "EQUILIBRADO",
        etiqueta = "Equilibrado",
        descripcion = "Rebote moderado y dinámico con buena respuesta visual.",
        dampingRatio = Spring.DampingRatioMediumBouncy
    ),
    DINAMICO(
        id = "DINAMICO",
        etiqueta = "Dinámico",
        descripcion = "Resorte pronunciado con oscilación elástica notoria.",
        dampingRatio = Spring.DampingRatioHighBouncy
    );

    companion object {
        fun desdeId(id: String): NivelReboteSpring =
            entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: SUTIL
    }
}
