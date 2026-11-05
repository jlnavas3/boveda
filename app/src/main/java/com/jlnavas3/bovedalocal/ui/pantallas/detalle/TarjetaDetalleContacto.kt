/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosContacto
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.contactos.BotonAccionCorreo
import com.jlnavas3.bovedalocal.ui.pantallas.contactos.BotonAccionLlamada
import com.jlnavas3.bovedalocal.ui.pantallas.contactos.BotonAccionMapa
import com.jlnavas3.bovedalocal.ui.pantallas.contactos.BotonAccionSms
import com.jlnavas3.bovedalocal.ui.pantallas.contactos.BotonAccionWhatsApp
import com.jlnavas3.bovedalocal.ui.pantallas.contactos.DialogoOpcionesLlamada
import com.jlnavas3.bovedalocal.ui.pantallas.contactos.DialogoRedactarMensajeSeguro
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.comunicacion.GeneradorImagenMensajeEfimero
import com.jlnavas3.bovedalocal.util.comunicacion.LanzadorChatWhatsApp
import com.jlnavas3.bovedalocal.util.comunicacion.LanzadorCorreoElectronico
import com.jlnavas3.bovedalocal.util.comunicacion.LanzadorDireccionMapas
import com.jlnavas3.bovedalocal.util.comunicacion.LanzadorLlamadaTelefonica
import com.jlnavas3.bovedalocal.util.comunicacion.LanzadorMensajeSms

/**
 * Microcomponente desacoplado que renderiza contactos seguros con sus números de teléfono,
 * correo y dirección postal de entrega utilizando los componentes estándar de Bóveda,
 * e integrando la suite de comunicación confidencial (llamadas con número oculto,
 * WhatsApp directo sin agenda, imágenes efímeras y navegación).
 */
@Composable
fun TarjetaDetalleContacto(
    telefonoPrincipal: String,
    contacto: DatosContacto,
    vm: VaultViewModel? = null,
    ajustes: AjustesApp? = null,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit
) {
    val tieneDatos = telefonoPrincipal.isNotBlank() ||
        contacto.telefonoSecundario.isNotBlank() ||
        contacto.correo.isNotBlank() ||
        contacto.tieneDireccion
    if (!tieneDatos) return

    val context = LocalContext.current
    var numeroParaLlamada by remember { mutableStateOf<String?>(null) }
    var numeroParaMensaje by remember { mutableStateOf<String?>(null) }

    val telFormateado = remember(telefonoPrincipal, ajustes?.formatoTelefono) {
        if (ajustes != null && telefonoPrincipal.isNotBlank()) {
            FormateadorCampos.aplicarMascaraTelefono(telefonoPrincipal, ajustes.formatoTelefono)
        } else {
            telefonoPrincipal
        }
    }

    val telSecFormateado = remember(contacto.telefonoSecundario, ajustes?.formatoTelefono) {
        if (ajustes != null && contacto.telefonoSecundario.isNotBlank()) {
            FormateadorCampos.aplicarMascaraTelefono(contacto.telefonoSecundario, ajustes.formatoTelefono)
        } else {
            contacto.telefonoSecundario
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Contacto y dirección")

        // 1. Teléfono principal
        if (telefonoPrincipal.isNotBlank()) {
            TarjetaDatoDetalle(colorBorde = ColorDatosUsuario) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Phone,
                                contentDescription = null,
                                tint = TextoSecundario,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Teléfono principal",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = TextoSecundario
                            )
                        }

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = telFormateado,
                            style = EstiloMono,
                            color = TextoPrincipal,
                            maxLines = 1,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }

                    Spacer(Modifier.width(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BotonAccionLlamada(alPulsar = {
                            haptica.tic()
                            if (ajustes?.habilitarLlamadaOculta == true) {
                                numeroParaLlamada = telefonoPrincipal
                            } else {
                                LanzadorLlamadaTelefonica.llamar(context, telefonoPrincipal)
                            }
                        })

                        if (ajustes?.habilitarWhatsappDirecto != false) {
                            BotonAccionWhatsApp(alPulsar = {
                                haptica.tic()
                                if (ajustes?.modoEnvioMensajeSeguro == "texto") {
                                    LanzadorChatWhatsApp.abrirChat(context, telefonoPrincipal)
                                } else {
                                    numeroParaMensaje = telefonoPrincipal
                                }
                            })
                        }

                        BotonAccionSms(alPulsar = {
                            haptica.tic()
                            LanzadorMensajeSms.enviarSms(context, telefonoPrincipal)
                        })

                        BotonCopiarDetalle(
                            copiado = ultimaCopia == "telefono_principal",
                            alPulsar = {
                                haptica.exito()
                                vm?.copiar("Teléfono principal", telefonoPrincipal, sensible = true)
                                alCopiarCampo("telefono_principal")
                            }
                        )
                    }
                }
            }
        }

        // 2. Teléfono secundario
        if (contacto.telefonoSecundario.isNotBlank()) {
            if (telefonoPrincipal.isNotBlank()) {
                Spacer(Modifier.height(EspaciadoDetalle))
            }
            TarjetaDatoDetalle(colorBorde = ColorDatosUsuario) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Phone,
                                contentDescription = null,
                                tint = TextoSecundario,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Teléfono secundario",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = TextoSecundario
                            )
                        }

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = telSecFormateado,
                            style = EstiloMono,
                            color = TextoPrincipal,
                            maxLines = 1,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }

                    Spacer(Modifier.width(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BotonAccionLlamada(alPulsar = {
                            haptica.tic()
                            if (ajustes?.habilitarLlamadaOculta == true) {
                                numeroParaLlamada = contacto.telefonoSecundario
                            } else {
                                LanzadorLlamadaTelefonica.llamar(context, contacto.telefonoSecundario)
                            }
                        })

                        if (ajustes?.habilitarWhatsappDirecto != false) {
                            BotonAccionWhatsApp(alPulsar = {
                                haptica.tic()
                                if (ajustes?.modoEnvioMensajeSeguro == "texto") {
                                    LanzadorChatWhatsApp.abrirChat(context, contacto.telefonoSecundario)
                                } else {
                                    numeroParaMensaje = contacto.telefonoSecundario
                                }
                            })
                        }

                        BotonAccionSms(alPulsar = {
                            haptica.tic()
                            LanzadorMensajeSms.enviarSms(context, contacto.telefonoSecundario)
                        })

                        BotonCopiarDetalle(
                            copiado = ultimaCopia == "telefono_secundario",
                            alPulsar = {
                                haptica.exito()
                                vm?.copiar("Teléfono secundario", contacto.telefonoSecundario, sensible = true)
                                alCopiarCampo("telefono_secundario")
                            }
                        )
                    }
                }
            }
        }

        // 3. Correo electrónico
        if (contacto.correo.isNotBlank()) {
            if (telefonoPrincipal.isNotBlank() || contacto.telefonoSecundario.isNotBlank()) {
                Spacer(Modifier.height(EspaciadoDetalle))
            }
            TarjetaDatoDetalle(colorBorde = ColorDatosWeb) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = null,
                                tint = TextoSecundario,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Correo electrónico",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = TextoSecundario
                            )
                        }

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = contacto.correo,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextoPrincipal,
                            maxLines = 1,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }

                    Spacer(Modifier.width(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BotonAccionCorreo(alPulsar = {
                            haptica.tic()
                            LanzadorCorreoElectronico.redactar(context, contacto.correo)
                        })

                        BotonCopiarDetalle(
                            copiado = ultimaCopia == "correo_contacto",
                            alPulsar = {
                                haptica.exito()
                                vm?.copiar("Correo", contacto.correo, sensible = true)
                                alCopiarCampo("correo_contacto")
                            }
                        )
                    }
                }
            }
        }

        // 4. Dirección postal de entrega
        if (contacto.tieneDireccion) {
            if (telefonoPrincipal.isNotBlank() || contacto.telefonoSecundario.isNotBlank() || contacto.correo.isNotBlank()) {
                Spacer(Modifier.height(EspaciadoDetalle))
            }
            TarjetaDatoDetalle(colorBorde = ColorAcento) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = TextoSecundario,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Dirección postal de entrega",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = TextoSecundario
                            )
                        }

                        Spacer(Modifier.height(3.dp))

                        if (contacto.direccion.isNotBlank()) {
                            Text(
                                text = contacto.direccion,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                color = TextoPrincipal,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        val detalles = listOf(
                            contacto.codigoPostal.takeIf { it.isNotBlank() }?.let { "CP $it" },
                            contacto.ciudad.takeIf { it.isNotBlank() },
                            contacto.provincia.takeIf { it.isNotBlank() },
                            contacto.pais.takeIf { it.isNotBlank() }
                        ).filterNotNull().joinToString(" • ")

                        if (detalles.isNotBlank()) {
                            if (contacto.direccion.isNotBlank()) {
                                Spacer(Modifier.height(2.dp))
                            }
                            Text(
                                text = detalles,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoSecundario
                            )
                        }
                    }

                    Spacer(Modifier.width(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BotonAccionMapa(alPulsar = {
                            haptica.tic()
                            LanzadorDireccionMapas.abrirMapa(context, contacto.direccionFormateada)
                        })

                        BotonCopiarDetalle(
                            copiado = ultimaCopia == "direccion_contacto",
                            alPulsar = {
                                haptica.exito()
                                vm?.copiar("Dirección", contacto.direccionFormateada, sensible = true)
                                alCopiarCampo("direccion_contacto")
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogos modales de comunicación confidencial
    numeroParaLlamada?.let { tel ->
        val prefijo = ajustes?.prefijoNumeroOculto ?: "#31#"
        DialogoOpcionesLlamada(
            abierto = true,
            alCerrar = { numeroParaLlamada = null },
            numero = tel,
            prefijoOculto = prefijo,
            alLlamarDirecto = {
                LanzadorLlamadaTelefonica.llamar(context, tel)
            },
            alLlamarOculto = {
                LanzadorLlamadaTelefonica.llamar(context, tel, prefijo)
            }
        )
    }

    numeroParaMensaje?.let { tel ->
        DialogoRedactarMensajeSeguro(
            abierto = true,
            alCerrar = { numeroParaMensaje = null },
            numero = tel,
            alEnviarTextoWhatsApp = { msg ->
                LanzadorChatWhatsApp.abrirChat(context, tel, msg)
            },
            alEnviarImagenWhatsApp = { msg ->
                val uri = GeneradorImagenMensajeEfimero.generarImagen(context, msg)
                if (uri != null) {
                    LanzadorChatWhatsApp.enviarImagenEfimera(context, tel, uri)
                }
            },
            alEnviarSms = { msg ->
                LanzadorMensajeSms.enviarSms(context, tel, msg)
            },
            proteccionPantalla = ajustes?.proteccionPantalla ?: true
        )
    }
}
