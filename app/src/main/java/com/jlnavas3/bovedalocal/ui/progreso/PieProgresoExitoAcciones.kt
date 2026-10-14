/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.progreso

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Microcomponente que renderiza los botones de acción al finalizar con éxito una operación,
 * permitiendo decidir entre renombrar o ver el listado.
 */
@Composable
fun PieProgresoExitoAcciones(
    textoPrincipal: String?,
    textoSecundario: String?,
    alPulsarPrincipal: (() -> Unit)?,
    alPulsarSecundario: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        // Botón secundario: "Ver listado"
        if (!textoSecundario.isNullOrBlank()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(CurvaturaEsquinas))
                    .background(SuperficieAlta)
                    .border(
                        width = GrosorBorde.coerceAtMost(1.dp),
                        color = ColorBordeActual.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(CurvaturaEsquinas)
                    )
                    .clickable(onClick = alPulsarSecundario)
                    .padding(vertical = 11.dp, horizontal = 12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                        contentDescription = null,
                        tint = TextoPrincipal,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = textoSecundario,
                        color = TextoPrincipal,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Botón principal: "Renombrar"
        if (!textoPrincipal.isNullOrBlank() && alPulsarPrincipal != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(if (textoSecundario.isNullOrBlank()) 1f else 1.3f)
                    .clip(RoundedCornerShape(CurvaturaEsquinas))
                    .background(ColorAcento)
                    .clickable(onClick = alPulsarPrincipal)
                    .padding(vertical = 11.dp, horizontal = 12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DriveFileRenameOutline,
                        contentDescription = null,
                        tint = ColorSobreAcento,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = textoPrincipal,
                        color = ColorSobreAcento,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
