/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosRedWifi
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente desacoplado para edición directa y fuertemente tipada de conexiones Wi-Fi.
 */
@Composable
fun FormularioEdicionWifi(
    datos: DatosRedWifi,
    alCambiar: (DatosRedWifi) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    val ocultarPorDefecto = ajustes.seguridadVisualActiva && ajustes.ocultarContrasena
    var mostrarClave by remember(ajustes.seguridadVisualActiva, ajustes.ocultarContrasena) { mutableStateOf(!ocultarPorDefecto) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = datos.ssid,
            etiqueta = "Nombre de red (SSID)",
            alCambiar = {
                alCambiar(datos.copy(ssid = it))
            },
            colorBordeIzquierdo = ColorDatosWeb
        )

        CampoBoveda(
            valor = datos.contrasena,
            etiqueta = "Contraseña Wi-Fi",
            alCambiar = {
                alCambiar(datos.copy(contrasena = it))
            },
            esContrasena = true,
            mostrarContrasena = mostrarClave,
            alAlternarMostrarContrasena = { mostrarClave = !mostrarClave },
            monoespaciada = true,
            colorBordeIzquierdo = ColorDatosContrasena
        )

        Column {
            CampoBoveda(
                valor = datos.tipoSeguridad,
                etiqueta = "Tipo de seguridad (ej. WPA3, WPA2-Personal)",
                alCambiar = {
                    alCambiar(datos.copy(tipoSeguridad = it))
                },
                colorBordeIzquierdo = ColorDatos2FA
            )
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("WPA2 / WPA3", "WPA", "WEP", "Sin contraseña (Abierta)").forEach { opcion ->
                    val seleccionado = datos.tipoSeguridad.equals(opcion, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (seleccionado) ColorAcento else ColorCampoAjustes)
                            .clickable {
                                alCambiar(datos.copy(tipoSeguridad = opcion))
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = opcion,
                            color = if (seleccionado) ColorSobreAcento else TextoSecundario,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}
