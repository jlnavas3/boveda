/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente desacoplado que renderiza el botón de texto en cursiva "Más..." o "Menos...",
 * alineado a la derecha de la pantalla, sin subrayado ni flechas para conmutar la visibilidad
 * de las secciones secundarias vacías.
 */
@Composable
fun BotonAlternarMasOpciones(
    expandido: Boolean,
    alAlternar: () -> Unit,
    modifier: Modifier = Modifier,
    haptica: Haptica? = null
) {
    val contexto = LocalContext.current
    val hapticaEfectiva = haptica ?: remember { Haptica(contexto) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (expandido) "Menos..." else "Más...",
            style = MaterialTheme.typography.bodySmall.copy(
                fontStyle = FontStyle.Italic,
                fontSize = 12.5.sp,
                textDecoration = TextDecoration.None
            ),
            color = TextoSecundario,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false, radius = 24.dp),
                    onClick = {
                        hapticaEfectiva.tic()
                        alAlternar()
                    }
                )
                .padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
}
