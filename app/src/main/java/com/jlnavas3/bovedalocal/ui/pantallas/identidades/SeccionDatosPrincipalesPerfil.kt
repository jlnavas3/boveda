/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Sección de datos principales del perfil (nombre, icono representativo y selector de color).
 */
@Composable
fun SeccionDatosPrincipalesPerfil(
    nombre: String,
    alCambiarNombre: (String) -> Unit,
    colorHex: String?,
    alCambiarColorHex: (String) -> Unit,
    colorAcento: Color,
    focusRequester: FocusRequester? = null,
    modifier: Modifier = Modifier
) {
    val colorActual = parsearColorO(colorHex ?: "", colorAcento)

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
                    icono = Icons.Filled.AccountCircle,
                    tamano = TamanoInsignia.MEDIANO,
                    colorFondo = colorActual.copy(alpha = 0.16f),
                    colorIcono = colorActual
                )
                Column {
                    TextoTitulo(
                        texto = "Datos del perfil",
                        estilo = EstiloTitulo.PEQUENO
                    )
                    TextoCuerpo(
                        texto = "Nombre identificador y color de distinción visual",
                        tamano = TamanoCuerpo.MINI,
                        color = TextoSecundario
                    )
                }
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = alCambiarNombre,
                label = { Text("Nombre del perfil", fontSize = 12.sp) },
                placeholder = { Text("ej. Personal, Trabajo, Empresa", fontSize = 12.sp) },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.5.sp),
                shape = FormaPequena,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ColorCampoAjustes,
                    unfocusedContainerColor = ColorCampoAjustes,
                    focusedBorderColor = colorActual,
                    unfocusedBorderColor = ColorSeparadorAjustes,
                    focusedTextColor = TextoPrincipal,
                    unfocusedTextColor = TextoPrincipal
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
            )

            Column {
                TextoSubtitulo(
                    texto = "Color distintivo",
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PaletaColoresIdentidad.forEach { hex ->
                        val colorOpcion = parsearColorO(hex, ColorAcento)
                        val seleccionado = colorHex?.equals(hex, ignoreCase = true) == true
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(colorOpcion)
                                .border(
                                    width = if (seleccionado) 2.5.dp else 1.dp,
                                    color = if (seleccionado) TextoPrincipal else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { alCambiarColorHex(hex) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (seleccionado) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
