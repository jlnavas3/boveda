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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.sync.SyncQrPayload
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
 * Diálogo modal para confirmar la sincronización LAN P2P bidireccional con Bóveda Local Desktop.
 *
 * Muestra prominentemente la Huella Visual Determinista de 6 bytes calculada a partir
 * de la clave pública y el token PSK (ej. "74:5A:63:02:1A:A5"), permitiendo al usuario
 * verificar visualmente que no exista ningún ataque Man-in-the-Middle antes de iniciar el túnel.
 */
@Composable
fun DialogoConfirmarSincronizacionP2P(
    payload: SyncQrPayload,
    sincronizando: Boolean,
    errorMensaje: String?,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    val nombreDispositivo = payload.aliasDispositivo?.takeIf { it.isNotBlank() } ?: "Bóveda Local Desktop"
    val primeraIp = payload.ips.firstOrNull() ?: ""

    DialogoBoveda(
        abierto = true,
        alCerrar = { if (!sincronizando) alDescartar() },
        titulo = "¿Sincronizar con $nombreDispositivo?",
        botonConfirmar = {
            if (!sincronizando) {
                BotonColorido(
                    texto = "Sincronizar ahora",
                    icono = Icons.Filled.Sync,
                    color = ColorAcento,
                    alPulsar = alConfirmar
                )
            }
        },
        botonDescartar = {
            if (!sincronizando) {
                TextButton(onClick = alDescartar) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cabecera descriptiva
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaCampo)
                    .background(ColorCampoAjustes)
                    .border(0.8.dp, ColorBordeActual, FormaCampo)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ColorAcento.copy(alpha = 0.15f))
                        .border(1.dp, ColorAcento.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Sync,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = nombreDispositivo,
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = if (primeraIp.isNotEmpty()) "Conexión directa en red local ($primeraIp)" else "Conexión directa en red local (LAN)",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                    )
                }
            }

            // Tarjeta de Huella Visual de Seguridad (Visual Fingerprint)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaCampo)
                    .background(ColorCampoAjustes)
                    .border(1.dp, ColorAcento.copy(alpha = 0.4f), FormaCampo)
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CÓDIGO DE VERIFICACIÓN (HUELLA)",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = payload.huellaVisual,
                    color = ColorAcento,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Comprueba que este mismo código coincida en la pantalla del dispositivo par antes de continuar.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    textAlign = TextAlign.Center
                )
            }

            // Datos de red RFC 1918
            Text(
                text = "IP: ${payload.ips.joinToString(", ")}  •  Puerto: ${payload.puerto}",
                color = TextoSecundario.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Estado de progreso
            if (sincronizando) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaCampo)
                        .background(ColorAcento.copy(alpha = 0.1f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = ColorAcento,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Sincronizando y fusionando datos...",
                        color = ColorAcento,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }

            // Mensaje de error
            if (errorMensaje != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaCampo)
                        .background(Peligro.copy(alpha = 0.1f))
                        .border(1.dp, Peligro.copy(alpha = 0.4f), FormaCampo)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ErrorOutline,
                        contentDescription = null,
                        tint = Peligro,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = errorMensaje,
                        color = Peligro,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                    )
                }
            }
        }
    }
}
