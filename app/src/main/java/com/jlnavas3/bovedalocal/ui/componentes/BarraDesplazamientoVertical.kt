/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Modificador para dibujar una barra de desplazamiento (scrollbar) vertical sutil
 * sobre cualquier LazyColumn cuando el número de elementos excede la ventana visible.
 */
fun Modifier.barraDesplazamientoVertical(
    state: LazyListState,
    color: Color = ColorAcento.copy(alpha = 0.45f),
    ancho: Dp = 3.dp,
    margenFin: Dp = 2.dp,
    margenVertical: Dp = 4.dp
): Modifier = this.drawWithContent {
    drawContent()

    val layoutInfo = state.layoutInfo
    val totalItems = layoutInfo.totalItemsCount
    val visibleItems = layoutInfo.visibleItemsInfo

    if (totalItems > 0 && visibleItems.isNotEmpty() && visibleItems.size < totalItems) {
        val altoTotalDisponible = size.height - (margenVertical.toPx() * 2)
        if (altoTotalDisponible > 0) {
            val fraccionVisible = (visibleItems.size.toFloat() / totalItems.toFloat()).coerceIn(0.1f, 1f)
            val altoBarra = (altoTotalDisponible * fraccionVisible).coerceAtLeast(20.dp.toPx())

            val primerIndice = state.firstVisibleItemIndex
            val offsetPixel = state.firstVisibleItemScrollOffset
            val tamanoPromedioElemento = (visibleItems.sumOf { it.size }.toFloat() / visibleItems.size).coerceAtLeast(1f)

            val progresoScroll = when {
                !state.canScrollBackward -> 0f
                !state.canScrollForward -> 1f
                else -> {
                    val maxDesplazable = (totalItems - visibleItems.size).coerceAtLeast(1).toFloat()
                    val avance = (primerIndice.toFloat() + (offsetPixel.toFloat() / tamanoPromedioElemento))
                    (avance / maxDesplazable).coerceIn(0f, 1f)
                }
            }

            val posicionY = margenVertical.toPx() + (progresoScroll * (altoTotalDisponible - altoBarra))
            val posicionX = size.width - ancho.toPx() - margenFin.toPx()

            drawRoundRect(
                color = color,
                topLeft = Offset(posicionX, posicionY),
                size = Size(ancho.toPx(), altoBarra),
                cornerRadius = CornerRadius(ancho.toPx() / 2, ancho.toPx() / 2)
            )
        }
    }
}
