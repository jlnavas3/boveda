package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad

/**
 * Calcula las categorias disponibles y sus conteos cuando las Identidades
 * actuan como nivel superior (Identidad -> Categorias).
 */
fun filtrarCategoriasPorIdentidad(
    categorias: List<Categoria>,
    entradas: List<Entrada>,
    identidades: List<Identidad>,
    identidadSeleccionadaId: String?
): Pair<List<Categoria>, Map<String, Int>> {
    // Si no hay filtro de identidad seleccionado ("Todas"), mostramos todas las categorias con conteo global
    if (identidadSeleccionadaId == null) {
        val conteos = categorias.associate { col ->
            col.id to entradas.count { it.categorias.contains(col.id) }
        }
        return Pair(categorias, conteos)
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

    // Calcular conteos unicamente dentro de las entradas de esa identidad
    val conteos = categorias.associate { col ->
        col.id to entradasIdentidad.count { it.categorias.contains(col.id) }
    }

    // Mostrar solo las categorias que contengan al menos una credencial con esta identidad
    val categoriasFiltradas = categorias.filter { col ->
        (conteos[col.id] ?: 0) > 0
    }

    return Pair(categoriasFiltradas, conteos)
}
