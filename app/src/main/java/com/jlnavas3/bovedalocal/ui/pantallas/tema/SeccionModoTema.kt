package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador

@Composable
fun SeccionModoTema(
    ajustes: AjustesApp,
    alCambiarTema: (String) -> Unit
) {
    ComponenteGrupo(
        etiqueta = "MODO DE TEMA",
        icono = Icons.Filled.BrightnessAuto,
        colorIcono = Color(0xFFFB8C00),
        alRestablecer = { alCambiarTema(AjustesDefaults.Tema.TEMA_APP) },
        idGrupo = "02-APA-THM-G01",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        ComponenteRadio(
            titulo = "Automático (sistema)",
            icono = null,
            seleccionado = ajustes.temaApp == "sistema",
            idFila = "02-APA-THM-AUT",
            mostrarId = ajustes.mostrarIdsAjustes,
            alSeleccionar = {
                alCambiarTema("sistema")
            }
        )
        ComponenteSeparador()
        ComponenteRadio(
            titulo = "Modo claro",
            icono = null,
            seleccionado = ajustes.temaApp == "claro",
            idFila = "02-APA-THM-CLR",
            mostrarId = ajustes.mostrarIdsAjustes,
            alSeleccionar = {
                alCambiarTema("claro")
            }
        )
        ComponenteSeparador()
        ComponenteRadio(
            titulo = "Modo oscuro",
            icono = null,
            seleccionado = ajustes.temaApp == "oscuro",
            idFila = "02-APA-THM-DRK",
            mostrarId = ajustes.mostrarIdsAjustes,
            alSeleccionar = {
                alCambiarTema("oscuro")
            }
        )
    }
}
