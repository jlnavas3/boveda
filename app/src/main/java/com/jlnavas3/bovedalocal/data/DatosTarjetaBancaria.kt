/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Modelo de datos inmutable para registros de tarjetas de débito o crédito.
 */
@Immutable
@Serializable
data class DatosTarjetaBancaria(
    val titular: String = "",
    val numero: String = "",
    val vencimiento: String = "",
    val cvv: String = "",
    val pin: String = "",
    val tipo: String = "Crédito",
    val marca: String = ""
)
