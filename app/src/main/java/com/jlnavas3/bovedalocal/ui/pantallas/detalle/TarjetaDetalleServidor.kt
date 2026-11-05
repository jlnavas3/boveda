/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

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
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Terminal
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosServidorSsh
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente desacoplado que renderiza la configuración de servidores y llaves SSH
 * con generador directo de comandos de terminal listos para copiar.
 */
@Composable
fun TarjetaDetalleServidor(
    servidor: DatosServidorSsh,
    vm: VaultViewModel? = null,
    ajustes: AjustesApp? = null,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit
) {
    var mostrarClavePrivada by remember { mutableStateOf(false) }

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true && ajustes.ocultarContrasena
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = ajustes?.tiempoAutoOcultarSegundos ?: 10

    if (mostrarClavePrivada) {
        TemporizadorAutoOcultar(
            revelado = mostrarClavePrivada,
            tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
        ) {
            mostrarClavePrivada = false
        }
    }

    val puertoValido = servidor.puerto.ifBlank { "22" }
    val hostValido = servidor.host
    val comandoSsh = remember(servidor) {
        if (hostValido.isBlank()) ""
        else {
            val userPrefix = if (servidor.usuario.isNotBlank()) "${servidor.usuario}@" else ""
            val portSuffix = if (puertoValido != "22") " -p $puertoValido" else ""
            "ssh $userPrefix$hostValido$portSuffix"
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Servidor / SSH")

        // 1. Host y Puerto
        TarjetaDatoDetalle(colorBorde = ColorDatosWeb) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Dns,
                            contentDescription = null,
                            tint = TextoSecundario,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Host / Dirección IP",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                    }

                    Spacer(Modifier.height(3.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        val displayHost = if (hostValido.isNotBlank()) "$hostValido:$puertoValido" else "—"
                        Text(
                            text = displayHost,
                            style = EstiloMono,
                            color = TextoPrincipal,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                if (hostValido.isNotBlank()) {
                    Spacer(Modifier.width(8.dp))
                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "servidor_host",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Host", hostValido, sensible = false)
                            alCopiarCampo("servidor_host")
                        }
                    )
                }
            }
        }

        // 2. Comando SSH rápido
        if (comandoSsh.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            TarjetaDatoDetalle(colorBorde = ColorDatosWeb) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Terminal,
                                contentDescription = null,
                                tint = TextoSecundario,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Comando de conexión SSH",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = TextoSecundario
                            )
                        }

                        Spacer(Modifier.height(3.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = comandoSsh,
                                style = EstiloMono,
                                color = TextoPrincipal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "servidor_comando",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Comando SSH", comandoSsh, sensible = false)
                            alCopiarCampo("servidor_comando")
                        }
                    )
                }
            }
        }

        // 3. Usuario SSH
        if (servidor.usuario.isNotBlank()) {
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
                            text = "Usuario SSH",
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
                                text = servidor.usuario,
                                style = EstiloMono,
                                color = TextoPrincipal,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "servidor_usuario",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Usuario SSH", servidor.usuario, sensible = false)
                            alCopiarCampo("servidor_usuario")
                        }
                    )
                }
            }
        }

        // 4. Clave Privada / Contraseña
        if (servidor.clavePrivada.isNotBlank()) {
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
                            text = "Clave privada / Credencial",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )

                        Spacer(Modifier.height(3.dp))

                        if (seguridadVisualActiva && !mostrarClavePrivada) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                TextoSeguroVisual(
                                    texto = servidor.clavePrivada,
                                    oculto = true,
                                    estilo = estiloOcultamiento,
                                    estiloTexto = EstiloMono.copy(letterSpacing = 2.sp),
                                    colorTexto = TextoSecundario,
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip,
                                    softWrap = false
                                )
                            }
                        } else {
                            Text(
                                text = servidor.clavePrivada,
                                style = EstiloMono,
                                color = TextoPrincipal,
                                maxLines = if (mostrarClavePrivada) 6 else 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (seguridadVisualActiva) {
                            BotonIconoDetalle(
                                icono = if (mostrarClavePrivada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                descripcion = if (mostrarClavePrivada) "Ocultar" else "Revelar",
                                alPulsar = {
                                    haptica.toque()
                                    mostrarClavePrivada = !mostrarClavePrivada
                                    if (mostrarClavePrivada) {
                                        Diagnostico.apuntar("seguridad", "Clave privada SSH revelada")
                                    }
                                }
                            )
                        }
                        BotonCopiarDetalle(
                            copiado = ultimaCopia == "servidor_clave_privada",
                            alPulsar = {
                                haptica.exito()
                                vm?.copiar("Clave privada SSH", servidor.clavePrivada, sensible = true)
                                alCopiarCampo("servidor_clave_privada")
                            }
                        )
                    }
                }
            }
        }

        // 5. Clave pública (si está presente)
        if (servidor.clavePublica.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            TarjetaDatoDetalle(colorBorde = ColorDatosPasskey) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Clave pública",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = servidor.clavePublica,
                            style = EstiloMono,
                            color = TextoPrincipal,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "servidor_clave_publica",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Clave pública SSH", servidor.clavePublica, sensible = false)
                            alCopiarCampo("servidor_clave_publica")
                        }
                    )
                }
            }
        }
    }
}
