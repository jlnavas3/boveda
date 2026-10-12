/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Botón de icono estandarizado para las cabeceras de pantalla (40.dp, recorte circular, icono de 20.dp).
 * En reposo es limpio y transparente; al hacer tap muestra sutilmente el efecto ripple circular nativo.
 */
@Composable
fun BotonIconoCabecera(
    onClick: () -> Unit,
    icono: ImageVector,
    descripcion: String,
    modifier: Modifier = Modifier,
    tint: Color = ColorIconosInternos,
    colorFondo: Color = Color.Transparent,
    habilitado: Boolean = true
) {
    IconButton(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .then(
                if (colorFondo != Color.Transparent) {
                    Modifier.background(if (habilitado) colorFondo else colorFondo.copy(alpha = 0.5f))
                } else Modifier
            )
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = if (habilitado) tint else tint.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}
