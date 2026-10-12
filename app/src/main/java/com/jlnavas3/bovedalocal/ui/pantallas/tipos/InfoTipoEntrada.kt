/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Metadata representativa para las pantallas dedicadas por tipo de entrada.
 */
data class InfoTipoEntrada(
    val tituloPlural: String,
    val tituloSingular: String,
    val subtitulo: String,
    val icono: ImageVector,
    val color: Color,
    val idEtiqueta: String,
    val descripcionCrear: String,
    val textoVacioTotal: String,
    val palabrasClave: List<String>
)
