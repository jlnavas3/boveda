/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.transiciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pastilla de acceso rápido para preajustes de duración en milisegundos con feedback háptico.
 */
@Composable
fun BotonAccesoRapidoDuracion(
    etiqueta: String,
    activo: Boolean,
    modifier: Modifier = Modifier,
    alPulsar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Box(
        modifier = modifier
            .clip(FormaPequena)
            .background(if (activo) ColorAcento.copy(alpha = 0.22f) else ColorSeparadorAjustes.copy(alpha = 0.6f))
            .clickable {
                haptica.tic()
                alPulsar()
            }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = etiqueta,
            color = if (activo) ColorAcento else ColorAjusteGris,
            fontSize = (10.5f * EscalaTexto).sp,
            fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal
        )
    }
}
