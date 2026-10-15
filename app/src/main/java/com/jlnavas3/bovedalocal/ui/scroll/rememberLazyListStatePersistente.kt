/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.scroll

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Composable desacoplado que crea y vincula un [LazyListState] con un [GestorPosicionScroll]
 * bajo una [clave] única. Al salir de la pantalla y regresar, la lista mantendrá exactamente
 * su posición original de scroll (índice y offset).
 */
@Composable
fun rememberLazyListStatePersistente(
    clave: String,
    gestor: GestorPosicionScroll?
): LazyListState {
    if (gestor == null) {
        return rememberLazyListState()
    }
    val (indiceInicial, offsetInicial) = remember(clave) {
        gestor.obtenerPosicion(clave)
    }

    val estado = rememberLazyListState(
        initialFirstVisibleItemIndex = indiceInicial,
        initialFirstVisibleItemScrollOffset = offsetInicial
    )

    // Guardar activamente la posición cuando el usuario hace scroll
    LaunchedEffect(estado, clave) {
        snapshotFlow {
            Triple(
                estado.firstVisibleItemIndex,
                estado.firstVisibleItemScrollOffset,
                estado.isScrollInProgress
            )
        }
            .distinctUntilChanged()
            .collect { (indice, offset, scrolleando) ->
                if (scrolleando || (estado.layoutInfo.totalItemsCount > 0 && (indice > 0 || offset > 0))) {
                    gestor.guardarPosicion(clave, indice, offset)
                } else if (estado.layoutInfo.totalItemsCount > 0 && indice == 0 && offset == 0 && scrolleando) {
                    gestor.guardarPosicion(clave, 0, 0)
                }
            }
    }

    // Asegurar restauración si los elementos de la lista terminan de computarse tras el primer pase
    LaunchedEffect(clave, estado.layoutInfo.totalItemsCount) {
        if (estado.layoutInfo.totalItemsCount > 0 && indiceInicial > 0 && estado.firstVisibleItemIndex == 0) {
            val indiceSeguro = indiceInicial.coerceAtMost(estado.layoutInfo.totalItemsCount - 1)
            estado.scrollToItem(indiceSeguro, offsetInicial)
        }
    }

    return estado
}
