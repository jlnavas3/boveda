/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo minimalista de advertencia cuando se unifican entradas con usuarios distintos.
 * Permite seleccionar de forma limpia cuál conservar como principal; los demás se respaldan en notas.
 */
@Composable
fun DialogoAdvertenciaUnificarUsuarios(
    usuarios: List<String>,
    alConfirmar: (String) -> Unit,
    alDescartar: () -> Unit
) {
    var usuarioSeleccionado by remember(usuarios) {
        mutableStateOf(usuarios.firstOrNull() ?: "")
    }

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = RoundedCornerShape(CurvaturaEsquinas),
        containerColor = ColorTarjetaAjustes,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.WarningAmber,
                    contentDescription = null,
                    tint = Peligro,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Usuarios diferentes",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextoPrincipal
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Elige cuál conservar como principal:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
                Spacer(Modifier.height(10.dp))

                usuarios.forEach { usr ->
                    val esSeleccionado = usr == usuarioSeleccionado
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { usuarioSeleccionado = usr }
                            .padding(vertical = 4.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = esSeleccionado,
                            onClick = { usuarioSeleccionado = usr },
                            colors = RadioButtonDefaults.colors(selectedColor = ColorAcento)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = usr,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (esSeleccionado) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = TextoPrincipal
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "El otro usuario se respaldará en notas.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = TextoSecundario
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { alConfirmar(usuarioSeleccionado) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorAcento,
                    contentColor = ColorSobreAcento
                )
            ) {
                Text("Unificar")
            }
        },
        dismissButton = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    )
}
