/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.jlnavas3.bovedalocal.data.DatosCriptoWallet
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente desacoplado que renderiza los datos de billeteras criptográficas,
 * dirección pública con QR y visor ultra seguro de frases mnemónicas y claves privadas.
 */
@Composable
fun TarjetaDetalleWallet(
    wallet: DatosCriptoWallet,
    vm: VaultViewModel? = null,
    ajustes: AjustesApp? = null,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit,
    alMostrarQr: () -> Unit
) {
    var mostrarSemilla by remember { mutableStateOf(false) }
    var mostrarClavePrivada by remember { mutableStateOf(false) }

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true && ajustes.ocultarContrasena
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = ajustes?.tiempoAutoOcultarSegundos ?: 10

    if (mostrarSemilla) {
        TemporizadorAutoOcultar(
            revelado = mostrarSemilla,
            tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
        ) {
            mostrarSemilla = false
        }
    }

    if (mostrarClavePrivada) {
        TemporizadorAutoOcultar(
            revelado = mostrarClavePrivada,
            tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
        ) {
            mostrarClavePrivada = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Cripto Wallet")

        // 1. Dirección pública y Red Blockchain
        TarjetaDatoDetalle(colorBorde = ColorDatosWeb) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.AccountBalanceWallet,
                            contentDescription = null,
                            tint = TextoSecundario,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Dirección pública",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        if (wallet.red.isNotBlank()) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(FormaPequena)
                                    .background(ColorCampoAjustes)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = wallet.red,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = TextoSecundario
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = wallet.direccionPublica.ifBlank { "—" },
                        style = EstiloMono,
                        color = TextoPrincipal,
                        maxLines = 1,
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (wallet.direccionPublica.isNotBlank()) {
                        BotonIconoDetalle(
                            icono = Icons.Filled.QrCode2,
                            descripcion = "Mostrar código QR de dirección",
                            alPulsar = {
                                haptica.tic()
                                alMostrarQr()
                            }
                        )
                    }
                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "wallet_direccion",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Dirección pública", wallet.direccionPublica, sensible = false)
                            alCopiarCampo("wallet_direccion")
                        }
                    )
                }
            }
        }

        // 2. Frase Semilla (Seed phrase)
        if (wallet.fraseSemilla.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            TarjetaDatoDetalle(colorBorde = ColorDatosPasskey) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = if (seguridadVisualActiva && !mostrarSemilla) Alignment.CenterVertically else Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Frase semilla (Seed phrase)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )

                        Spacer(Modifier.height(2.dp))

                        if (seguridadVisualActiva) {
                            TextoSeguroVisual(
                                texto = wallet.fraseSemilla,
                                oculto = !mostrarSemilla,
                                estilo = estiloOcultamiento,
                                estiloTexto = if (!mostrarSemilla) EstiloMono.copy(letterSpacing = 2.sp) else EstiloMono,
                                colorTexto = if (!mostrarSemilla) TextoSecundario else TextoPrincipal,
                                maxLines = if (!mostrarSemilla) 1 else 6,
                                overflow = TextOverflow.Clip,
                                modifier = if (!mostrarSemilla) Modifier.horizontalScroll(rememberScrollState()) else Modifier
                            )
                        } else {
                            Text(
                                text = wallet.fraseSemilla,
                                style = EstiloMono,
                                color = TextoPrincipal,
                                maxLines = 6,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (seguridadVisualActiva) {
                            BotonIconoDetalle(
                                icono = if (mostrarSemilla) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                descripcion = if (mostrarSemilla) "Ocultar" else "Revelar",
                                alPulsar = {
                                    haptica.toque()
                                    mostrarSemilla = !mostrarSemilla
                                    if (mostrarSemilla) {
                                        Diagnostico.apuntar("seguridad", "Frase semilla de wallet revelada")
                                    }
                                }
                            )
                        }
                        BotonCopiarDetalle(
                            copiado = ultimaCopia == "wallet_semilla",
                            alPulsar = {
                                haptica.exito()
                                vm?.copiar("Frase semilla", wallet.fraseSemilla, sensible = true)
                                alCopiarCampo("wallet_semilla")
                            }
                        )
                    }
                }
            }
        }

        // 3. Clave privada
        if (wallet.clavePrivada.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            TarjetaDatoDetalle(colorBorde = ColorDatosContrasena) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = if (seguridadVisualActiva && !mostrarClavePrivada) Alignment.CenterVertically else Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Clave privada",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )

                        Spacer(Modifier.height(2.dp))

                        if (seguridadVisualActiva) {
                            TextoSeguroVisual(
                                texto = wallet.clavePrivada,
                                oculto = !mostrarClavePrivada,
                                estilo = estiloOcultamiento,
                                estiloTexto = if (!mostrarClavePrivada) EstiloMono.copy(letterSpacing = 2.sp) else EstiloMono,
                                colorTexto = if (!mostrarClavePrivada) TextoSecundario else TextoPrincipal,
                                maxLines = if (!mostrarClavePrivada) 1 else 4,
                                overflow = TextOverflow.Clip,
                                modifier = if (!mostrarClavePrivada) Modifier.horizontalScroll(rememberScrollState()) else Modifier
                            )
                        } else {
                            Text(
                                text = wallet.clavePrivada,
                                style = EstiloMono,
                                color = TextoPrincipal,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (seguridadVisualActiva) {
                            BotonIconoDetalle(
                                icono = if (mostrarClavePrivada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                descripcion = if (mostrarClavePrivada) "Ocultar" else "Revelar",
                                alPulsar = {
                                    haptica.toque()
                                    mostrarClavePrivada = !mostrarClavePrivada
                                    if (mostrarClavePrivada) {
                                        Diagnostico.apuntar("seguridad", "Clave privada de wallet revelada")
                                    }
                                }
                            )
                        }
                        BotonCopiarDetalle(
                            copiado = ultimaCopia == "wallet_clave_privada",
                            alPulsar = {
                                haptica.exito()
                                vm?.copiar("Clave privada", wallet.clavePrivada, sensible = true)
                                alCopiarCampo("wallet_clave_privada")
                            }
                        )
                    }
                }
            }
        }
    }
}
