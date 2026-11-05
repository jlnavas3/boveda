/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Microbotón desacoplado para abrir la dirección física en la app de mapas o navegación.
 */
@Composable
fun BotonAccionMapa(
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = alPulsar,
        modifier = modifier.size(38.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Navigation,
            contentDescription = "Abrir mapa",
            tint = ColorIconosInternos,
            modifier = Modifier.size(19.dp)
        )
    }
}
