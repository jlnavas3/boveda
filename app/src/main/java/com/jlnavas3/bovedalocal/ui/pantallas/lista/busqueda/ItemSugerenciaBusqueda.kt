/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Representa una sugerencia de filtro rápido para la barra de búsqueda.
 */
data class ItemSugerenciaBusqueda(
    val comando: String,
    val etiqueta: String,
    val icono: ImageVector,
    val categoria: String,
    val requiereParametro: Boolean = false
)
