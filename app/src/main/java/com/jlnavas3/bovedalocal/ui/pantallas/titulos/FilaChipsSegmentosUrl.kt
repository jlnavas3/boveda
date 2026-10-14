/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilaChipsSegmentosUrl(
    segmentos: List<String>,
    nombreSeleccionado: String,
    prefijosEnReglas: List<String>,
    alSeleccionarSegmento: (seleccionado: String, descartados: List<String>) -> Unit,
    alAlternarRegla: (prefijo: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (segmentos.size < 2) return

    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        segmentos.forEach { segmento ->
            val seleccionado = segmento.equals(nombreSeleccionado.trim(), ignoreCase = true)
            val esRegla = !seleccionado && prefijosEnReglas.any { it.equals(segmento.trim(), ignoreCase = true) }

            ChipSegmentoUrl(
                texto = segmento,
                seleccionado = seleccionado,
                esRegla = esRegla,
                alSeleccionar = {
                    val descartados = segmentos.filterNot { it.equals(segmento, ignoreCase = true) }
                    alSeleccionarSegmento(segmento, descartados)
                },
                alAlternarRegla = {
                    if (!seleccionado) {
                        alAlternarRegla(segmento)
                    }
                }
            )
        }
    }
}
