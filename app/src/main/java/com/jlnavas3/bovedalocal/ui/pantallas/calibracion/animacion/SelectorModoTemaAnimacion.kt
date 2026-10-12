/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Selector segmentado para alternar la vista y calibración entre paleta de Tema Claro y Tema Oscuro.
 */
@Composable
fun SelectorModoTemaAnimacion(
    esOscuro: Boolean,
    alCambiarModo: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeActual, forma) else Modifier
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val seleccionadoClaro = !esOscuro
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (seleccionadoClaro) ColorAcento.copy(alpha = 0.2f) else Color.Transparent)
                .border(
                    width = if (seleccionadoClaro) 1.5.dp else 0.dp,
                    color = if (seleccionadoClaro) ColorAcento else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { alCambiarModo(false) }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "☀️ Tema Claro",
                color = if (seleccionadoClaro) ColorAcento else TextoSecundario,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (seleccionadoClaro) FontWeight.Bold else FontWeight.Medium
                )
            )
        }

        val seleccionadoOscuro = esOscuro
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (seleccionadoOscuro) ColorAcento.copy(alpha = 0.2f) else Color.Transparent)
                .border(
                    width = if (seleccionadoOscuro) 1.5.dp else 0.dp,
                    color = if (seleccionadoOscuro) ColorAcento else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { alCambiarModo(true) }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🌙 Tema Oscuro",
                color = if (seleccionadoOscuro) ColorAcento else TextoSecundario,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (seleccionadoOscuro) FontWeight.Bold else FontWeight.Medium
                )
            )
        }
    }
}
