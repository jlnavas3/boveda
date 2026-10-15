/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable

/**
 * Micro-modelo que encapsula la información de inspección preliminar de un archivo de respaldo (Dry-Run),
 * permitiendo al usuario verificar su contenido antes de aplicar la restauración en la bóveda.
 */
@Immutable
data class InfoRespaldoPrevisualizado(
    val totalEntradas: Int,
    val totalCategorias: Int,
    val totalIdentidades: Int,
    val fechaMasReciente: Long
)
