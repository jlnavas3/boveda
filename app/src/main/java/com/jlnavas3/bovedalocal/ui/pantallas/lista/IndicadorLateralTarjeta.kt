/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
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
 * Renderiza una franja vertical segmentada en el margen izquierdo de la tarjeta.
 */
@Composable
fun IndicadorLateralTarjeta(
    segmentos: List<SegmentoIndicadorDato>,
    modifier: Modifier = Modifier,
    ancho: Dp = AlturaIndicadores,
    opacidad: Float = OpacidadIndicadores
) {
    val activos = segmentos.filter { it.activo }
    if (activos.isEmpty()) return

    val anchoEfectivo = ancho.coerceIn(2.dp, 8.dp)

    Canvas(
        modifier = modifier
            .width(anchoEfectivo)
            .fillMaxHeight()
    ) {
        val espaciadoPx = 2.dp.toPx()
        val numSegmentos = activos.size
        val altoSegmento = (size.height - (espaciadoPx * (numSegmentos - 1))) / numSegmentos
        val cornerRadius = CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx())

        var yOffset = 0f
        val alfaEfectivo = opacidad.coerceIn(0.01f, 1f)
        for (seg in activos) {
            drawRoundRect(
                color = seg.color.copy(alpha = alfaEfectivo),
                topLeft = Offset(0f, yOffset),
                size = Size(size.width, altoSegmento),
                cornerRadius = cornerRadius
            )
            yOffset += altoSegmento + espaciadoPx
        }
    }
}
