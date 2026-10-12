/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraInferiorSeleccion
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Barra contextual inferior para acciones por lote cuando hay elementos seleccionados.
 */
@Composable
fun BarraAccionesSeleccionTipo(
    seleccionados: Set<String>,
    todasLasEntradas: List<Entrada>,
    haptica: Haptica,
    alAlternarFavoritos: (Set<String>) -> Unit,
    alComparar: (String, List<String>) -> Unit,
    alRespaldar: (String) -> Unit,
    alTransferirCxf: (List<Entrada>) -> Unit,
    alAbrirRenombrar: (String) -> Unit,
    alAbrirBorrado: () -> Unit
) {
    val itemsSeleccionados = remember(todasLasEntradas, seleccionados) {
        todasLasEntradas.filter { seleccionados.contains(it.id) }
    }
    val todosSonFavoritos = remember(itemsSeleccionados) {
        itemsSeleccionados.isNotEmpty() && itemsSeleccionados.all { it.favorito }
    }

    BarraInferiorSeleccion(
        cantidad = seleccionados.size,
        todosSonFavoritos = todosSonFavoritos,
        alAlternarFavoritos = {
            haptica.exito()
            alAlternarFavoritos(seleccionados)
        },
        alComparar = {
            haptica.toque()
            val primera = seleccionados.first()
            alComparar(primera, seleccionados.toList())
        },
        alRespaldar = {
            haptica.tic()
            val idsParam = seleccionados.joinToString(",")
            alRespaldar(idsParam)
        },
        alTransferirCxf = {
            haptica.tic()
            val copia = itemsSeleccionados.toList()
            alTransferirCxf(copia)
        },
        alRenombrar = {
            haptica.tic()
            val primerSeleccionado = todasLasEntradas.find { it.id == seleccionados.firstOrNull() }
            val nuevoTitulo = primerSeleccionado?.titulo ?: ""
            alAbrirRenombrar(nuevoTitulo)
        },
        alBorrar = {
            haptica.error()
            alAbrirBorrado()
        }
    )
}
