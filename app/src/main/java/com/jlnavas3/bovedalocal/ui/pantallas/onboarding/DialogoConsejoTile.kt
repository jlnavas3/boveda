package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.quicksettings.GeneradorRapidoHelper
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Diálogo informativo que orienta al usuario sobre cómo generar una contraseña rápida y segura
 * utilizando el Quick Settings Tile o el Widget 1x1.
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
        descripcion = "Tile de ajustes rápidos o Widget 1x1",
        icono = Icons.Filled.Lightbulb,
        colorIcono = ColorSobreAcento,
        fondoIcono = ColorAcento,
        mostrarBotonCerrar = false
    ) {
        TextoSubtitulo(
            texto = "Puedes deslizar hacia abajo la barra de estado de Android y pulsar el botón «Generador rápido», o colocar el widget 1x1 «Generador Rápido» en tu pantalla de inicio.\n\nGenerará una clave ultra-segura al instante directamente en el portapapeles para pegarla aquí."
        )

        Spacer(Modifier.height(16.dp))

        BotonColorido(
            texto = "Añadir a Ajustes Rápidos",
            icono = Icons.Filled.DashboardCustomize,
            color = ColorAcento,
            modifier = Modifier.fillMaxWidth(),
            alPulsar = {
                haptica.toque()
                GeneradorRapidoHelper.solicitarAgregarTile(contexto)
            }
        )

        Spacer(Modifier.height(12.dp))

        BotonBoveda(
            texto = "Entendido",
            variante = VarianteBoton.SECUNDARIO,
            modifier = Modifier.fillMaxWidth(),
            alPulsar = alCerrar
        )
    }
}
