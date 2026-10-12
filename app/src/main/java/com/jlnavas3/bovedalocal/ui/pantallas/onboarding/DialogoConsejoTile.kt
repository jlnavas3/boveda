/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.quicksettings.GeneradorRapidoHelper
import com.jlnavas3.bovedalocal.quicksettings.GeneradorRapidoShortcutHelper
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido
import com.jlnavas3.bovedalocal.widget.WidgetPinHelper

/**
 * Diálogo informativo que orienta al usuario sobre cómo generar una contraseña rápida y segura
 * utilizando el Quick Settings Tile, el Widget 1x1 o el Acceso Directo de escritorio.
 */
@Composable
fun DialogoConsejoTile(alCerrar: () -> Unit) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alCerrar,
        fijarAbajo = false,
        titulo = "Generador Rápido",
        descripcion = "Tile de ajustes rápidos, Widget 1x1 o Acceso Directo",
        icono = Icons.Filled.Lightbulb,
        colorIcono = ColorSobreAcento,
        fondoIcono = ColorAcento,
        mostrarBotonCerrar = false
    ) {
        TextoSubtitulo(
            texto = "Puedes colocar el widget 1x1 «Generador Rápido» o un acceso directo en tu pantalla de inicio, o activar el botón en la barra de ajustes rápidos de Android.\n\nGenerará una clave ultra-segura al instante directamente en el portapapeles para pegarla aquí."
        )

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BotonColorido(
                texto = "Añadir Widget 1x1 al Escritorio",
                icono = Icons.Filled.Widgets,
                color = ColorAcento,
                modifier = Modifier.fillMaxWidth(),
                alPulsar = {
                    haptica.toque()
                    WidgetPinHelper.solicitarColocarWidget(contexto, WidgetGeneradorRapido::class.java)
                }
            )

            BotonBoveda(
                texto = "Añadir Acceso Directo al Escritorio",
                icono = Icons.Filled.TouchApp,
                variante = VarianteBoton.SECUNDARIO,
                modifier = Modifier.fillMaxWidth(),
                alPulsar = {
                    haptica.toque()
                    GeneradorRapidoShortcutHelper.anclarAlEscritorio(contexto)
                }
            )

            BotonBoveda(
                texto = "Añadir a Ajustes Rápidos",
                icono = Icons.Filled.DashboardCustomize,
                variante = VarianteBoton.SECUNDARIO,
                modifier = Modifier.fillMaxWidth(),
                alPulsar = {
                    haptica.toque()
                    GeneradorRapidoHelper.solicitarAgregarTile(contexto)
                }
            )
        }

        Spacer(Modifier.height(14.dp))

        BotonBoveda(
            texto = "Entendido",
            variante = VarianteBoton.SECUNDARIO,
            modifier = Modifier.fillMaxWidth(),
            alPulsar = alCerrar
        )
    }
}
