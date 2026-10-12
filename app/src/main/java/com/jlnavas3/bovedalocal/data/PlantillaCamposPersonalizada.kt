/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlantillaCamposPersonalizada(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String = "",
    val campos: List<CampoPersonalizado> = emptyList()
)
