/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad

/**
 * Representa los elementos de una lista agrupada por perfiles de [Identidad].
 */
sealed interface ItemAgrupadoIdentidad {
    data class CabeceraIdentidad(
        val identidad: Identidad,
        val totalEntradas: Int
    ) : ItemAgrupadoIdentidad

    data class CabeceraSinIdentidad(
        val totalEntradas: Int
    ) : ItemAgrupadoIdentidad

    data class EntradaHija(
        val entrada: Entrada,
        val identidad: Identidad?,
        val esUltimaEnSeccion: Boolean
    ) : ItemAgrupadoIdentidad
}
