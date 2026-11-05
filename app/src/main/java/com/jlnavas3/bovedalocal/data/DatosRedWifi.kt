/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Modelo de datos inmutable para redes y conexiones Wi-Fi protegidas.
 */
@Immutable
@Serializable
data class DatosRedWifi(
    val ssid: String = "",
    val contrasena: String = "",
    val tipoSeguridad: String = "WPA",
    val oculta: Boolean = false
)
