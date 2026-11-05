/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoCorreoBaseAlias(
    correoActual: String,
    alCerrar: () -> Unit,
    alGuardar: (String) -> Unit
) {
    var correoTemporal by remember(correoActual) { mutableStateOf(correoActual) }

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Correo base para alias",
        botonConfirmar = {
            TextButton(onClick = {
                alCerrar()
                alGuardar(correoTemporal.trim())
            }) {
                Text("Guardar", color = ColorAcento)
            }
        },
        botonDescartar = {
            Row {
                TextButton(onClick = {
                    correoTemporal = ""
                    alGuardar("")
                    alCerrar()
                }) {
                    Text("Borrar", color = TextoSecundario)
                }
                TextButton(onClick = alCerrar) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        }
    ) {
        Column {
            Text(
                "Ingresa tu dirección de correo electrónico real (ej. usuario@gmail.com). Se usará automáticamente como plantilla al generar nuevos alias en formularios.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(14.dp))
            CampoBoveda(
                valor = correoTemporal,
                etiqueta = "Correo principal",
                alCambiar = { correoTemporal = it }
            )
        }
    }
}
