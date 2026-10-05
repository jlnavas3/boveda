package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad

/**
 * Calcula las identidades disponibles y sus conteos cuando las Categorias
 * actuan como nivel superior (Categoria -> Identidades).
 */
fun filtrarIdentidadesPorCategoria(
    identidades: List<Identidad>,
    entradas: List<Entrada>,
    categoriaSeleccionadaId: String?
): Triple<List<Identidad>, Map<String, Int>, Int> {
    // Si no hay filtro de categoria seleccionado ("Todas"), conteos sobre todas las entradas
    if (categoriaSeleccionadaId == null) {
        val conteos = identidades.associate { iden ->
            iden.id to entradas.count { entrada ->
                resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
            }
        }
        val sinIdentidad = entradas.count { entrada ->
            resolverIdentidadParaEntrada(entrada, identidades) == null
        }
        return Triple(identidades, conteos, sinIdentidad)
    }

    // Filtrar entradas que pertenecen a la categoria seleccionada
    val entradasCategoria = entradas.filter { it.categorias.contains(categoriaSeleccionadaId) }

    val conteos = identidades.associate { iden ->
        iden.id to entradasCategoria.count { entrada ->
            resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
        }
    }
    val sinIdentidad = entradasCategoria.count { entrada ->
        resolverIdentidadParaEntrada(entrada, identidades) == null
    }

    // Filtrar para mostrar solo identidades que tienen entradas dentro de esta categoria
    val identidadesFiltradas = identidades.filter { iden ->
        (conteos[iden.id] ?: 0) > 0
    }

    return Triple(identidadesFiltradas, conteos, sinIdentidad)
}
