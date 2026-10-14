/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.progreso

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.ItemProgresoEntrada

/**
 * Microcomponente que muestra 3 entradas a la vez con scroll vertical suave,
 * barra lateral de desplazamiento y persistencia de todo el historial.
 */
@Composable
fun ListaProgresoEntradasActivas(
    items: List<ItemProgresoEntrada>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    val listState = rememberLazyListState()

    // Auto-scroll al final con cada nuevo elemento mientras el usuario no esté manipulando el scroll
    LaunchedEffect(items.size) {
        if (items.isNotEmpty() && !listState.isScrollInProgress) {
            listState.scrollToItem(items.size - 1)
        }
    }

    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 480.dp)
    ) {
        val maxAltura = maxHeight
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxAltura)
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(max = maxAltura)
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { item ->
                    FilaProgresoEntradaItem(item = item)
                }
            }

            // Barra de desplazamiento visible a la derecha
            BarraDesplazamientoVertical(
                state = listState,
                modifier = Modifier
                    .height(maxAltura)
                    .padding(vertical = 2.dp)
            )
        }
    }
}
