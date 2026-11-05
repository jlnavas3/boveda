/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado.reglas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoAgregarPaqueteComunicacion(
    alConfirmar: (paquete: String) -> Unit,
    alDescartar: () -> Unit
) {
    var paquete by remember { mutableStateOf("") }
    val paqueteLimpio = paquete.trim().lowercase().removePrefix("android://")
    val esValido = paqueteLimpio.isNotBlank()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Agregar App de Comunicación",
        botonConfirmar = {
            TextButton(
                onClick = {
                    if (esValido) {
                        alConfirmar(paqueteLimpio)
                    }
                },
                enabled = esValido
            ) {
                Text("Agregar", color = if (esValido) ColorAcento else TextoSecundario)
            }
        },
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Ingresa el nombre del paquete de una aplicación de mensajería, llamadas o contactos para ofrecer el autocompletado y guardado de contactos:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = paquete,
                alCambiar = { paquete = it },
                etiqueta = "Paquete (ej. org.thoughtcrime.securesms, im.vector.app)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "En estas aplicaciones se habilitará la detección contextual de números telefónicos y nombres para sugerir o crear contactos.",
                color = TextoSecundario,
                fontSize = 12.sp
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaDialogoAgregarPaqueteComunicacion() {
    BovedaTheme {
        DialogoAgregarPaqueteComunicacion(
            alConfirmar = {},
            alDescartar = {}
        )
    }
}
