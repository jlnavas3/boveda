/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
import com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice
import com.jlnavas3.bovedalocal.ui.componentes.obtenerLetrasIndice
import kotlinx.coroutines.launch

/**
 * Microcomponente que gestiona el índice alfabético lateral flotante para la pantalla de exportación CXF.
 */
@Composable
fun IndiceAlfabeticoCxf(
    entradas: List<Entrada>,
    estadoLista: LazyListState,
    ajustes: AjustesApp = AjustesApp(),
    modifier: Modifier = Modifier
) {
    val ambitoCorutina = rememberCoroutineScope()

    IndiceAlfabetico(
        alSeleccionarLetra = { letra ->
            val indice = encontrarIndiceParaLetraCxf(entradas, letra, ajustes.indiceIncluirEnie)
            if (indice != null && indice in entradas.indices) {
                ambitoCorutina.launch {
                    estadoLista.scrollToItem(indice, 0)
                }
            }
        },
        alSeleccionarLetraConOffset = { letra, _ ->
            val indice = encontrarIndiceParaLetraCxf(entradas, letra, ajustes.indiceIncluirEnie)
            if (indice != null && indice in entradas.indices) {
                ambitoCorutina.launch {
                    estadoLista.scrollToItem(indice, 0)
                }
            }
        },
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

internal fun encontrarIndiceParaLetraCxf(items: List<Entrada>, letra: Char, incluirEnie: Boolean = true): Int? {
    if (items.isEmpty()) return null
    val listaLetras = obtenerLetrasIndice(incluirEnie)

    if (letra == '#') {
        val exacto = items.indexOfFirst { letraInicialIndice(it.titulo, incluirEnie) == '#' }
        return if (exacto >= 0) exacto else 0
    }

    val idxExacto = items.indexOfFirst { letraInicialIndice(it.titulo, incluirEnie) == letra }
    if (idxExacto >= 0) return idxExacto

    val posLetraBuscada = listaLetras.indexOf(letra)
    if (posLetraBuscada >= 0) {
        for (i in (posLetraBuscada + 1) until listaLetras.size) {
            val letraSiguiente = listaLetras[i]
            val idxSig = items.indexOfFirst { letraInicialIndice(it.titulo, incluirEnie) == letraSiguiente }
            if (idxSig >= 0) return idxSig
        }
    }

    return items.lastIndex
}
