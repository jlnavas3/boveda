/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.hud.accionConHud
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Microcomponente que renderiza el botón de acceso al asistente y chuleta de comandos de búsqueda.
 */
@Composable
fun BotonAsistenteBusqueda(
    activo: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconoColor by animateColorAsState(
        targetValue = if (activo) ColorAcento else ColorIconosInternos,
        label = "colorIconoAsistente"
    )

    Box(
        modifier = modifier
            .size(28.dp)
            .clip(CircleShape)
            .accionConHud(
                etiqueta = "Asistente de comandos",
                icono = Icons.Filled.TipsAndUpdates,
                radioRipple = 14.dp,
                alPulsar = alPulsar
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.TipsAndUpdates,
            contentDescription = "Asistente de comandos de búsqueda",
            tint = iconoColor,
            modifier = Modifier.size(19.dp)
        )
    }
}
