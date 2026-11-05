/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.unificar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IconoTipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Fila interactiva para seleccionar una entrada específica como la principal a conservar durante la unificación.
 */
@Composable
fun ElementoEntradaUnificar(
    entrada: Entrada,
    esPrincipal: Boolean,
    alSeleccionar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(10.dp)
    val fondo = if (esPrincipal) ColorAcento.copy(alpha = 0.08f) else ColorCampoAjustes.copy(alpha = 0.35f)
    val borde = if (esPrincipal) ColorAcento.copy(alpha = 0.45f) else ColorSeparadorAjustes.copy(alpha = 0.3f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(fondo)
            .border(width = 1.dp, color = borde, shape = forma)
            .clickable(onClick = alSeleccionar)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = esPrincipal,
            onClick = alSeleccionar,
            colors = RadioButtonDefaults.colors(selectedColor = ColorAcento),
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.width(8.dp))

        IconoTipoEntrada(
            entrada = entrada,
            tamanoIcono = 32
        )
        Spacer(Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = entrada.titulo.ifBlank { "Sin título" },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (esPrincipal) FontWeight.SemiBold else FontWeight.Normal
                    ),
                    color = TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (esPrincipal) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ColorAcento.copy(alpha = 0.15f))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "Principal",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                            color = ColorAcento
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = entrada.usuario.ifBlank { "Sin usuario" },
                    style = MaterialTheme.typography.labelSmall,
                    color = TextoSecundario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (entrada.passkey != null) {
                    Text(
                        text = "• Passkey",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextoSecundario
                    )
                }
                if (!entrada.secretoTotp.isNullOrBlank()) {
                    Text(
                        text = "• 2FA",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}
