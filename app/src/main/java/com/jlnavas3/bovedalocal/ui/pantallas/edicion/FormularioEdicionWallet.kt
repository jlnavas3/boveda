/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosCriptoWallet
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb

/**
 * Microcomponente desacoplado para edición directa y fuertemente tipada de billeteras criptográficas.
 */
@Composable
fun FormularioEdicionWallet(
    datos: DatosCriptoWallet,
    alCambiar: (DatosCriptoWallet) -> Unit,
    ajustes: AjustesApp = AjustesApp()
) {
    val ocultarPorDefecto = ajustes.seguridadVisualActiva && ajustes.ocultarContrasena
    var mostrarSemilla by remember(ajustes.seguridadVisualActiva, ajustes.ocultarContrasena) { mutableStateOf(!ocultarPorDefecto) }
    var mostrarClavePrivada by remember(ajustes.seguridadVisualActiva, ajustes.ocultarContrasena) { mutableStateOf(!ocultarPorDefecto) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoBoveda(
            valor = datos.red,
            etiqueta = "Red / Blockchain (ej. Ethereum, Bitcoin, Solana)",
            alCambiar = {
                alCambiar(datos.copy(red = it))
            },
            colorBordeIzquierdo = ColorDatosWeb
        )

        CampoBoveda(
            valor = datos.direccionPublica,
            etiqueta = "Dirección pública (clave pública)",
            alCambiar = {
                alCambiar(datos.copy(direccionPublica = it))
            },
            monoespaciada = true,
            colorBordeIzquierdo = ColorDatosWeb
        )

        CampoBoveda(
            valor = datos.fraseSemilla,
            etiqueta = "Frase semilla (12 o 24 palabras mnemónicas)",
            alCambiar = {
                alCambiar(datos.copy(fraseSemilla = it))
            },
            esContrasena = true,
            mostrarContrasena = mostrarSemilla,
            alAlternarMostrarContrasena = { mostrarSemilla = !mostrarSemilla },
            monoespaciada = true,
            varias = true,
            colorBordeIzquierdo = ColorDatosPasskey
        )

        CampoBoveda(
            valor = datos.clavePrivada,
            etiqueta = "Clave privada (Private key)",
            alCambiar = {
                alCambiar(datos.copy(clavePrivada = it))
            },
            esContrasena = true,
            mostrarContrasena = mostrarClavePrivada,
            alAlternarMostrarContrasena = { mostrarClavePrivada = !mostrarClavePrivada },
            monoespaciada = true,
            varias = true,
            colorBordeIzquierdo = ColorDatosContrasena
        )
    }
}
