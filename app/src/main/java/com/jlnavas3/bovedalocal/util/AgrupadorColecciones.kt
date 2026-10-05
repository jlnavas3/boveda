package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Agrupa las credenciales visibles según sus [Coleccion]es,
 * generando cabeceras de sección y entradas hijas ordenadas.
 */
fun construirItemsAgrupadosPorColeccion(
    entradas: List<Entrada>,
    colecciones: List<Coleccion>,
    expandido: (String) -> Boolean
): List<ItemAgrupadoColeccion> {
    if (colecciones.isEmpty()) {
        return entradas.mapIndexed { index, entrada ->
            ItemAgrupadoColeccion.EntradaHija(entrada, null, index == entradas.lastIndex)
        }
    }

    val resultado = mutableListOf<ItemAgrupadoColeccion>()

    val mapaPorColeccion = colecciones.associateWith { col ->
        entradas.filter { entrada -> entrada.colecciones.contains(col.id) }
    }

    val sinColeccion = entradas.filter { entrada -> entrada.colecciones.isEmpty() }

    colecciones.forEach { col ->
        val listaDeCol = mapaPorColeccion[col] ?: emptyList()
        if (listaDeCol.isNotEmpty()) {
            resultado.add(ItemAgrupadoColeccion.CabeceraColeccion(col, listaDeCol.size))
            if (expandido(col.id)) {
                listaDeCol.forEachIndexed { idx, entrada ->
                    resultado.add(
                        ItemAgrupadoColeccion.EntradaHija(
                            entrada = entrada,
                            coleccion = col,
                            esUltimaEnSeccion = idx == listaDeCol.lastIndex
                        )
                    )
                }
            }
        }
    }

    if (sinColeccion.isNotEmpty()) {
        resultado.add(ItemAgrupadoColeccion.CabeceraSinColeccion(sinColeccion.size))
        if (expandido("__SIN_COLECCION__")) {
            sinColeccion.forEachIndexed { idx, entrada ->
                resultado.add(
                    ItemAgrupadoColeccion.EntradaHija(
                        entrada = entrada,
                        coleccion = null,
                        esUltimaEnSeccion = idx == sinColeccion.lastIndex
                    )
                )
            }
        }
    }

    return resultado
}
