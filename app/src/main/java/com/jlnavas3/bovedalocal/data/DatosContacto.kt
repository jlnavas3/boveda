/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Modelo de datos inmutable para contactos seguros, direcciones de envío y facturación física.
 */
@Immutable
@Serializable
data class DatosContacto(
    val telefonoSecundario: String = "",
    val correo: String = "",
    val direccion: String = "",
    val codigoPostal: String = "",
    val ciudad: String = "",
    val provincia: String = "",
    val pais: String = ""
) {
    val tieneDireccion: Boolean
        get() = direccion.isNotBlank() || ciudad.isNotBlank() || codigoPostal.isNotBlank() || pais.isNotBlank()

    val direccionFormateada: String
        get() = listOf(direccion, ciudad, provincia, codigoPostal, pais)
            .filter { it.isNotBlank() }
            .joinToString(", ")
}
