/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento

/**
 * Selector circular de estado idéntico al de la pantalla principal para la selección de importación.
 */
@Composable
fun CheckCircularImportacion(
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    val fondo = when {
        !habilitado -> Borde.copy(alpha = 0.35f)
        seleccionado -> ColorAcento
        else -> Borde
    }
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        if (seleccionado) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = if (habilitado) ColorSobreAcento else ColorSobreAcento.copy(alpha = 0.4f),
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
