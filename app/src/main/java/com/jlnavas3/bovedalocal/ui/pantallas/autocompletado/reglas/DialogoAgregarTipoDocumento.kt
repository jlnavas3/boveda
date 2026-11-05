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
fun DialogoAgregarTipoDocumento(
    alConfirmar: (String) -> Unit,
    alDescartar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    val limpia = texto.trim().uppercase()
    val esValido = limpia.isNotBlank() && limpia.length in 2..15

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Agregar tipo de documento oficial",
        botonConfirmar = {
            TextButton(
                onClick = {
                    if (esValido) {
                        alConfirmar(limpia)
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
                text = "Ingresa la sigla o abreviatura del documento oficial (ej. NIE, SSN, CPF, RG, NIF, RUN):",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = texto,
                alCambiar = { texto = it.uppercase() },
                etiqueta = "Sigla del documento (ej. NIE, SSN, CPF)",
                modifier = Modifier.fillMaxWidth()
            )
            if (esValido) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Aparecerá en el selector de tipos de documento oficial como '$limpia'.",
                    color = TextoSecundario,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaDialogoAgregarTipoDocumento() {
    BovedaTheme {
        DialogoAgregarTipoDocumento(
            alConfirmar = {},
            alDescartar = {}
        )
    }
}
