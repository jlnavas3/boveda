/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class CriterioOrdenRegistro(val etiqueta: String) {
    RECIENTES("Más recientes"),
    ANTIGUOS("Más antiguos"),
    AREA_AZ("Área (A - Z)"),
    AREA_ZA("Área (Z - A)")
}

data class CategoriaOpcion(
    val nombre: String,
    val descripcion: String,
    val icono: ImageVector,
    val color: Color
)

data class EventoRegistro(
    val timestamp: String,
    val area: String,
    val mensaje: String,
    val esError: Boolean,
    val textoCompleto: String
)
