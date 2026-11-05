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
import androidx.compose.material.icons.filled.CreditCard
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
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente desacoplado que renderiza la tarjeta bancaria de alta fidelidad
 * con soporte para enmascaramiento seguro, copiado y temporizador de seguridad visual.
 */
@Composable
fun TarjetaDetalleTarjetaBancaria(
    tarjeta: DatosTarjetaBancaria,
    vm: VaultViewModel? = null,
    ajustes: AjustesApp? = null,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit
) {
    var mostrarNumero by remember { mutableStateOf(false) }
    var mostrarCvv by remember { mutableStateOf(false) }
    var mostrarPin by remember { mutableStateOf(false) }

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true && ajustes.ocultarContrasena
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = ajustes?.tiempoAutoOcultarSegundos ?: 10

    if (mostrarNumero) {
        TemporizadorAutoOcultar(
            revelado = mostrarNumero,
            tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
        ) {
            mostrarNumero = false
        }
    }

    if (mostrarCvv) {
        TemporizadorAutoOcultar(
            revelado = mostrarCvv,
            tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
        ) {
            mostrarCvv = false
        }
    }

    if (mostrarPin) {
        TemporizadorAutoOcultar(
            revelado = mostrarPin,
            tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
        ) {
            mostrarPin = false
        }
    }

    val numeroFormateado = remember(tarjeta.numero, ajustes?.formatoTarjeta) {
        FormateadorCampos.aplicarMascaraTarjeta(tarjeta.numero, ajustes?.formatoTarjeta ?: FormateadorCampos.TARJETA_4X4)
    }
    val vencimientoFormateado = remember(tarjeta.vencimiento, ajustes?.formatoExpiracionTarjeta) {
        if (tarjeta.vencimiento.isBlank()) "—"
        else FormateadorCampos.aplicarMascaraExpiracion(tarjeta.vencimiento, ajustes?.formatoExpiracionTarjeta ?: FormateadorCampos.EXPIRACION_MMAA)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Tarjeta bancaria")

        // 1. Tarjeta principal: Número de tarjeta
        TarjetaDatoDetalle(colorBorde = ColorAcento) {
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
                            imageVector = Icons.Filled.CreditCard,
                            contentDescription = null,
                            tint = TextoSecundario,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Número de tarjeta",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        if (tarjeta.tipo.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(FormaPequena)
                                    .background(ColorCampoAjustes)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = tarjeta.tipo,
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
                        if (seguridadVisualActiva) {
                            TextoSeguroVisual(
                                texto = numeroFormateado,
                                oculto = !mostrarNumero,
                                estilo = estiloOcultamiento,
                                estiloTexto = EstiloMono.copy(letterSpacing = 2.sp),
                                colorTexto = if (!mostrarNumero) TextoSecundario else TextoPrincipal,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                softWrap = false
                            )
                        } else {
                            Text(
                                text = numeroFormateado,
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
                    if (seguridadVisualActiva) {
                        BotonIconoDetalle(
                            icono = if (mostrarNumero) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            descripcion = if (mostrarNumero) "Ocultar" else "Revelar",
                            alPulsar = {
                                haptica.toque()
                                mostrarNumero = !mostrarNumero
                                if (mostrarNumero) {
                                    Diagnostico.apuntar("seguridad", "Número de tarjeta revelado")
                                }
                            }
                        )
                    }
                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "tarjeta_numero",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Número de tarjeta", tarjeta.numero.filter { it.isDigit() }, sensible = true)
                            alCopiarCampo("tarjeta_numero")
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(EspaciadoDetalle))

        // 2. Fila con Vencimiento y CVV
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(EspaciadoDetalle)
        ) {
            // Vencimiento
            TarjetaDatoDetalle(
                colorBorde = ColorDatos2FA,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Vencimiento",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = vencimientoFormateado,
                            style = EstiloMono,
                            color = TextoPrincipal,
                            maxLines = 1
                        )
                    }
                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "tarjeta_vencimiento",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Vencimiento", tarjeta.vencimiento, sensible = false)
                            alCopiarCampo("tarjeta_vencimiento")
                        }
                    )
                }
            }

            // CVV / CVC
            TarjetaDatoDetalle(
                colorBorde = ColorDatosContrasena,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CVV / CVC",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(2.dp))
                        if (seguridadVisualActiva) {
                            TextoSeguroVisual(
                                texto = tarjeta.cvv,
                                oculto = !mostrarCvv,
                                estilo = estiloOcultamiento,
                                estiloTexto = EstiloMono.copy(letterSpacing = 2.sp),
                                colorTexto = if (!mostrarCvv) TextoSecundario else TextoPrincipal,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                softWrap = false
                            )
                        } else {
                            Text(
                                text = tarjeta.cvv.ifBlank { "—" },
                                style = EstiloMono,
                                color = TextoPrincipal,
                                maxLines = 1
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (seguridadVisualActiva && tarjeta.cvv.isNotBlank()) {
                            BotonIconoDetalle(
                                icono = if (mostrarCvv) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                descripcion = if (mostrarCvv) "Ocultar" else "Revelar",
                                alPulsar = {
                                    haptica.toque()
                                    mostrarCvv = !mostrarCvv
                                    if (mostrarCvv) {
                                        Diagnostico.apuntar("seguridad", "CVV revelado")
                                    }
                                }
                            )
                        }
                        BotonCopiarDetalle(
                            copiado = ultimaCopia == "tarjeta_cvv",
                            alPulsar = {
                                haptica.exito()
                                vm?.copiar("CVV", tarjeta.cvv, sensible = true)
                                alCopiarCampo("tarjeta_cvv")
                            }
                        )
                    }
                }
            }
        }

        // 3. Titular de la tarjeta (si está presente)
        if (tarjeta.titular.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            TarjetaDatoDetalle(colorBorde = ColorDatosUsuario) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Titular",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = tarjeta.titular,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextoPrincipal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "tarjeta_titular",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Titular", tarjeta.titular, sensible = false)
                            alCopiarCampo("tarjeta_titular")
                        }
                    )
                }
            }
        }

        // 4. PIN del cajero (si está presente)
        if (tarjeta.pin.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
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
                            text = "PIN de tarjeta",
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
                                    texto = tarjeta.pin,
                                    oculto = !mostrarPin,
                                    estilo = estiloOcultamiento,
                                    estiloTexto = EstiloMono.copy(letterSpacing = 2.sp),
                                    colorTexto = if (!mostrarPin) TextoSecundario else TextoPrincipal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip,
                                    softWrap = false
                                )
                            } else {
                                Text(
                                    text = tarjeta.pin,
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
                        if (seguridadVisualActiva) {
                            BotonIconoDetalle(
                                icono = if (mostrarPin) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                descripcion = if (mostrarPin) "Ocultar" else "Revelar",
                                alPulsar = {
                                    haptica.toque()
                                    mostrarPin = !mostrarPin
                                    if (mostrarPin) {
                                        Diagnostico.apuntar("seguridad", "PIN revelado")
                                    }
                                }
                            )
                        }
                        BotonCopiarDetalle(
                            copiado = ultimaCopia == "tarjeta_pin",
                            alPulsar = {
                                haptica.exito()
                                vm?.copiar("PIN", tarjeta.pin, sensible = true)
                                alCopiarCampo("tarjeta_pin")
                            }
                        )
                    }
                }
            }
        }
    }
}
