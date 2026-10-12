/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.avisos

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AvisoInterno
import com.jlnavas3.bovedalocal.data.TipoAvisoInterno
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaAvisoInterno(
    aviso: AvisoInterno,
    alEjecutar: () -> Unit,
    alIgnorar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val icono = when (aviso.tipo) {
        TipoAvisoInterno.TITULOS_SIN_NORMALIZAR -> Icons.Filled.Spellcheck
        TipoAvisoInterno.DUPLICADOS_DETECTADOS -> Icons.AutoMirrored.Filled.CompareArrows
        TipoAvisoInterno.CONTRASENAS_VULNERABLES -> Icons.Filled.Security
        TipoAvisoInterno.RESPALDO_PENDIENTE -> Icons.Filled.Backup
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .border(GrosorBorde, ColorBordeActual, forma)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ColorAcento.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = ColorAcento,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = aviso.titulo,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = ColorTitulos
                        )
                        Text(
                            text = aviso.categoria.etiqueta,
                            style = MaterialTheme.typography.labelSmall,
                            color = ColorAcento
                        )
                    }
                }

                IconButton(
                    onClick = alIgnorar,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Descartar aviso",
                        tint = ColorIconosInternos.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = aviso.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = alEjecutar,
                shape = FormaBoton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorAcento,
                    contentColor = ColorSobreAcento
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = aviso.accionTexto,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
