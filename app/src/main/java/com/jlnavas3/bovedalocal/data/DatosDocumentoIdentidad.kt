/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Modelo de datos inmutable para documentos oficiales de identificación nacional e internacional.
 */
@Immutable
@Serializable
data class DatosDocumentoIdentidad(
    val tipoDocumento: String = "CI",
    val numero: String = "",
    val nombreCompleto: String = "",
    val expedicion: String = "",
    val caducidad: String = "",
    val paisEmisor: String = ""
)
