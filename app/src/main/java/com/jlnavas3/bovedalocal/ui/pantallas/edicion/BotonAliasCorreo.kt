/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.GeneradorAliasCorreo
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Micro-componente desacoplado para generar alias de correo electrónico (Email Masking)
 * junto al campo de usuario/correo en el formulario de edición.
 */
@Composable
fun BotonAliasCorreo(
    usuarioActual: String,
    tituloEntrada: String,
    alSeleccionarAlias: (String) -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    var menuAbierto by remember { mutableStateOf(false) }
    var dialogoPedirCorreo by remember { mutableStateOf(false) }
    var correoBaseIngresado by remember { mutableStateOf("") }

    val tieneCorreo = usuarioActual.contains("@") && usuarioActual.substringAfter("@").contains(".")

    Box(modifier = modifier) {
        IconButton(
            onClick = {
                haptica.toque()
                if (tieneCorreo) {
                    menuAbierto = true
                } else {
                    correoBaseIngresado = usuarioActual.takeIf { it.contains("@") } ?: ""
                    dialogoPedirCorreo = true
                }
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AlternateEmail,
                contentDescription = "Generar alias de correo",
                tint = ColorAcento,
                modifier = Modifier.size(20.dp)
            )
        }

        MenuDesplegableBoveda(
            expanded = menuAbierto,
            onDismissRequest = { menuAbierto = false },
            modifier = Modifier.widthIn(min = 230.dp, max = 290.dp)
        ) {
            val etiquetaSitio = tituloEntrada.ifBlank { "sitio" }
            val aliasSitio = GeneradorAliasCorreo.generarParaSitio(usuarioActual, etiquetaSitio)
            val aliasAleatorio = GeneradorAliasCorreo.generarConAleatorio(usuarioActual)

            ElementoMenuCompacto(
                texto = "Alias para este sitio",
                icono = Icons.Filled.Language,
                colorIcono = ColorAcento,
                onClick = {
                    menuAbierto = false
                    haptica.exito()
                    alSeleccionarAlias(aliasSitio)
                }
            )

            SeparadorOpcionMenu()

            ElementoMenuCompacto(
                texto = "Alias aleatorio seguro",
                icono = Icons.Filled.Casino,
                colorIcono = ColorAcento,
                onClick = {
                    menuAbierto = false
                    haptica.exito()
                    alSeleccionarAlias(aliasAleatorio)
                }
            )

            SeparadorOpcionMenu()

            ElementoMenuCompacto(
                texto = "Cambiar correo base...",
                icono = Icons.Filled.Mail,
                colorIcono = TextoSecundario,
                onClick = {
                    menuAbierto = false
                    correoBaseIngresado = usuarioActual.substringBefore("+") + "@" + usuarioActual.substringAfter("@")
                    dialogoPedirCorreo = true
                }
            )
        }
    }

    if (dialogoPedirCorreo) {
        AlertDialog(
            onDismissRequest = { dialogoPedirCorreo = false },
            shape = FormaTarjeta,
            icon = {
                Icon(
                    imageVector = Icons.Filled.AlternateEmail,
                    contentDescription = null,
                    tint = ColorAcento
                )
            },
            title = {
                Text(
                    text = "Correo electrónico base",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos
                )
            },
            text = {
                androidx.compose.foundation.layout.Column(
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Introduce tu dirección de correo real (Gmail, Outlook, iCloud, etc.) para crear alias con subdireccionamiento:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                    ComponenteCampoTexto(
                        valor = correoBaseIngresado,
                        etiqueta = "Tu correo principal",
                        alCambiar = { correoBaseIngresado = it },
                        placeholder = "ejemplo@gmail.com",
                        mostrarIcono = true,
                        icono = Icons.Filled.Mail,
                        colorBordeIzquierdo = ColorDatosUsuario
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val base = correoBaseIngresado.trim()
                        if (base.contains("@") && base.substringAfter("@").contains(".")) {
                            dialogoPedirCorreo = false
                            val alias = GeneradorAliasCorreo.generarParaSitio(base, tituloEntrada.ifBlank { "sitio" })
                            haptica.exito()
                            alSeleccionarAlias(alias)
                        } else {
                            haptica.error()
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = ColorAcento)
                ) {
                    Text("Generar alias", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogoPedirCorreo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
