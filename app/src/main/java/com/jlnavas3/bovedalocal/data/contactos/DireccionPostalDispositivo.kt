/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data.contactos

import androidx.compose.runtime.Immutable

/**
 * Modelo inmutable para representar una dirección postal física leída de la agenda de Android.
 */
@Immutable
data class DireccionPostalDispositivo(
    val calle: String = "",
    val ciudad: String = "",
    val region: String = "",
    val codigoPostal: String = "",
    val pais: String = "",
    val etiqueta: String = ""
) {
    val direccionCompleta: String
        get() = listOf(calle, ciudad, region, codigoPostal, pais)
            .filter { it.isNotBlank() }
            .joinToString(", ")
}
