/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun IconoAlternarContrasena(
    esVisible: Boolean,
    onToggle: () -> Unit
) {
    val contexto = androidx.compose.ui.platform.LocalContext.current
    val haptica = androidx.compose.runtime.remember { com.jlnavas3.bovedalocal.util.Haptica(contexto) }
    IconButton(onClick = {
        haptica.toque()
        onToggle()
    }) {
        Icon(
            imageVector = if (esVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            contentDescription = if (esVisible) "Ocultar contraseña" else "Mostrar contraseña",
            tint = TextoSecundario,
            modifier = Modifier.size(20.dp)
        )
    }
}
