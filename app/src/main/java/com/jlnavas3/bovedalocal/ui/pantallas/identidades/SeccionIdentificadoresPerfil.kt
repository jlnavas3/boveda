/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Sección de identificadores de acceso para el perfil (correo principal, alias
 * y preferencia de autocompletado en caso de existir documento vinculado).
 */
@Composable
fun SeccionIdentificadoresPerfil(
    correoPrincipal: String,
    alCambiarCorreoPrincipal: (String) -> Unit,
    correosSecundariosTexto: String,
    alCambiarCorreosSecundariosTexto: (String) -> Unit,
    preferenciaIdentificador: String,
    alCambiarPreferenciaIdentificador: (String) -> Unit,
    tieneDocumento: Boolean = false,
    siglaDocumento: String = "Documento",
    colorAcento: Color,
    modifier: Modifier = Modifier
) {
    ContenedorTarjeta(
        modifier = modifier,
        colorFondo = ColorTarjetaAjustes,
        paddingInterno = 14.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ContenedorIconoInsignia(
                    icono = Icons.Filled.Badge,
                    tamano = TamanoInsignia.MEDIANO,
                    colorFondo = ColorCampoAjustes,
                    colorIcono = TextoPrincipal
                )
                Column {
                    TextoTitulo(
                        texto = "Identificadores de usuario",
                        estilo = EstiloTitulo.PEQUENO
                    )
                    TextoCuerpo(
                        texto = "Correos y preferencia de inicio de sesión",
                        tamano = TamanoCuerpo.MINI,
                        color = TextoSecundario
                    )
                }
            }

            OutlinedTextField(
                value = correoPrincipal,
                onValueChange = alCambiarCorreoPrincipal,
                label = { Text("Correo principal (opcional)", fontSize = 12.sp) },
                placeholder = { Text("ejemplo@correo.com", fontSize = 12.sp) },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.5.sp),
                shape = FormaPequena,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ColorCampoAjustes,
                    unfocusedContainerColor = ColorCampoAjustes,
                    focusedBorderColor = colorAcento,
                    unfocusedBorderColor = ColorSeparadorAjustes,
                    focusedTextColor = TextoPrincipal,
                    unfocusedTextColor = TextoPrincipal
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (correoPrincipal.isNotBlank()) {
                OutlinedTextField(
                    value = correosSecundariosTexto,
                    onValueChange = alCambiarCorreosSecundariosTexto,
                    label = { Text("Alias / correos adicionales", fontSize = 12.sp) },
                    placeholder = { Text("alias1@empresa.com, personal2@dominio.com", fontSize = 12.sp) },
                    singleLine = false,
                    maxLines = 2,
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.5.sp),
                    shape = FormaPequena,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ColorCampoAjustes,
                        unfocusedContainerColor = ColorCampoAjustes,
                        focusedBorderColor = colorAcento,
                        unfocusedBorderColor = ColorSeparadorAjustes,
                        focusedTextColor = TextoPrincipal,
                        unfocusedTextColor = TextoPrincipal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (correoPrincipal.isNotBlank() && tieneDocumento) {
                SelectorPreferenciaIdentificador(
                    preferenciaActual = preferenciaIdentificador,
                    alSeleccionarPreferencia = alCambiarPreferenciaIdentificador,
                    colorAcento = colorAcento,
                    siglaDocumento = siglaDocumento
                )
            }
        }
    }
}
