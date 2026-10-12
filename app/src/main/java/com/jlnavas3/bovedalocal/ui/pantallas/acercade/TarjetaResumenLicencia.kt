/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaResumenLicencia(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(16.dp)))
            .background(ColorTarjetaAjustes)
            .border(1.dp, ColorBordeActual.copy(alpha = 0.5f), RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(16.dp)))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorAcento.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Verified,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column {
                Text(
                    text = "GNU General Public License v3.0",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos
                )
                Text(
                    text = "Copyleft Fuerte · 100% Código Abierto",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorAcento
                )
            }
        }

        Text(
            text = "Bóveda Local es software libre. Tienes plena libertad para usar, auditar, inspeccionar, modificar y compartir el código, garantizando que cualquier versión derivada permanezca siempre libre y bajo estos mismos términos.",
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
            color = TextoSecundario
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ColorBordeActual.copy(alpha = 0.15f))
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        text = "Protección de Marca (Sección 7e)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextoPrincipal
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "El nombre \"Bóveda Local\", identificador y logotipos están protegidos. Toda versión modificada o fork debe utilizar una denominación e identidad visual claramente distintas.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 15.sp),
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}
