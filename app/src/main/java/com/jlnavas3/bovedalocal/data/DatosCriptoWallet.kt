/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Modelo de datos inmutable para billeteras criptográficas, direcciones públicas, frases semilla y llaves privadas.
 */
@Immutable
@Serializable
data class DatosCriptoWallet(
    val red: String = "Bitcoin",
    val direccionPublica: String = "",
    val fraseSemilla: String = "",
    val clavePrivada: String = ""
)
