/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.seguridad.comunes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoEditorContrasenaComun(
    alConfirmar: (String) -> Unit,
    alCerrar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    val esValida = texto.trim().isNotBlank()

    DialogoBoveda(
        onDismissRequest = alCerrar,
        confirmButton = {
            TextButton(
                onClick = {
                    if (esValida) {
                        alConfirmar(texto.trim())
                    }
                },
                enabled = esValida
            ) {
                Text("Añadir", color = if (esValida) ColorAcento else Color.Gray, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar", color = TextoSecundario)
            }
        },
        title = { Text("Añadir contraseña común", color = TextoPrincipal, fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                singleLine = true,
                label = { Text("Contraseña a marcar como común") },
                shape = RoundedCornerShape(CurvaturaEsquinas),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorAcento,
                    unfocusedBorderColor = TextoSecundario.copy(alpha = 0.3f),
                    focusedTextColor = TextoPrincipal,
                    unfocusedTextColor = TextoPrincipal
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
