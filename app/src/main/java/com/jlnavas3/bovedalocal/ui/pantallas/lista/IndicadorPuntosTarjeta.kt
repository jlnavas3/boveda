/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.AlturaIndicadores
import com.jlnavas3.bovedalocal.ui.theme.OpacidadIndicadores

/**
 * Renderiza pequeños puntos esféricos discretos de color en la esquina inferior derecha de la tarjeta.
 */
@Composable
fun IndicadorPuntosTarjeta(
    segmentos: List<SegmentoIndicadorDato>,
    modifier: Modifier = Modifier,
    diametro: Dp = AlturaIndicadores,
    opacidad: Float = OpacidadIndicadores
) {
    val activos = segmentos.filter { it.activo }
    if (activos.isEmpty()) return

    val diametroEfectivo = diametro.coerceIn(3.dp, 10.dp)
    val alfaEfectivo = opacidad.coerceIn(0.01f, 1f)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (seg in activos) {
            Box(
                modifier = Modifier
                    .size(diametroEfectivo)
                    .clip(CircleShape)
                    .background(seg.color.copy(alpha = alfaEfectivo))
            )
        }
    }
}
