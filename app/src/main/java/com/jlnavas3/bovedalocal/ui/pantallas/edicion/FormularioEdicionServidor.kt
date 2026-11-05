/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosServidorSsh
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb

/**
 * Microcomponente desacoplado para edición directa y fuertemente tipada de conexiones SSH y servidores.
 */
@Composable
fun FormularioEdicionServidor(
    datos: DatosServidorSsh,
    alCambiar: (DatosServidorSsh) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    val ocultarPorDefecto = ajustes.seguridadVisualActiva && ajustes.ocultarContrasena
    var mostrarClave by remember(ajustes.seguridadVisualActiva, ajustes.ocultarContrasena) { mutableStateOf(!ocultarPorDefecto) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CampoBoveda(
                valor = datos.host,
                etiqueta = "Host o IP",
                alCambiar = {
                    alCambiar(datos.copy(host = it))
                },
                colorBordeIzquierdo = ColorDatosWeb,
                modifier = Modifier.weight(2.2f)
            )

            CampoBoveda(
                valor = datos.puerto,
                etiqueta = "Puerto",
                alCambiar = { raw ->
                    val sanitizado = raw.filter { it.isDigit() }.take(5)
                    alCambiar(datos.copy(puerto = sanitizado))
                },
                colorBordeIzquierdo = ColorDatosWeb,
                tecladoNumerico = true,
                modifier = Modifier.weight(1f)
            )
        }

        CampoBoveda(
            valor = datos.usuario,
            etiqueta = "Usuario SSH (ej. root, ubuntu)",
            alCambiar = {
                alCambiar(datos.copy(usuario = it))
            },
            colorBordeIzquierdo = ColorDatosUsuario
        )

        CampoBoveda(
            valor = datos.clavePrivada,
            etiqueta = "Clave privada SSH o Contraseña",
            alCambiar = {
                alCambiar(datos.copy(clavePrivada = it))
            },
            esContrasena = true,
            mostrarContrasena = mostrarClave,
            alAlternarMostrarContrasena = { mostrarClave = !mostrarClave },
            monoespaciada = true,
            varias = true,
            colorBordeIzquierdo = ColorDatosContrasena
        )

        CampoBoveda(
            valor = datos.clavePublica,
            etiqueta = "Clave pública SSH (opcional)",
            alCambiar = {
                alCambiar(datos.copy(clavePublica = it))
            },
            monoespaciada = true,
            varias = true,
            colorBordeIzquierdo = ColorDatosPasskey
        )

        var mostrarDialogoSsh by remember { mutableStateOf(false) }

        com.jlnavas3.bovedalocal.ui.componentes.BotonBorde(
            texto = "Generar claves SSH (Ed25519 / RSA)",
            icono = Icons.Filled.Dns,
            color = com.jlnavas3.bovedalocal.ui.theme.ColorAcento,
            alPulsar = { mostrarDialogoSsh = true }
        )

        if (mostrarDialogoSsh) {
            DialogoGeneradorClavesSsh(
                alCerrar = { mostrarDialogoSsh = false },
                alAplicar = { privadaPem, publicaSsh ->
                    alCambiar(datos.copy(clavePrivada = privadaPem, clavePublica = publicaSsh))
                    mostrarClave = true
                    mostrarDialogoSsh = false
                }
            )
        }
    }
}
