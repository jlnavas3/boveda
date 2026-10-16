/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.salud

import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Estructura de datos inmutable que contiene el resultado consolidado del análisis de salud de la bóveda.
 */
data class DatosSaludBoveda(
    val claves: List<Entrada> = emptyList(),
    val ignoradas: List<Entrada> = emptyList(),
    val caducadas: List<Entrada> = emptyList(),
    val duplicadas: List<List<Entrada>> = emptyList(),
    val debiles: List<Entrada> = emptyList(),
    val muyComunes: List<Entrada> = emptyList(),
    val antiguas: List<Entrada> = emptyList(),
    val totalSobrantesDuplicadas: Int = 0,
    val pestanasConDatos: List<PestanaSalud> = emptyList(),
    val calculando: Boolean = false
)
