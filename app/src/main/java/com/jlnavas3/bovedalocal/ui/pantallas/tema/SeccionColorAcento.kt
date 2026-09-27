package com.jlnavas3.bovedalocal.ui.pantallas.tema

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionColorDinamicoYSistema(
    ajustes: AjustesApp,
    haptica: Haptica,
    reqDinamico: BringIntoViewRequester,
    alAlternarColorDinamico: (Boolean) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Spacer(Modifier.height(14.dp))
        ComponenteGrupo(
            etiqueta = "COLOR DINÁMICO",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = Color(0xFF00897B),
            alRestablecer = { alAlternarColorDinamico(true) },
            idGrupo = "02.1.G3",
            mostrarId = ajustes.mostrarIdsAjustes,
            modifier = Modifier.bringIntoViewRequester(reqDinamico)
        ) {
            ComponenteSwitch(
                titulo = "Material You",
                icono = null,
                activo = ajustes.colorDinamicoSistema,
                idFila = "02.1.7",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    alAlternarColorDinamico(it)
                }
            )
        }
    }
}

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
        idGrupo = "02.1.G4",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = {
            alAjustarColorAcento("ambar")
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
