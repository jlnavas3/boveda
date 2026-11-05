/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Microbotón desacoplado para iniciar chat o envío efímero de WhatsApp directo.
 */
@Composable
fun BotonAccionWhatsApp(
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = alPulsar,
        modifier = modifier.size(38.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Chat,
            contentDescription = "WhatsApp confidencial",
            tint = Color(0xFF25D366),
            modifier = Modifier.size(19.dp)
        )
    }
}
