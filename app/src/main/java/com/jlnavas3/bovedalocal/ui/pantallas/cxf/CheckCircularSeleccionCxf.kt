/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento

/**
 * Selector circular de estado idéntico al del listado principal para la selección de credenciales CXF.
 */
@Composable
fun CheckCircularSeleccionCxf(
    seleccionado: Boolean,
    alAlternar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (seleccionado) ColorAcento else Color.Transparent)
            .then(
                if (!seleccionado) {
                    Modifier.border(1.5.dp, ColorSeparadorAjustes, CircleShape)
                } else Modifier
            )
            .clickable(onClick = alAlternar),
        contentAlignment = Alignment.Center
    ) {
        if (seleccionado) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = if (seleccionado) "Seleccionada" else "No seleccionada",
                tint = ColorSobreAcento,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
