/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

@Serializable
data class DatosPasskey(
    val rpId: String,
    val rpName: String,
    val userHandle: String,
    val credId: String,
    val clavePrivada: String,
    val algoritmo: String = "ES256",
    val usuario: String = ""
)
