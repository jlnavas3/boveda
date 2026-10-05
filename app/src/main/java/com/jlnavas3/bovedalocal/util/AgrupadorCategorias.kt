package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Agrupa las credenciales visibles según sus [Categoria]s,
 * generando cabeceras de sección y entradas hijas ordenadas.
 */
fun construirItemsAgrupadosPorCategoria(
    entradas: List<Entrada>,
    categorias: List<Categoria>,
    expandido: (String) -> Boolean
): List<ItemAgrupadoCategoria> {
    if (categorias.isEmpty()) {
        return entradas.mapIndexed { index, entrada ->
            ItemAgrupadoCategoria.EntradaHija(entrada, null, index == entradas.lastIndex)
        }
    }

    val resultado = mutableListOf<ItemAgrupadoCategoria>()

    val mapaPorCategoria = categorias.associateWith { col ->
        entradas.filter { entrada -> entrada.categorias.contains(col.id) }
    }

    val sinCategoria = entradas.filter { entrada -> entrada.categorias.isEmpty() }

    categorias.forEach { col ->
        val listaDeCol = mapaPorCategoria[col] ?: emptyList()
        if (listaDeCol.isNotEmpty()) {
            resultado.add(ItemAgrupadoCategoria.CabeceraCategoria(col, listaDeCol.size))
            if (expandido(col.id)) {
                listaDeCol.forEachIndexed { idx, entrada ->
                    resultado.add(
                        ItemAgrupadoCategoria.EntradaHija(
                            entrada = entrada,
                            categoria = col,
                            esUltimaEnSeccion = idx == listaDeCol.lastIndex
                        )
                    )
                }
            }
        }
    }

    if (sinCategoria.isNotEmpty()) {
        resultado.add(ItemAgrupadoCategoria.CabeceraSinCategoria(sinCategoria.size))
        if (expandido("__SIN_CATEGORIA__")) {
            sinCategoria.forEachIndexed { idx, entrada ->
                resultado.add(
                    ItemAgrupadoCategoria.EntradaHija(
                        entrada = entrada,
                        categoria = null,
                        esUltimaEnSeccion = idx == sinCategoria.lastIndex
                    )
                )
            }
        }
    }

    return resultado
}
