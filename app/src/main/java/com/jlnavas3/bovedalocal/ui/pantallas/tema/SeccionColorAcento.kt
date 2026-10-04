package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.aHex

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionColorAcento(
    ajustes: AjustesApp,
    reqAcento: BringIntoViewRequester,
    alAjustarColorAcento: (String) -> Unit
) {
    ComponenteGrupo(
        etiqueta = "COLOR DE ACENTO",
        icono = Icons.Filled.Palette,
        colorIcono = Color(0xFF8E24AA),
        idGrupo = "02-APA-THM-G05",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = {
            alAjustarColorAcento(AjustesDefaults.Tema.COLOR_ACENTO)
        },
        modifier = Modifier.bringIntoViewRequester(reqAcento)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SelectorColorEnTiempoReal(
                colorInicial = ColorAcento,
                titulo = "Acento"
            ) { nuevoColor ->
                alAjustarColorAcento(nuevoColor.aHex())
            }
        }
    }
}
