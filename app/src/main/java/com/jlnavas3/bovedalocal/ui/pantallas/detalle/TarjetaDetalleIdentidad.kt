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
import androidx.compose.material.icons.filled.Badge
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
import com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente desacoplado que renderiza documentos oficiales de identidad nacional
 * o pasaportes con insignias de tipo oficial, vigencia y país emisor, respetando
 * el formato de fecha configurado en los ajustes.
 */
@Composable
fun TarjetaDetalleIdentidad(
    identidad: DatosDocumentoIdentidad,
    vm: VaultViewModel? = null,
    ajustes: AjustesApp? = null,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit
) {
    var mostrarNumero by remember { mutableStateOf(false) }

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true && ajustes.ocultarUsuario
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

    val expedicionFormateada = remember(identidad.expedicion, ajustes?.formatoFecha) {
        if (ajustes != null) FormateadorCampos.normalizarYFormatearFecha(identidad.expedicion, ajustes.formatoFecha)
        else identidad.expedicion
    }
    val caducidadFormateada = remember(identidad.caducidad, ajustes?.formatoFecha) {
        if (ajustes != null) FormateadorCampos.normalizarYFormatearFecha(identidad.caducidad, ajustes.formatoFecha)
        else identidad.caducidad
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Documento de identidad")

        // 1. Número y tipo de documento
        TarjetaDatoDetalle(colorBorde = ColorAcento) {
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
                            imageVector = Icons.Filled.Badge,
                            contentDescription = null,
                            tint = TextoSecundario,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Número de documento",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        if (identidad.tipoDocumento.isNotBlank()) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(FormaPequena)
                                    .background(ColorCampoAjustes)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = identidad.tipoDocumento,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                                    color = TextoSecundario
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(2.dp))

                    if (seguridadVisualActiva) {
                        TextoSeguroVisual(
                            texto = identidad.numero,
                            oculto = !mostrarNumero,
                            estilo = estiloOcultamiento,
                            estiloTexto = if (!mostrarNumero) EstiloMono.copy(letterSpacing = 2.sp) else EstiloMono,
                            colorTexto = if (!mostrarNumero) TextoSecundario else TextoPrincipal,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    } else {
                        Text(
                            text = identidad.numero.ifBlank { "—" },
                            style = EstiloMono,
                            color = TextoPrincipal,
                            maxLines = 1,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (seguridadVisualActiva && identidad.numero.isNotBlank()) {
                        BotonIconoDetalle(
                            icono = if (mostrarNumero) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            descripcion = if (mostrarNumero) "Ocultar" else "Revelar",
                            alPulsar = {
                                haptica.toque()
                                mostrarNumero = !mostrarNumero
                                if (mostrarNumero) {
                                    Diagnostico.apuntar("seguridad", "Número de documento revelado")
                                }
                            }
                        )
                    }
                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "identidad_numero",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar(identidad.tipoDocumento.ifBlank { "Número de documento" }, identidad.numero, sensible = true)
                            alCopiarCampo("identidad_numero")
                        }
                    )
                }
            }
        }

        // 2. Nombre completo / Titular
        if (identidad.nombreCompleto.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            TarjetaDatoDetalle(colorBorde = ColorDatosUsuario) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Titular del documento",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )

                        Spacer(Modifier.height(2.dp))

                        Text(
                            text = identidad.nombreCompleto,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextoPrincipal,
                            maxLines = 1,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }

                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "identidad_titular",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Titular", identidad.nombreCompleto, sensible = false)
                            alCopiarCampo("identidad_titular")
                        }
                    )
                }
            }
        }

        // 3. Expedición y Caducidad
        if (identidad.expedicion.isNotBlank() || identidad.caducidad.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(EspaciadoDetalle)
            ) {
                if (identidad.expedicion.isNotBlank()) {
                    TarjetaDatoDetalle(
                        colorBorde = ColorDatos2FA,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Expedición",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = TextoSecundario
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = expedicionFormateada,
                                style = EstiloMono,
                                color = TextoPrincipal,
                                maxLines = 1,
                                modifier = Modifier.horizontalScroll(rememberScrollState())
                            )
                        }
                    }
                }

                if (identidad.caducidad.isNotBlank()) {
                    TarjetaDatoDetalle(
                        colorBorde = ColorDatos2FA,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Caducidad",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = TextoSecundario
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = caducidadFormateada,
                                style = EstiloMono,
                                color = TextoPrincipal,
                                maxLines = 1,
                                modifier = Modifier.horizontalScroll(rememberScrollState())
                            )
                        }
                    }
                }
            }
        }

        // 4. País emisor
        if (identidad.paisEmisor.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            TarjetaDatoDetalle(colorBorde = ColorDatosWeb) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "País emisor",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )

                        Spacer(Modifier.height(2.dp))

                        Text(
                            text = identidad.paisEmisor,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextoPrincipal,
                            maxLines = 1,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }
                }
            }
        }
    }
}
