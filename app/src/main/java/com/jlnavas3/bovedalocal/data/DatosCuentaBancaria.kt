/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Modelo de datos inmutable para cuentas bancarias, IBAN, transferencias y claves bancarias.
 */
@Immutable
@Serializable
data class DatosCuentaBancaria(
    val banco: String = "",
    val titular: String = "",
    val tipoCuenta: String = "Corriente",
    val numeroCuenta: String = "",
    val swiftBic: String = "",
    val cbuClabe: String = ""
)
