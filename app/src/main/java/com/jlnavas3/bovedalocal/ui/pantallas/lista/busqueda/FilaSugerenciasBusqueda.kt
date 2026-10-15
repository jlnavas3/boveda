/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Fila horizontal deslizable que muestra los chips destacados para búsqueda rápida.
 */
@Composable
fun FilaSugerenciasBusqueda(
    textoBusqueda: String,
    sugerencias: List<ItemSugerenciaBusqueda>,
    alSeleccionarSugerencia: (ItemSugerenciaBusqueda) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sugerencias, key = { it.comando }) { item ->
            val activo = ManejadorInsercionFiltro.estaActivo(textoBusqueda, item.comando)
            ChipFiltroBusqueda(
                item = item,
                estaActivo = activo,
                alPulsar = { alSeleccionarSugerencia(item) }
            )
        }
    }
}
