/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.seleccion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.hud.accionConHud
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Botón de acción individual plano y circular para la barra inferior de selección múltiple (estilo Google/Gmail).
 * Ofrece un área táctil ergonómica de 44dp con efecto ripple centrado y soporte para HUD flotante en pulsación sostenida.
 */
@Composable
fun BotonAccionSeleccionIcono(
    icono: ImageVector,
    descripcion: String,
    habilitado: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    colorIcono: Color = ColorIconosInternos
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .accionConHud(
                etiqueta = descripcion,
                icono = icono,
                habilitado = habilitado,
                radioRipple = 22.dp,
                alPulsar = alPulsar
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = if (habilitado) colorIcono else TextoSecundario.copy(alpha = 0.35f),
            modifier = Modifier.size(24.dp)
        )
    }
}
