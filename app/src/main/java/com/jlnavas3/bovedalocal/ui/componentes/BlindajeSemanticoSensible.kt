/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription

/**
 * Microcomponente que aplica blindaje semántico para evitar que servicios
 * de accesibilidad maliciosos o herramientas de screen scraping extraigan
 * contraseñas y datos sensibles en claro del árbol de vistas de Android.
 */
fun Modifier.blindajeSemanticoSensible(
    esSensible: Boolean,
    etiquetaAccesible: String? = null
): Modifier {
    if (!esSensible) return this
    return this.clearAndSetSemantics {
        if (!etiquetaAccesible.isNullOrBlank()) {
            contentDescription = "$etiquetaAccesible (protegido)"
        }
    }
}
