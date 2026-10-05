package com.jlnavas3.bovedalocal.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Representa los elementos de una lista agrupada con jerarquía de 2 niveles plegables:
 * Nivel 1 (Principal), Nivel 2 (Subsección subordinada) y Entradas individuales.
 */
sealed class ItemAgrupadoJerarquico {
    /** Cabecera de sección principal (Nivel 1) */
    data class CabeceraPrincipal(
        val claveGrupo: String,
        val titulo: String,
        val totalEntradas: Int,
        val color: Color,
        val icono: ImageVector,
        val expandido: Boolean,
        val idsEntradas: Set<String>
    ) : ItemAgrupadoJerarquico()

    /** Subcabecera subordinada (Nivel 2) */
    data class Subcabecera(
        val claveGrupo: String,
        val titulo: String,
        val totalEntradas: Int,
        val color: Color,
        val icono: ImageVector,
        val expandido: Boolean,
        val idsEntradas: Set<String>
    ) : ItemAgrupadoJerarquico()

    /** Entrada hoja perteneciente a una subsección */
    data class EntradaHoja(
        val entrada: Entrada,
        val esUltimaEnSubseccion: Boolean,
        val clavePadre: String = ""
    ) : ItemAgrupadoJerarquico()
}
