package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad

/**
 * Calcula las identidades disponibles y sus conteos cuando las Colecciones
 * actuan como nivel superior (Coleccion -> Identidades).
 */
fun filtrarIdentidadesPorColeccion(
    identidades: List<Identidad>,
    entradas: List<Entrada>,
    coleccionSeleccionadaId: String?
): Triple<List<Identidad>, Map<String, Int>, Int> {
    // Si no hay filtro de coleccion seleccionado ("Todas"), conteos sobre todas las entradas
    if (coleccionSeleccionadaId == null) {
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

    // Filtrar entradas que pertenecen a la coleccion seleccionada
    val entradasColeccion = entradas.filter { it.colecciones.contains(coleccionSeleccionadaId) }

    val conteos = identidades.associate { iden ->
        iden.id to entradasColeccion.count { entrada ->
            resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
        }
    }
    val sinIdentidad = entradasColeccion.count { entrada ->
        resolverIdentidadParaEntrada(entrada, identidades) == null
    }

    // Filtrar para mostrar solo identidades que tienen entradas dentro de esta coleccion
    val identidadesFiltradas = identidades.filter { iden ->
        (conteos[iden.id] ?: 0) > 0
    }

    return Triple(identidadesFiltradas, conteos, sinIdentidad)
}
