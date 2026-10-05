package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad

/**
 * Agrupa las credenciales visibles según sus perfiles de [Identidad],
 * generando cabeceras de sección y entradas hijas ordenadas.
 */
fun construirItemsAgrupadosPorIdentidad(
    entradas: List<Entrada>,
    identidades: List<Identidad>,
    expandido: (String) -> Boolean
): List<ItemAgrupadoIdentidad> {
    if (identidades.isEmpty()) {
        return entradas.mapIndexed { index, entrada ->
            ItemAgrupadoIdentidad.EntradaHija(entrada, null, index == entradas.lastIndex)
        }
    }

    val resultado = mutableListOf<ItemAgrupadoIdentidad>()

    // Entradas clasificadas por identidad
    val mapaPorIdentidad = identidades.associateWith { iden ->
        entradas.filter { entrada ->
            resolverIdentidadParaEntrada(entrada, identidades)?.id == iden.id
        }
    }

    // Entradas sin identidad asignada o detectada
    val sinIdentidad = entradas.filter { entrada ->
        resolverIdentidadParaEntrada(entrada, identidades) == null
    }

    identidades.forEach { iden ->
        val listaDeIden = mapaPorIdentidad[iden] ?: emptyList()
        if (listaDeIden.isNotEmpty()) {
            resultado.add(ItemAgrupadoIdentidad.CabeceraIdentidad(iden, listaDeIden.size))
            if (expandido(iden.id)) {
                listaDeIden.forEachIndexed { idx, entrada ->
                    resultado.add(
                        ItemAgrupadoIdentidad.EntradaHija(
                            entrada = entrada,
                            identidad = iden,
                            esUltimaEnSeccion = idx == listaDeIden.lastIndex
                        )
                    )
                }
            }
        }
    }

    if (sinIdentidad.isNotEmpty()) {
        resultado.add(ItemAgrupadoIdentidad.CabeceraSinIdentidad(sinIdentidad.size))
        if (expandido("__SIN_IDENTIDAD__")) {
            sinIdentidad.forEachIndexed { idx, entrada ->
                resultado.add(
                    ItemAgrupadoIdentidad.EntradaHija(
                        entrada = entrada,
                        identidad = null,
                        esUltimaEnSeccion = idx == sinIdentidad.lastIndex
                    )
                )
            }
        }
    }

    return resultado
}
