/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.barra

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Modelo de datos inmutable que representa un botón de acción en la barra inferior fija.
 */
data class ItemAccionBarraInferior(
    val icono: ImageVector,
    val etiqueta: String,
    val alPulsar: () -> Unit,
    val colorIcono: Color? = null,
    val colorTexto: Color? = null,
    val activo: Boolean = true,
    val descripcion: String? = null
)
