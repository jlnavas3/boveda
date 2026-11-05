/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosRedWifi
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente desacoplado que renderiza la información de conexión y credenciales Wi-Fi
 * con acceso directo para compartir mediante código QR escaneable por smartphones.
 */
@Composable
fun TarjetaDetalleWifi(
    wifi: DatosRedWifi,
    vm: VaultViewModel? = null,
    ajustes: AjustesApp? = null,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit,
    alMostrarQr: () -> Unit
) {
    var mostrarContrasena by remember { mutableStateOf(false) }

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true && ajustes.ocultarContrasena
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = ajustes?.tiempoAutoOcultarSegundos ?: 10

    if (mostrarContrasena) {
        TemporizadorAutoOcultar(
            revelado = mostrarContrasena,
            tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
        ) {
            mostrarContrasena = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Red Wi-Fi")

        // 1. Nombre de red (SSID) y tipo de seguridad
        TarjetaDatoDetalle(colorBorde = ColorDatosWeb) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Wifi,
                            contentDescription = null,
                            tint = TextoSecundario,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Nombre de red (SSID)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        if (wifi.oculta) {
                            Box(
                                modifier = Modifier
                                    .clip(FormaPequena)
                                    .background(ColorCampoAjustes)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Oculta",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextoSecundario
                                )
                            }
                        }
                        if (wifi.tipoSeguridad.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(FormaPequena)
                                    .background(ColorCampoAjustes)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = wifi.tipoSeguridad,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextoSecundario
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(3.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = wifi.ssid.ifBlank { "—" },
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoPrincipal,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                BotonCopiarDetalle(
                    copiado = ultimaCopia == "wifi_ssid",
                    alPulsar = {
                        haptica.exito()
                        vm?.copiar("Nombre de red (SSID)", wifi.ssid, sensible = false)
                        alCopiarCampo("wifi_ssid")
                    }
                )
            }
        }

        Spacer(Modifier.height(EspaciadoDetalle))

        // 2. Contraseña de la red
        TarjetaDatoDetalle(colorBorde = ColorDatosContrasena) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Contraseña Wi-Fi",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoSecundario
                    )

                    Spacer(Modifier.height(3.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        if (seguridadVisualActiva) {
                            TextoSeguroVisual(
                                texto = wifi.contrasena,
                                oculto = !mostrarContrasena,
                                estilo = estiloOcultamiento,
                                estiloTexto = EstiloMono.copy(letterSpacing = 2.sp),
                                colorTexto = if (!mostrarContrasena) TextoSecundario else TextoPrincipal,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                softWrap = false
                            )
                        } else {
                            Text(
                                text = wifi.contrasena.ifBlank { "—" },
                                style = EstiloMono,
                                color = TextoPrincipal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                Spacer(Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (seguridadVisualActiva && wifi.contrasena.isNotBlank()) {
                        BotonIconoDetalle(
                            icono = if (mostrarContrasena) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            descripcion = if (mostrarContrasena) "Ocultar" else "Revelar",
                            alPulsar = {
                                haptica.toque()
                                mostrarContrasena = !mostrarContrasena
                                if (mostrarContrasena) {
                                    Diagnostico.apuntar("seguridad", "Contraseña Wi-Fi revelada")
                                }
                            }
                        )
                    }
                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "wifi_contrasena",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Contraseña Wi-Fi", wifi.contrasena, sensible = true)
                            alCopiarCampo("wifi_contrasena")
                        }
                    )
                }
            }
        }
    }
}
