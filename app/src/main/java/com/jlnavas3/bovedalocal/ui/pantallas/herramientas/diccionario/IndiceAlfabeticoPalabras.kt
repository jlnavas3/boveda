/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
import kotlinx.coroutines.launch

/**
 * Microcomponente que gestiona el índice alfabético lateral flotante para el diccionario de palabras,
 * replicando exactamente la posición, curvatura de ola y configuración de la pantalla principal.
 */
@Composable
fun IndiceAlfabeticoPalabras(
    ajustes: AjustesApp,
    mapaIndices: Map<Char, Int>,
    estadoLista: LazyListState,
    alCambiarLetraActiva: (Char?) -> Unit,
    modifier: Modifier = Modifier
) {
    val ambitoCorutina = rememberCoroutineScope()
    val densidad = LocalDensity.current

    fun resolverIndice(letra: Char): Int? {
        if (mapaIndices.containsKey(letra)) return mapaIndices[letra]
        // Si no hay palabras que comiencen exactamente con esa letra, buscar la siguiente más cercana
        return mapaIndices.entries
            .filter { it.key >= letra }
            .minByOrNull { it.key }
            ?.value ?: mapaIndices.values.lastOrNull()
    }

    IndiceAlfabetico(
        alSeleccionarLetra = { letra ->
            if (!ajustes.indiceAlinearConCresta) {
                val indice = resolverIndice(letra)
                if (indice != null) {
                    ambitoCorutina.launch {
                        estadoLista.scrollToItem(indice, 0)
                    }
                }
            }
        },
        alSeleccionarLetraConOffset = { letra, touchY ->
            val indice = resolverIndice(letra)
            if (indice != null) {
                ambitoCorutina.launch {
                    if (!ajustes.indiceAlinearConCresta) {
                        estadoLista.scrollToItem(indice, 0)
                    } else {
                        val alturaFilaPx = with(densidad) { 48.dp.toPx() }
                        val espaciadoFilasPx = with(densidad) { 6.dp.toPx() }
                        val pasoItemPx = alturaFilaPx + espaciadoFilasPx
                        val touchYEnViewport = touchY + with(densidad) { 4.dp.toPx() }
                        val targetItemTop = touchYEnViewport - (alturaFilaPx / 2f)

                        if (targetItemTop <= 0f || pasoItemPx <= 0f) {
                            estadoLista.scrollToItem(indice, 0)
                        } else {
                            val numItems = (targetItemTop / pasoItemPx).toInt()
                            val resto = targetItemTop - (numItems * pasoItemPx)
                            if (resto <= 0.001f) {
                                val indiceTop = (indice - numItems).coerceAtLeast(0)
                                estadoLista.scrollToItem(indiceTop, 0)
                            } else {
                                val indiceTop = indice - numItems - 1
                                if (indiceTop >= 0) {
                                    val scrollOffset = (pasoItemPx - resto).toInt().coerceAtLeast(0)
                                    estadoLista.scrollToItem(indiceTop, scrollOffset)
                                } else {
                                    estadoLista.scrollToItem(0, 0)
                                }
                            }
                        }
                    }
                }
            }
        },
        alCambiarLetraActiva = alCambiarLetraActiva,
        incluirEnie = ajustes.indiceIncluirEnie,
        efectoOla = ajustes.indiceEfectoOla,
        amplitudOlaDp = ajustes.indiceAmplitudOlaDp,
        radioOlaDp = ajustes.indiceRadioOlaDp,
        escalaMaximaLetras = ajustes.indiceEscalaLetras,
        mostrarCirculo = ajustes.indiceMostrarCirculo,
        tamanoCirculoDp = ajustes.indiceTamanoCirculoDp,
        offsetCirculoDp = ajustes.indiceOffsetCirculoDp,
        hapticaActiva = ajustes.indiceHaptica,
        anchoZonaTactilDp = ajustes.indiceAnchoTactilDp,
        tonoLetras = ajustes.indiceTonoLetras,
        modifier = modifier
    )
}
