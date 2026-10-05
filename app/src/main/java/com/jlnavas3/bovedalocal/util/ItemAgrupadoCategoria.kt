package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Representa los elementos de una lista agrupada por [Categoria],
 * utilizada cuando la jerarquía es Categoría sobre Identidad.
 */
sealed interface ItemAgrupadoCategoria {
    data class CabeceraCategoria(
        val categoria: Categoria,
        val totalEntradas: Int
    ) : ItemAgrupadoCategoria

    data class CabeceraSinCategoria(
        val totalEntradas: Int
    ) : ItemAgrupadoCategoria

    data class EntradaHija(
        val entrada: Entrada,
        val categoria: Categoria?,
        val esUltimaEnSeccion: Boolean
    ) : ItemAgrupadoCategoria
}
