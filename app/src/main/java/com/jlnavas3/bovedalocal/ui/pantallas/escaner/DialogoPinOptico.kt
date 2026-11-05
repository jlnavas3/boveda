/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo modal para ingresar el PIN efímero de 6 dígitos que protege la transferencia óptica.
 * Garantiza que ninguna cámara o tercero intermedio pueda leer el QR sin autorización visual directa.
 */
@Composable
fun DialogoPinOptico(
    totalTramas: Int,
    errorMensaje: String?,
    aliasEmisor: String? = null,
    alConfirmarPin: (String) -> Unit,
    alDescartar: () -> Unit
) {
    var pinIngresado by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {}
    }

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Autorizar Transferencia Óptica",
        icono = Icons.Filled.Lock,
        colorIcono = ColorAcento,
        botonConfirmar = {
            BotonColorido(
                texto = "Descifrar y Fusionar",
                icono = Icons.Filled.CheckCircle,
                color = ColorAcento,
                alPulsar = {
                    if (pinIngresado.trim().length == 6) {
                        alConfirmarPin(pinIngresado.trim())
                    }
                }
            )
        },
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Tarjeta de identificación del emisor
            val nombreEmisor = aliasEmisor?.takeIf { it.isNotBlank() } ?: "Dispositivo remoto"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaCampo)
                    .background(ColorCampoAjustes)
                    .border(0.8.dp, ColorBordeActual, FormaCampo)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ColorAcento.copy(alpha = 0.15f))
                        .border(1.dp, ColorAcento.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Devices,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Autorizar transferencia de:",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = nombreEmisor,
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            }

            Text(
                text = "Se ha recibido con éxito la transferencia óptica ($totalTramas fragmentos capturados).",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoPrincipal
            )

            Text(
                text = "Introduce el PIN de 6 dígitos mostrado en la pantalla del dispositivo emisor para autorizar el descifrado autenticado:",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )

            OutlinedTextField(
                value = pinIngresado,
                onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) pinIngresado = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 8.sp,
                    color = TextoPrincipal
                ),
                placeholder = {
                    Text(
                        text = "••••••",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        letterSpacing = 8.sp,
                        color = TextoSecundario.copy(alpha = 0.5f)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (pinIngresado.trim().length == 6) {
                            alConfirmarPin(pinIngresado.trim())
                        }
                    }
                ),
                singleLine = true,
                shape = FormaCampo,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorAcento,
                    unfocusedBorderColor = ColorBordeActual,
                    focusedContainerColor = ColorCampoAjustes,
                    unfocusedContainerColor = ColorCampoAjustes
                )
            )

            if (!errorMensaje.isNullOrBlank()) {
                Text(
                    text = errorMensaje,
                    color = Peligro,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
