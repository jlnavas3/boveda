/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Micro-componente desacoplado que muestra la insignia y estado de salud de la bóveda
 * en la cabecera principal, permitiendo acceder a la auditoría con un solo toque.
 */
@Composable
fun InsigniaSaludBoveda(
    alAbrirSalud: () -> Unit,
    cantidadProblemas: Int = 0,
    modifier: Modifier = Modifier
) {
    val colorEscudo = when {
        cantidadProblemas == 0 -> Menta
        cantidadProblemas <= 2 -> Color(0xFFFFB300) // Ámbar preventivo
        else -> Peligro
    }

    if (cantidadProblemas > 0) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = colorEscudo,
                    contentColor = Color.Black
                ) {
                    Text("$cantidadProblemas")
                }
            },
            modifier = modifier
        ) {
            IconButton(
                onClick = alAbrirSalud,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = "Auditoría de salud ($cantidadProblemas alertas)",
                    tint = colorEscudo,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    } else {
        IconButton(
            onClick = alAbrirSalud,
            modifier = modifier
                .size(40.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = "Auditoría de salud excelente",
                tint = colorEscudo,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
