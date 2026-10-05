package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Representa los elementos de una lista agrupada por [Coleccion],
 * utilizada cuando la jerarquía es Colección sobre Identidad.
 */
sealed interface ItemAgrupadoColeccion {
    data class CabeceraColeccion(
        val coleccion: Coleccion,
        val totalEntradas: Int
    ) : ItemAgrupadoColeccion

    data class CabeceraSinColeccion(
        val totalEntradas: Int
    ) : ItemAgrupadoColeccion

    data class EntradaHija(
        val entrada: Entrada,
        val coleccion: Coleccion?,
        val esUltimaEnSeccion: Boolean
    ) : ItemAgrupadoColeccion
}
