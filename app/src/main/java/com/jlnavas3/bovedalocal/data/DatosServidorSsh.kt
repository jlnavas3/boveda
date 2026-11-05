/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Modelo de datos inmutable para servidores remotos, conexiones SSH y llaves criptográficas de acceso.
 */
@Immutable
@Serializable
data class DatosServidorSsh(
    val host: String = "",
    val puerto: String = "22",
    val usuario: String = "",
    val clavePrivada: String = "",
    val clavePublica: String = ""
)
