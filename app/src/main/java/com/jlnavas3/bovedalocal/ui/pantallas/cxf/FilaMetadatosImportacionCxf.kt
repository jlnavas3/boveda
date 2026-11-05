/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Microcomponente que renderiza los metadatos provistos por el origen (Google):
 * fecha de creación, fecha de última modificación y etiquetas asociadas.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilaMetadatosImportacionCxf(
    entrada: Entrada,
    modifier: Modifier = Modifier
) {
    val formatoFecha = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (entrada.creadaEn > 0L) {
            val fechaCreacionTexto = try {
                formatoFecha.format(Date(entrada.creadaEn))
            } catch (_: Exception) {
                ""
            }
            if (fechaCreacionTexto.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Fecha de creación",
                        tint = ColorAjusteGris,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "Creada: $fechaCreacionTexto",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        color = TextoSecundario
                    )
                }
            }
        }

        if (entrada.modificadaEn > 0L && entrada.modificadaEn != entrada.creadaEn) {
            val fechaModificacionTexto = try {
                formatoFecha.format(Date(entrada.modificadaEn))
            } catch (_: Exception) {
                ""
            }
            if (fechaModificacionTexto.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.EditCalendar,
                        contentDescription = "Última modificación",
                        tint = ColorAjusteGris,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "Modificada: $fechaModificacionTexto",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        color = TextoSecundario
                    )
                }
            }
        }

        entrada.etiquetas.forEach { etiqueta ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ColorCampoAjustes)
                    .padding(horizontal = 6.dp, vertical = 1.5.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Label,
                    contentDescription = null,
                    tint = ColorAjusteGris,
                    modifier = Modifier.size(10.dp)
                )
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = TextoSecundario
                )
            }
        }
    }
}
