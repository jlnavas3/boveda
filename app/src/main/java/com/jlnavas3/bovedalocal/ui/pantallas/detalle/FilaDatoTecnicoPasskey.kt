/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun FilaDatoTecnicoPasskey(
    etiqueta: String,
    valor: String,
    esMonospace: Boolean = true,
    copiado: Boolean = false,
    alCopiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = alCopiar)
            .padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = etiqueta,
                    color = TextoSecundario,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
                )

                if (copiado) {
                    Text(
                        text = "✓ Copiado",
                        color = Menta,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                } else {
                    Text(
                        text = "tocar para copiar",
                        color = ColorAcento.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp)
                    )
                }
            }

            Spacer(Modifier.height(3.dp))

            Text(
                text = valor,
                color = if (esMonospace) ColorDatosPasskey else TextoPrincipal,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = if (esMonospace) FontFamily.Monospace else FontFamily.Default,
                    fontWeight = if (esMonospace) FontWeight.Normal else FontWeight.Medium,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )
        }
    }
}
