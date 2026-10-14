/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.progreso

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta

/**
 * Microcomponente desacoplado que renderiza una barra de desplazamiento vertical sutil
 * sincronizada reactivamente con un [LazyListState].
 */
@Composable
fun BarraDesplazamientoVertical(
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    val layoutInfo = state.layoutInfo
    val totalItems = layoutInfo.totalItemsCount
    val visibleItems = layoutInfo.visibleItemsInfo.size

    if (totalItems <= visibleItems || totalItems == 0) return

    BoxWithConstraints(modifier = modifier) {
        val alturaTotalPx = constraints.maxHeight.toFloat()
        if (alturaTotalPx <= 0f) return@BoxWithConstraints

        val firstVisible = state.firstVisibleItemIndex
        val proporcionVisible = (visibleItems.toFloat() / totalItems).coerceIn(0.18f, 0.85f)
        val alturaThumbPx = (alturaTotalPx * proporcionVisible).coerceAtLeast(16f)

        val fraccionScroll = (firstVisible.toFloat() / (totalItems - visibleItems).coerceAtLeast(1)).coerceIn(0f, 1f)
        val offsetThumbPx = fraccionScroll * (alturaTotalPx - alturaThumbPx)

        val densidad = LocalDensity.current
        val offsetDp = with(densidad) { offsetThumbPx.toDp() }
        val alturaThumbDp = with(densidad) { alturaThumbPx.toDp() }

        val formaBarra = if (com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas == 0.dp) androidx.compose.foundation.shape.RoundedCornerShape(0.dp) else CircleShape

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(3.5.dp)
                .clip(formaBarra)
                .background(SuperficieAlta.copy(alpha = 0.6f))
        ) {
            Box(
                modifier = Modifier
                    .offset(y = offsetDp)
                    .fillMaxWidth()
                    .height(alturaThumbDp)
                    .clip(formaBarra)
                    .background(ColorAcento)
            )
        }
    }
}
