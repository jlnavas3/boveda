/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento

private enum class EstadoVisualGrupo {
    TOTAL,
    PARCIAL,
    NINGUNO
}

/**
 * Microcomponente que desacopla el avatar interactivo del grupo en la lista.
 * Permite selección directa estilo Google con un tap sobre el icono del grupo,
 * transformándose con una animación fluida hacia el checkmark (✓) o guión parcial (-)
 * en la misma posición exacta sin desplazar la fila ni requerir casillas adicionales.
 */
@Composable
fun AvatarSeleccionableGrupo(
    vectorIcono: ImageVector,
    colorIcono: Color,
    tamanoIcono: Int,
    seleccionado: Boolean,
    parcialmenteSeleccionado: Boolean,
    alAlternarSeleccion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estadoVisual = when {
        seleccionado -> EstadoVisualGrupo.TOTAL
        parcialmenteSeleccionado -> EstadoVisualGrupo.PARCIAL
        else -> EstadoVisualGrupo.NINGUNO
    }

    Box(
        modifier = modifier
            .size(tamanoIcono.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = (tamanoIcono / 2).dp),
                onClick = alAlternarSeleccion
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = estadoVisual,
            transitionSpec = {
                (scaleIn(initialScale = 0.8f) + fadeIn()) togetherWith (scaleOut(targetScale = 0.8f) + fadeOut())
            },
            label = "AvatarGrupoSeleccionAnim"
        ) { estado ->
            when (estado) {
                EstadoVisualGrupo.TOTAL -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(ColorAcento),
                        contentAlignment = Alignment.Center
                    ) {
                        val tamanoCheck = (tamanoIcono * 0.52f).coerceAtLeast(14f)
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Grupo seleccionado",
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(tamanoCheck.dp)
                        )
                    }
                }
                EstadoVisualGrupo.PARCIAL -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(ColorAcento),
                        contentAlignment = Alignment.Center
                    ) {
                        val tamanoDash = (tamanoIcono * 0.52f).coerceAtLeast(14f)
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Grupo parcialmente seleccionado",
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(tamanoDash.dp)
                        )
                    }
                }
                EstadoVisualGrupo.NINGUNO -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(ColorCampoAjustes),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = vectorIcono,
                            contentDescription = null,
                            tint = colorIcono,
                            modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                        )
                    }
                }
            }
        }
    }
}
