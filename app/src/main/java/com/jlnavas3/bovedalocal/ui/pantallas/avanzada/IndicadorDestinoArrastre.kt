/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Indicador visual que resalta la posición exacta de destino (slot)
 * durante el arrastre de un elemento en el árbol de reorganización de ajustes.
 */
@Composable
fun IndicadorDestinoArrastre(
    modifier: Modifier = Modifier,
    texto: String = "Soltar aquí"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(ColorAcento, CircleShape)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(ColorAcento, RoundedCornerShape(1.dp))
        )
        Spacer(Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .background(ColorAcento.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
            Text(
                text = texto,
                color = ColorAcento,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
