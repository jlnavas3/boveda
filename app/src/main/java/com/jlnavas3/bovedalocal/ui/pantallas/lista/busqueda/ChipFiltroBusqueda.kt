/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Chip visual individual para un modificador de búsqueda rápida.
 */
@Composable
fun ChipFiltroBusqueda(
    item: ItemSugerenciaBusqueda,
    estaActivo: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondoColor by animateColorAsState(
        targetValue = if (estaActivo) ColorAcento.copy(alpha = 0.22f) else ColorTarjetaAjustes,
        label = "fondoChip"
    )
    val bordeColor by animateColorAsState(
        targetValue = if (estaActivo) ColorAcento else ColorBordeActual.copy(alpha = 0.5f),
        label = "bordeChip"
    )
    val iconoColor by animateColorAsState(
        targetValue = if (estaActivo) ColorAcento else TextoSecundario,
        label = "iconoChip"
    )
    val textoColor by animateColorAsState(
        targetValue = if (estaActivo) TextoPrincipal else TextoSecundario,
        label = "textoChip"
    )

    val forma = RoundedCornerShape(CurvaturaEsquinas / 2)

    Row(
        modifier = modifier
            .clip(forma)
            .background(fondoColor)
            .border(0.75.dp, bordeColor, forma)
            .clickable(onClick = alPulsar)
            .padding(horizontal = 6.dp, vertical = 2.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icono,
            contentDescription = null,
            tint = iconoColor,
            modifier = Modifier.size(11.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = item.etiqueta,
            color = textoColor,
            fontSize = 10.5.sp,
            fontWeight = if (estaActivo) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
