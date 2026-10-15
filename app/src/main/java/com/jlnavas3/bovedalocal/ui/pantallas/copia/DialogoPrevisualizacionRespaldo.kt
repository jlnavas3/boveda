/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.copia

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.InfoRespaldoPrevisualizado
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Micro-diálogo desacoplado para inspeccionar previamente una copia de seguridad antes de fusionar o restaurar.
 */
@Composable
fun DialogoPrevisualizacionRespaldo(
    info: InfoRespaldoPrevisualizado,
    alConfirmarRestauracion: () -> Unit,
    alDescartar: () -> Unit
) {
    val fechaFormateada = if (info.fechaMasReciente > 0) {
        SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault()).format(Date(info.fechaMasReciente))
    } else {
        "Sin fecha registrada"
    }

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = FormaTarjeta,
        icon = {
            Icon(
                imageVector = Icons.Filled.Backup,
                contentDescription = null,
                tint = ColorAcento
            )
        },
        title = {
            Text(
                text = "Resumen de la copia",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Se ha verificado la copia de seguridad. Los datos contenidos son:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Key, contentDescription = null, tint = ColorAcento)
                    Spacer(Modifier.width(10.dp))
                    Text("${info.totalEntradas} credenciales cifradas", color = ColorTitulos)
                }
                if (info.totalCategorias > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Folder, contentDescription = null, tint = ColorAcento)
                        Spacer(Modifier.width(10.dp))
                        Text("${info.totalCategorias} categorías", color = ColorTitulos)
                    }
                }
                if (info.totalIdentidades > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = ColorAcento)
                        Spacer(Modifier.width(10.dp))
                        Text("${info.totalIdentidades} identidades", color = ColorTitulos)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = ColorAcento)
                    Spacer(Modifier.width(10.dp))
                    Text("Última modificación: $fechaFormateada", color = TextoSecundario)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = alConfirmarRestauracion,
                colors = ButtonDefaults.textButtonColors(contentColor = ColorAcento)
            ) {
                Text("Restaurar ahora", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar")
            }
        }
    )
}
