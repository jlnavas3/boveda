package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad

/**
 * Calcula las colecciones disponibles y sus conteos cuando las Identidades
 * actuan como nivel superior (Identidad -> Colecciones).
 */
fun filtrarColeccionesPorIdentidad(
    colecciones: List<Coleccion>,
    entradas: List<Entrada>,
    identidades: List<Identidad>,
    identidadSeleccionadaId: String?
): Pair<List<Coleccion>, Map<String, Int>> {
    // Si no hay filtro de identidad seleccionado ("Todas"), mostramos todas las colecciones con conteo global
    if (identidadSeleccionadaId == null) {
        val conteos = colecciones.associate { col ->
            col.id to entradas.count { it.colecciones.contains(col.id) }
        }
        return Pair(colecciones, conteos)
    }

    // Filtrar entradas que pertenecen a la identidad seleccionada
    val entradasIdentidad = if (identidadSeleccionadaId == "__SIN_IDENTIDAD__") {
        entradas.filter { entrada ->
            resolverIdentidadParaEntrada(entrada, identidades) == null
        }
    } else {
        entradas.filter { entrada ->
            resolverIdentidadParaEntrada(entrada, identidades)?.id == identidadSeleccionadaId
        }
    }

    // Calcular conteos únicamente dentro de las entradas de esa identidad
    val conteos = colecciones.associate { col ->
        col.id to entradasIdentidad.count { it.colecciones.contains(col.id) }
    }

    // Mostrar sólo las colecciones que contengan al menos una credencial con esta identidad
    val coleccionesFiltradas = colecciones.filter { col ->
        (conteos[col.id] ?: 0) > 0
    }

    return Pair(coleccionesFiltradas, conteos)
}
