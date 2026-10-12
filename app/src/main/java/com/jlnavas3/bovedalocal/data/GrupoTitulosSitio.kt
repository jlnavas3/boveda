/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

data class GrupoTitulosSitio(
    val id: String = java.util.UUID.randomUUID().toString(),
    val dominioClave: String,
    val nombreSugerido: String,
    val nombrePersonalizado: String = nombreSugerido,
    val entradas: List<Entrada> = emptyList(),
    val tieneColision: Boolean = entradas.size > 1
) {
    val nombreEfectivo: String
        get() = nombrePersonalizado.ifBlank { nombreSugerido }
}
