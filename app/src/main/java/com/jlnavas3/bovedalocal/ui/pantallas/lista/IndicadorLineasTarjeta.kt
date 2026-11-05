/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.AlturaIndicadores
import com.jlnavas3.bovedalocal.ui.theme.OpacidadIndicadores

/**
 * Renderiza la barra horizontal segmentada en el borde superior de la tarjeta.
 */
@Composable
fun IndicadorLineasTarjeta(
    segmentos: List<SegmentoIndicadorDato>,
    modifier: Modifier = Modifier,
    altura: Dp = AlturaIndicadores,
    opacidad: Float = OpacidadIndicadores
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(altura)
    ) {
        val espaciadoPx = 2.dp.toPx()
        val maxCornerRadiusPx = if (size.height > 8.dp.toPx()) 2.dp.toPx() else 1.dp.toPx()
        val cornerRadiusPx = (size.height / 2f).coerceAtMost(maxCornerRadiusPx)
        val numSegmentos = segmentos.size
        val anchoSegmento = (size.width - (espaciadoPx * (numSegmentos - 1))) / numSegmentos
        val cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)

        var xOffset = 0f
        val alfaEfectivo = opacidad.coerceIn(0.01f, 1f)
        for (seg in segmentos) {
            if (seg.activo) {
                drawRoundRect(
                    color = seg.color.copy(alpha = alfaEfectivo),
                    topLeft = Offset(xOffset, 0f),
                    size = Size(anchoSegmento, size.height),
                    cornerRadius = cornerRadius
                )
            }
            xOffset += anchoSegmento + espaciadoPx
        }
    }
}
