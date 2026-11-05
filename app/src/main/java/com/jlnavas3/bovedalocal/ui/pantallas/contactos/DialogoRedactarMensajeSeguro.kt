/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo modal blindado con FLAG_SECURE (antcapturas) para redactar un mensaje confidencial,
 * permitiendo enviarlo como texto directo o renderizado en una imagen efímera (vista única)
 * hacia WhatsApp o por SMS.
 */
@Composable
fun DialogoRedactarMensajeSeguro(
    abierto: Boolean,
    alCerrar: () -> Unit,
    numero: String,
    alEnviarTextoWhatsApp: (String) -> Unit,
    alEnviarImagenWhatsApp: (String) -> Unit,
    alEnviarSms: (String) -> Unit,
    proteccionPantalla: Boolean = true
) {
    if (!abierto) return

    var texto by remember { mutableStateOf("") }
    val formaItem = RoundedCornerShape(CurvaturaEsquinas)
    val modBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        Modifier.border(GrosorBorde, ColorBordeActual, formaItem)
    } else Modifier

    DialogoBoveda(
        abierto = abierto,
        alCerrar = alCerrar,
        titulo = "Mensaje confidencial",
        icono = Icons.Filled.Security,
        colorIcono = ColorAcento,
        properties = DialogProperties(
            securePolicy = if (proteccionPantalla) SecureFlagPolicy.SecureOn else SecureFlagPolicy.SecureOff
        ),
        botonDescartar = {
            BotonTextoBoveda(texto = "Cancelar", alPulsar = alCerrar, tipo = TipoBotonTexto.SECUNDARIO)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Destinatario: $numero",
                style = EstiloMono.copy(fontWeight = FontWeight.Medium),
                color = TextoSecundario
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Contenido o credencial (opcional)") },
                placeholder = { Text("Escribe un mensaje, contraseña o nota...") },
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorAcento,
                    unfocusedBorderColor = ColorSeparadorAjustes,
                    focusedContainerColor = ColorCampoAjustes,
                    unfocusedContainerColor = ColorCampoAjustes,
                    focusedTextColor = TextoPrincipal,
                    unfocusedTextColor = TextoPrincipal
                ),
                shape = formaItem
            )

            Spacer(Modifier.height(16.dp))

            // Opción 1: WhatsApp Texto directo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaItem)
                    .then(modBorde)
                    .background(ColorCampoAjustes)
                    .clickable {
                        alCerrar()
                        alEnviarTextoWhatsApp(texto)
                    }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    tint = Color(0xFF25D366),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "WhatsApp directo",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Abre la conversación sin agregarlo a contactos",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Opción 2: WhatsApp Imagen efímera (Vista única)
            val puedeEnviarImagen = texto.isNotBlank()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaItem)
                    .then(modBorde)
                    .background(if (puedeEnviarImagen) ColorCampoAjustes else ColorCampoAjustes.copy(alpha = 0.5f))
                    .clickable(enabled = puedeEnviarImagen) {
                        alCerrar()
                        alEnviarImagenWhatsApp(texto)
                    }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null,
                    tint = if (puedeEnviarImagen) ColorAcento else ColorSeparadorAjustes,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Imagen efímera (Vista única)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (puedeEnviarImagen) TextoPrincipal else TextoSecundario
                    )
                    Text(
                        text = "Renderiza el texto en imagen segura con marca temporal",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Opción 3: SMS tradicional
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaItem)
                    .then(modBorde)
                    .background(ColorCampoAjustes)
                    .clickable {
                        alCerrar()
                        alEnviarSms(texto)
                    }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Sms,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Enviar por SMS",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Abre el compositor de mensajes estándar",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}
