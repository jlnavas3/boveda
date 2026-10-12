/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.cxf

import com.jlnavas3.bovedalocal.data.Entrada

data class ResultadoConversionCxf(
    val entradas: List<Entrada>,
    val totalPasskeys: Int,
    val totalContrasenas: Int,
    val totalTotp: Int,
    val exportador: String? = null
)
