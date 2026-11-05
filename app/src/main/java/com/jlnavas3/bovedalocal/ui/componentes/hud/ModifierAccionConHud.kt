/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.hud

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Modificador ergonómico para botones e iconos que soporta pulsación normal (onClick)
 * y pulsación sostenida (onLongClick) para disparar el HUD global de acciones.
 *
 * Al mantener presionado:
 * - Emite una vibración táctil suave (`haptica.tic()`).
 * - Proyecta en la base de la pantalla la pastilla HUD con el icono y la descripción durante 2.5s.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.accionConHud(
    etiqueta: String,
    icono: ImageVector? = null,
    habilitado: Boolean = true,
    radioRipple: Dp = 20.dp,
    alPulsar: () -> Unit
): Modifier {
    val gestorHud = LocalHudAccion.current
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    return this.combinedClickable(
        enabled = true,
        interactionSource = remember { MutableInteractionSource() },
        indication = if (habilitado) ripple(bounded = true, radius = radioRipple) else null,
        onClick = {
            if (habilitado) {
                alPulsar()
            }
        },
        onLongClick = {
            haptica.tic()
            gestorHud?.mostrar(etiqueta = etiqueta, icono = icono)
        }
    )
}
