/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import androidx.compose.ui.text.font.FontStyle
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Microcomponente que presenta las fechas de auditoría (Editada y Último uso)
 * como texto plano sutil en cursiva, alineado a la derecha y próximo al borde del campo.
 */
@Composable
fun PieMetadatosDetalle(
    creadaEn: Long,
    modificadaEn: Long,
    ultimoUsoEn: Long = 0L,
    tipo: TipoEntrada? = null,
    modifier: Modifier = Modifier
) {
    val mostrarEditada = modificadaEn > 0L && modificadaEn != creadaEn
    val mostrarUltimoUso = ultimoUsoEn > 0L && tipo != TipoEntrada.NOTA

    if (!mostrarEditada && !mostrarUltimoUso) return

    val formatoFecha = remember {
        SimpleDateFormat("dd/MM/yy HH:mm", Locale.forLanguageTag("es-ES"))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (mostrarEditada) {
            Text(
                text = "Editada el ${formatoFecha.format(Date(modificadaEn))}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontStyle = FontStyle.Italic,
                    fontSize = 11.5.sp
                ),
                color = TextoSecundario
            )
        }
        if (mostrarUltimoUso) {
            Text(
                text = "Usada el ${formatoFecha.format(Date(ultimoUsoEn))}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontStyle = FontStyle.Italic,
                    fontSize = 11.5.sp
                ),
                color = TextoSecundario
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun PieMetadatosDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        PieMetadatosDetalle(
            creadaEn = System.currentTimeMillis() - 86400000L * 120,
            modificadaEn = System.currentTimeMillis() - 86400000L * 2,
            ultimoUsoEn = System.currentTimeMillis() - 3600000L * 4
        )
    }
}

