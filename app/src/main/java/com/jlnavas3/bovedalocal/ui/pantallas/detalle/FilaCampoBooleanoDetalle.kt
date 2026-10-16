/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Composable micro desacoplado para visualización de campos tipo [com.jlnavas3.bovedalocal.data.TipoCampo.BOOLEANO] en la vista de detalle.
 */
@Composable
fun FilaCampoBooleanoDetalle(
    campo: CampoPersonalizado,
    modifier: Modifier = Modifier
) {
    val activo = campo.valor.equals("true", ignoreCase = true)
    val colorBadge = if (activo) ColorAcento else TextoSecundario
    val fondoBadge = colorBadge.copy(alpha = 0.15f)

    TarjetaDatoDetalle(colorBorde = ColorAcento, modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    text = campo.etiqueta.ifBlank { "Campo Sí/No" },
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TextoSecundario
                )
                Text(
                    text = if (activo) "Activado" else "Desactivado",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = TextoPrincipal
                )
            }

            Box(
                modifier = Modifier
                    .clip(FormaPequena)
                    .background(fondoBadge)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (activo) Icons.Filled.Check else Icons.Filled.Close,
                        contentDescription = if (activo) "Sí" else "No",
                        tint = colorBadge,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (activo) "Sí" else "No",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorBadge
                    )
                }
            }
        }
    }
}
