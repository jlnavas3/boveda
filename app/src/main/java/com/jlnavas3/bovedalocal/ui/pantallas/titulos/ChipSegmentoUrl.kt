/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun ChipSegmentoUrl(
    texto: String,
    seleccionado: Boolean,
    esRegla: Boolean,
    alSeleccionar: () -> Unit,
    alAlternarRegla: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (seleccionado) ColorAcento.copy(alpha = 0.22f) else SuperficieAlta
            )
            .border(
                width = 1.dp,
                color = if (seleccionado) ColorAcento else ColorAcento.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        // Área principal del chip: selecciona el nombre para el servicio
        Box(
            modifier = Modifier
                .clickable(onClick = alSeleccionar)
                .padding(
                    start = 7.dp,
                    end = if (seleccionado) 7.dp else 3.dp,
                    top = 2.dp,
                    bottom = 2.dp
                )
        ) {
            Text(
                text = texto,
                color = if (seleccionado) ColorAcento else TextoPrincipal,
                fontSize = 11.5.sp,
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium
            )
        }

        // Icono de estado de regla: solo se muestra en chips NO seleccionados (candidatos a descarte)
        // El chip seleccionado es el nombre del servicio y nunca debe agregarse a reglas de descarte.
        if (!seleccionado) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clip(CircleShape)
                    .clickable(onClick = alAlternarRegla)
                    .padding(2.dp)
            ) {
                Icon(
                    imageVector = if (esRegla) Icons.Filled.Check else Icons.Filled.Add,
                    contentDescription = if (esRegla) "Regla activa (toca para quitar)" else "Guardar en reglas de descarte",
                    tint = if (esRegla) Menta else TextoSecundario.copy(alpha = 0.6f),
                    modifier = Modifier.size(11.dp)
                )
            }
        }
    }
}
