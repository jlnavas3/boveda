/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Cabecera compacta en una sola línea horizontal para el diálogo de transferencia CXF:
 * icono de llave neutro (sin color de acento), título legible y acceso directo a Ajustes.
 */
@Composable
fun CabeceraDialogoTransferirCxf(
    totalSeleccionadas: Int,
    esSeleccionPersonalizada: Boolean,
    alAbrirAjustes: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val titulo = if (esSeleccionPersonalizada) {
        "Transferir selección ($totalSeleccionadas)"
    } else {
        "Transferencia directa"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        // Icono squircle neutro y compacto (sin saturar de acento)
        val formaIcono = FormaPequena
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(formaIcono)
                .background(ColorTarjetaAjustes)
                .border(if (GrosorBorde > 0.dp) GrosorBorde else 1.dp, ColorBordeActual, formaIcono),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.VpnKey,
                contentDescription = null,
                tint = TextoPrincipal,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = TextoPrincipal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        if (alAbrirAjustes != null) {
            IconButton(
                onClick = alAbrirAjustes,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Configurar transferencia",
                    tint = TextoSecundario,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
