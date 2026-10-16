/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Modelo de datos inmutable para un archivo adjunto seguro dentro de una entrada de la bóveda.
 * Se cifra junto con toda la estructura de la entrada en el archivo .bvda / .cxf sin tocar ninguna red.
 */
@Immutable
@Serializable
data class AdjuntoBoveda(
    val id: String,
    val nombre: String,
    val mimeType: String = "application/octet-stream",
    val tamanoBytes: Long = 0L,
    val contenidoBase64: String = "",
    val fechaSubida: Long = 0L
)
