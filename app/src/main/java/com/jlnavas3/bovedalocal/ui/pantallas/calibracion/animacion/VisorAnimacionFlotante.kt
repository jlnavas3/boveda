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
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.EngranajesBoveda
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.aEngranajesConfig
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

@Composable
fun VisorAnimacionFlotante(
    esEngranajes: Boolean,
    ajustes: AjustesApp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(FormaCampo)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (esEngranajes) {
            EngranajesBoveda(
                abierta = false,
                modifier = Modifier.fillMaxSize(),
                config = ajustes.aEngranajesConfig()
            )
        } else {
            val colorPuertaPersonalizado = if (ajustes.puertaColor.isNotBlank()) {
                parsearColorO(ajustes.puertaColor, Ambar)
            } else null

            PuertaBoveda(
                abierta = false,
                tamano = 160,
                velocidadFactor = ajustes.puertaVelocidad,
                grosorFactor = ajustes.puertaGrosorAnillos,
                colorPersonalizado = colorPuertaPersonalizado
            )
        }
    }
}
