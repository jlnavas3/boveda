/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import com.jlnavas3.bovedalocal.ui.Pantalla

/**
 * Representa una recomendación o aviso interno accionable para el usuario dentro de la bóveda.
 */
@Immutable
data class AvisoInterno(
    val tipo: TipoAvisoInterno,
    val categoria: CategoriaAvisoInterno,
    val titulo: String,
    val descripcion: String,
    val accionTexto: String,
    val pantallaDestino: Pantalla,
    val cantidadAfectados: Int = 0
)
