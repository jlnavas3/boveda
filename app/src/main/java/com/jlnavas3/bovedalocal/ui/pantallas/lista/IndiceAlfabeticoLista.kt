package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
import com.jlnavas3.bovedalocal.ui.componentes.encontrarIndiceParaLetra
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import kotlinx.coroutines.launch

/**
 * Microcomponente que gestiona el índice alfabético lateral flotante para el listado principal,
 * incluyendo el cálculo de crestas, offset de scroll y feedback háptico.
 */
@Composable
fun IndiceAlfabeticoLista(
    ajustes: AjustesApp,
    itemsAMostrar: List<ItemAgrupado>,
    densidadAltura: Dp,
    espaciadoFilas: Dp,
    estadoLista: LazyListState,
    alCambiarLetraActiva: (Char?) -> Unit,
    modifier: Modifier = Modifier
) {
    val ambitoCorutina = rememberCoroutineScope()
    val densidad = LocalDensity.current

    IndiceAlfabetico(
        alSeleccionarLetra = { letra ->
            if (!ajustes.indiceAlinearConCresta) {
                val indice = encontrarIndiceParaLetra(itemsAMostrar, letra, ajustes.indiceIncluirEnie)
                if (indice != null && indice in itemsAMostrar.indices) {
                    ambitoCorutina.launch {
                        estadoLista.scrollToItem(indice, 0)
                    }
                }
            }
        },
        alSeleccionarLetraConOffset = { letra, touchY ->
            val indice = encontrarIndiceParaLetra(itemsAMostrar, letra, ajustes.indiceIncluirEnie)
            if (indice != null && indice in itemsAMostrar.indices) {
                ambitoCorutina.launch {
                    if (!ajustes.indiceAlinearConCresta) {
                        estadoLista.scrollToItem(indice, 0)
                    } else {
                        val alturaFilaPx = with(densidad) { densidadAltura.toPx() }
                        val espaciadoFilasPx = with(densidad) { espaciadoFilas.toPx() }
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
