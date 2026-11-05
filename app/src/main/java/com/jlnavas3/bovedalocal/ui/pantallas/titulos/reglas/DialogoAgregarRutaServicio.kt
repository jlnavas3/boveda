/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.titulos.reglas

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
fun DialogoAgregarRutaServicio(
    alConfirmar: (ruta: String, servicio: String) -> Unit,
    alDescartar: () -> Unit
) {
    var ruta by remember { mutableStateOf("") }
    var servicio by remember { mutableStateOf("") }

    val rutaLimpia = ruta.trim().trim('/')
    val esValido = rutaLimpia.isNotBlank() && servicio.trim().isNotBlank()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Agregar servicio por ruta URL",
        botonConfirmar = {
            TextButton(
                onClick = {
                    if (esValido) {
                        alConfirmar(rutaLimpia, servicio.trim())
                    }
                },
                enabled = esValido
            ) {
                Text("Guardar", color = if (esValido) ColorAcento else TextoSecundario)
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
                text = "Asocia una ruta URL o subcarpeta (en redes locales, IPs o localhost) al nombre de la aplicación o servicio:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            CampoBoveda(
                valor = ruta,
                alCambiar = { ruta = it.lowercase() },
                etiqueta = "Ruta web (ej. phpmyadmin, portal, nextcloud)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            CampoBoveda(
                valor = servicio,
                alCambiar = { servicio = it },
                etiqueta = "Nombre del servicio (ej. phpMyAdmin, Nextcloud)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Al detectar esta ruta en la URL de una red privada, se nombrará automáticamente como este servicio.",
                color = TextoSecundario,
                fontSize = 12.sp
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaDialogoAgregarRutaServicio() {
    BovedaTheme {
        DialogoAgregarRutaServicio(
            alConfirmar = { _, _ -> },
            alDescartar = {}
        )
    }
}
