/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.AlturaIndicadores
import com.jlnavas3.bovedalocal.ui.theme.OpacidadIndicadores

/**
 * Renderiza micro-íconos temáticos coloreados (usuario, llave, totp, passkey, web, app) en la esquina inferior derecha.
 */
@Composable
fun IndicadorIconosTarjeta(
    segmentos: List<SegmentoIndicadorDato>,
    modifier: Modifier = Modifier,
    tamanoIcono: Dp = AlturaIndicadores,
    opacidad: Float = OpacidadIndicadores
) {
    val activos = segmentos.filter { it.activo }
    if (activos.isEmpty()) return

    val tamanoEfectivo = tamanoIcono.coerceIn(8.dp, 16.dp)
    val alfaEfectivo = opacidad.coerceIn(0.01f, 1f)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (seg in activos) {
            Icon(
                imageVector = seg.icono,
                contentDescription = seg.etiqueta,
                tint = seg.color.copy(alpha = alfaEfectivo),
                modifier = Modifier.size(tamanoEfectivo)
            )
        }
    }
}
