/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloIndicadores
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena

/**
 * Contenido textual y credenciales detalladas de una fila de entrada duplicada.
 */
@Composable
fun ColumnScope.ContenidoDetalleFilaDuplicada(
    entrada: Entrada,
    esSugerida: Boolean,
    ocultarUsuario: Boolean,
    estiloOcultamiento: String,
    seguridadVisualActiva: Boolean,
    mostrarUsuario: Boolean = false,
    mostrarContrasena: Boolean,
    mostrarTotp: Boolean,
    mostrarIndicadores: Boolean = false,
    alAlternarMostrarUsuario: () -> Unit = {},
    alAlternarMostrarContrasena: () -> Unit,
    alAlternarMostrarTotp: () -> Unit
) {
    // Título de la entrada
    Text(
        text = entrada.titulo.ifBlank { "Sin título" },
        color = ColorTextoAjustes,
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth()
    )

    val esEspecializada = com.jlnavas3.bovedalocal.data.ExtractorClaveDuplicadoNativo.esTipoEspecializado(entrada.tipo)

    if (esEspecializada) {
        FilaDatoNativoDuplicado(entrada = entrada)
        Spacer(Modifier.height(2.dp))
    }

    // Usuario con soporte de revelado y ofuscación (solo si no es especializada o tiene usuario específico)
    if (!esEspecializada || entrada.usuario.isNotBlank()) {
        val usuarioTexto = entrada.usuario.ifBlank { "Sin usuario" }
        if (entrada.usuario.isNotBlank() && ocultarUsuario) {
            com.jlnavas3.bovedalocal.ui.componentes.seguridad.DatoSensibleConRevelado(
                texto = entrada.usuario,
                oculto = !mostrarUsuario,
                estilo = estiloOcultamiento,
                modifier = Modifier.fillMaxWidth(),
                estiloTexto = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                colorTexto = ColorTextoAjustes.copy(alpha = 0.85f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                alAlternarRevelado = alAlternarMostrarUsuario,
                descripcionAccesible = "usuario o correo"
            )
        } else {
            Text(
                text = usuarioTexto,
                color = if (entrada.usuario.isNotBlank()) ColorTextoAjustes.copy(alpha = 0.85f) else ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(2.dp))
    }

    // Contraseña (solo si no es especializada o tiene contraseña)
    if (!esEspecializada || entrada.contrasena.isNotBlank()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (entrada.contrasena.isBlank()) {
                Text(
                    text = "Sin contraseña",
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            } else {
                TextoSeguroVisual(
                    texto = entrada.contrasena,
                    oculto = seguridadVisualActiva && !mostrarContrasena,
                    estilo = estiloOcultamiento,
                    estiloTexto = if (mostrarContrasena || !seguridadVisualActiva) {
                        MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    } else {
                        MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                    },
                    colorTexto = ColorTextoAjustes.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            if (seguridadVisualActiva && entrada.contrasena.isNotBlank()) {
                val contexto = androidx.compose.ui.platform.LocalContext.current
                val haptica = androidx.compose.runtime.remember { com.jlnavas3.bovedalocal.util.Haptica(contexto) }
                IconButton(
                    onClick = {
                        haptica.toque()
                        alAlternarMostrarContrasena()
                    },
                    modifier = Modifier.size(22.dp)
                ) {
                    Icon(
                        imageVector = if (mostrarContrasena) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (mostrarContrasena) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = ColorAjusteGris,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }

    // Passkey si existe
    FilaPasskeyDuplicada(passkey = entrada.passkey)

    // TOTP si existe
    FilaTotpDuplicada(
        entrada = entrada,
        mostrarTotp = mostrarTotp,
        alAlternarMostrarTotp = alAlternarMostrarTotp,
        seguridadVisualActiva = seguridadVisualActiva,
        estiloOcultamiento = estiloOcultamiento
    )

    // URLs
    val urlsLimpias = remember(entrada.urls) { entrada.urls.filter { it.isNotBlank() } }
    if (urlsLimpias.isNotEmpty()) {
        Spacer(Modifier.height(2.dp))
        urlsLimpias.forEachIndexed { index, url ->
            if (index == urlsLimpias.lastIndex && mostrarIndicadores && (EstiloIndicadores == "puntos" || EstiloIndicadores == "iconos")) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = url,
                        color = ColorAjusteGris,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    IndicadorContenidoTarjeta(
                        entrada = entrada,
                        estilo = EstiloIndicadores
                    )
                }
            } else {
                Text(
                    text = url,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    } else if (mostrarIndicadores && (EstiloIndicadores == "puntos" || EstiloIndicadores == "iconos")) {
        Spacer(Modifier.height(3.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IndicadorContenidoTarjeta(
                entrada = entrada,
                estilo = EstiloIndicadores
            )
        }
    }
}
