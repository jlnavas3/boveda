/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Botón flotante secundario con dimensiones y capas unificadas.
 * A diferencia de SmallFloatingActionButton, integra el borde, recorte y fondo
 * en la misma caja métrica, eliminando cualquier espacio transparente entre el borde y el botón.
 */
@Composable
fun BotonFlotanteSecundario(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = ColorTarjetaAjustes,
    brush: Brush? = null,
    contentColor: Color = ColorAcento,
    colorBorde: Color = ColorBordeActual,
    tamano: Dp = 44.dp,
    shape: Shape? = null,
    elevation: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val forma = shape ?: RoundedCornerShape(CurvaturaEsquinas)

    Box(
        modifier = modifier
            .size(tamano)
            .shadow(elevation = elevation, shape = forma)
            .clip(forma)
            .then(
                if (brush != null) Modifier.background(brush)
                else Modifier.background(containerColor)
            )
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && colorBorde != Color.Transparent) {
                    Modifier.border(GrosorBorde, colorBorde, forma)
                } else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = {
                    haptica.toque()
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Sobrecarga estándar con icono vector para BotonFlotanteSecundario.
 */
@Composable
fun BotonFlotanteSecundario(
    onClick: () -> Unit,
    icono: ImageVector,
    descripcion: String,
    modifier: Modifier = Modifier,
    containerColor: Color = ColorTarjetaAjustes,
    brush: Brush? = null,
    contentColor: Color = ColorAcento,
    colorBorde: Color = ColorBordeActual,
    tamano: Dp = 44.dp,
    tamanoIcono: Dp = 22.dp,
    shape: Shape? = null,
    elevation: Dp = 4.dp
) {
    BotonFlotanteSecundario(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
        brush = brush,
        contentColor = contentColor,
        colorBorde = colorBorde,
        tamano = tamano,
        shape = shape,
        elevation = elevation
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = contentColor,
            modifier = Modifier.size(tamanoIcono)
        )
    }
}
