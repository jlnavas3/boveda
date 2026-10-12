/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable
import java.util.UUID

/** Registro en el historial temporal de contraseñas generadas recientemente. */
@Serializable
data class RegistroClaveGenerada(
    val id: String = UUID.randomUUID().toString(),
    val clave: String,
    val generadaEn: Long = System.currentTimeMillis(),
    val origen: String = "Generador Rápido"
)
