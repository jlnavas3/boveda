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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaCredencialesDetalle(
    entrada: Entrada,
    revelada: Boolean,
    ultimaCopia: String?,
    alAlternarRevelada: () -> Unit,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit,
    ajustes: AjustesApp? = null
) {
    val tieneCredenciales = entrada.usuario.isNotBlank() || entrada.contrasena.isNotBlank()
    if (!tieneCredenciales) return

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = if (seguridadVisualActiva) ajustes.tiempoAutoOcultarSegundos else 0

    // Estado y temporizador para usuario
    val protegerUsuario = seguridadVisualActiva && ajustes.ocultarUsuario
    var usuarioRevelado by remember(entrada.id, protegerUsuario) { mutableStateOf(!protegerUsuario) }

    TemporizadorAutoOcultar(
        revelado = usuarioRevelado && protegerUsuario,
        tiempoSegundos = tiempoAutoOcultar,
        alAutoOcultar = { usuarioRevelado = false }
    )

    // Temporizador para contraseña
    TemporizadorAutoOcultar(
        revelado = revelada && seguridadVisualActiva,
        tiempoSegundos = tiempoAutoOcultar,
        alAutoOcultar = { if (revelada) alAlternarRevelada() }
    )

    val esContacto = entrada.tipo == com.jlnavas3.bovedalocal.data.TipoEntrada.CONTACTO
    val etiquetaUsuario = if (esContacto) "Teléfono principal" else "Usuario o correo"
    val usuarioMostrado = remember(entrada.usuario, esContacto, ajustes?.formatoTelefono) {
        if (esContacto && ajustes != null) {
            com.jlnavas3.bovedalocal.util.FormateadorCampos.aplicarMascaraTelefono(entrada.usuario, ajustes.formatoTelefono)
        } else {
            entrada.usuario
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = if (esContacto) "Contacto" else "Credenciales")

        if (entrada.usuario.isNotBlank()) {
            TarjetaDatoDetalle(
                colorBorde = ColorDatosUsuario
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = etiquetaUsuario,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            TextoSeguroVisual(
                                texto = usuarioMostrado,
                                oculto = protegerUsuario && !usuarioRevelado,
                                estilo = estiloOcultamiento,
                                estiloTexto = if (protegerUsuario && !usuarioRevelado) {
                                    EstiloMono.copy(letterSpacing = 2.sp)
                                } else {
                                    MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                                },
                                colorTexto = TextoPrincipal,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                softWrap = false
                            )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (protegerUsuario) {
                            val contexto = androidx.compose.ui.platform.LocalContext.current
                            val haptica = androidx.compose.runtime.remember { com.jlnavas3.bovedalocal.util.Haptica(contexto) }
                            BotonIconoDetalle(
                                icono = if (usuarioRevelado) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                descripcion = if (usuarioRevelado) "Ocultar usuario" else "Mostrar usuario",
                                alPulsar = {
                                    haptica.toque()
                                    usuarioRevelado = !usuarioRevelado
                                }
                            )
                        }
                        BotonCopiarDetalle(copiado = ultimaCopia == "usuario", alPulsar = alCopiarUsuario)
                    }
                }
            }
        }

        if (entrada.usuario.isNotBlank() && entrada.contrasena.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
        }

        if (entrada.contrasena.isNotBlank()) {
            TarjetaDatoDetalle(
                colorBorde = ColorDatosContrasena
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Contraseña · ${entrada.contrasena.length} caracteres",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(4.dp))
                        val mostrarPlano = !seguridadVisualActiva || revelada
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            if (mostrarPlano) {
                                Text(
                                    text = contrasenaColoreada(entrada.contrasena),
                                    style = EstiloMono,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            } else {
                                TextoSeguroVisual(
                                    texto = entrada.contrasena,
                                    oculto = true,
                                    estilo = estiloOcultamiento,
                                    estiloTexto = EstiloMono.copy(letterSpacing = 2.sp),
                                    colorTexto = TextoPrincipal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip,
                                    softWrap = false
                                )
                            }
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (seguridadVisualActiva) {
                            BotonIconoDetalle(
                                icono = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                descripcion = if (revelada) "Ocultar contraseña" else "Mostrar contraseña",
                                alPulsar = alAlternarRevelada
                            )
                        }
                        BotonCopiarDetalle(copiado = ultimaCopia == "contrasena", alPulsar = alCopiarContrasena)
                    }
                }
            }
        }

        if (entrada.fechaCaducidad != null) {
            Spacer(Modifier.height(8.dp))
            InsigniaCaducidadDetalle(fechaCaducidad = entrada.fechaCaducidad)
        }
    }
}
