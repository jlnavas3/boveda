package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion

sealed interface ItemAgrupado {
    data class Suelto(val entrada: Entrada) : ItemAgrupado
    data class Grupo(val clave: String, val entradas: List<Entrada>) : ItemAgrupado
    data class Hijo(val entrada: Entrada) : ItemAgrupado
}

internal fun ordenarEntradasInternas(entradas: List<Entrada>, criterio: CriterioOrdenacion): List<Entrada> {
    val comp: Comparator<Entrada> = when (criterio) {
        CriterioOrdenacion.NOMBRE_AZ -> compareByDescending<Entrada> { it.favorito }.thenBy { it.titulo.lowercase() }
        CriterioOrdenacion.NOMBRE_ZA -> compareByDescending<Entrada> { it.titulo.lowercase() }
        CriterioOrdenacion.MODIFICACION_RECIENTE -> compareByDescending<Entrada> { it.modificadaEn }
        CriterioOrdenacion.CREACION_RECIENTE -> compareByDescending<Entrada> { it.creadaEn }
        CriterioOrdenacion.ANTIGUEDAD -> compareBy<Entrada> { it.creadaEn }
        CriterioOrdenacion.USO_RECIENTE -> compareByDescending<Entrada> { it.ultimoUsoEn }.thenBy { it.titulo.lowercase() }
        CriterioOrdenacion.IGNORADAS -> compareByDescending<Entrada> { it.ignoradaEnSalud }.thenByDescending { it.favorito }.thenBy { it.titulo.lowercase() }
    }
    return entradas.sortedWith(comp)
}
