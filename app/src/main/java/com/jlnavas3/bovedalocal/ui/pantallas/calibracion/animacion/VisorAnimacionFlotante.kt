/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.obtenerPuertaColor
import com.jlnavas3.bovedalocal.data.obtenerPuertaColorDestello
import com.jlnavas3.bovedalocal.ui.componentes.EngranajesBoveda
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.aEngranajesConfig
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaClara
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaOscura
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

@Composable
fun VisorAnimacionFlotante(
    esEngranajes: Boolean,
    ajustes: AjustesApp,
    esOscuro: Boolean = true,
    modifier: Modifier = Modifier
) {
    val paletaSobria = if (esOscuro) {
        paletaSobriaGuardadaOscura ?: PaletaSobriaDefaults.OSCURA
    } else {
        paletaSobriaGuardadaClara ?: PaletaSobriaDefaults.CLARA
    }
    val fondoVisor = paletaSobria.fondo
    val bordeVisor = paletaSobria.borde

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(FormaCampo)
            .background(fondoVisor)
            .then(
                if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, bordeVisor, FormaCampo) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (esEngranajes) {
            EngranajesBoveda(
                abierta = false,
                modifier = Modifier.fillMaxSize(),
                config = ajustes.aEngranajesConfig(esOscuro, fondoVisor)
            )
        } else {
            val hexPuerta = ajustes.obtenerPuertaColor(esOscuro)
            val colorPuertaPersonalizado = if (hexPuerta.isNotBlank()) {
                parsearColorO(hexPuerta, paletaSobria.acento)
            } else null

            val hexDestello = ajustes.obtenerPuertaColorDestello(esOscuro)
            val colorDestelloPersonalizado = if (hexDestello.isNotBlank()) {
                parsearColorO(hexDestello, paletaSobria.acento)
            } else null

            PuertaBoveda(
                abierta = false,
                tamano = 160,
                velocidadFactor = ajustes.puertaVelocidad,
                grosorFactor = ajustes.puertaGrosorAnillos,
                colorPersonalizado = colorPuertaPersonalizado,
                colorDestello = colorDestelloPersonalizado,
                esOscuro = esOscuro,
                colorFondo = fondoVisor
            )
        }
    }
}
