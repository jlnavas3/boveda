/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Modelo estructurado para almacenar factores 2FA (TOTP) secundarios o alternativos
 * provenientes de la unificación de entradas o configuración múltiple de seguridad.
 */
@Immutable
@Serializable
data class TotpSecundario(
    val id: String = UUID.randomUUID().toString(),
    val secreto: String,
    val emisor: String = "",
    val digitos: Int = 6,
    val periodo: Int = 30,
    val algoritmo: String = "HmacSHA1",
    val agregadoEn: Long = System.currentTimeMillis()
)
