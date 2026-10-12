/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.Entrada

data class CategoriaExportacion(
    val id: String,
    val etiqueta: String,
    val icono: ImageVector,
    val color: Color,
    val filtro: (Entrada) -> Boolean
)
