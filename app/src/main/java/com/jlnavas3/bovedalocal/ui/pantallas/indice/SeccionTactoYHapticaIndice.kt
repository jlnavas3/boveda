package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionTactoYHapticaIndice(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica
) {
    val anchoTactil = ajustes.indiceAnchoTactilDp
    val tono = ajustes.indiceTonoLetras

    ComponenteGrupo(
        etiqueta = "Tacto, háptica y contraste",
        idGrupo = "03.4.G4",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Sensibilidad de arrastre, vibración y legibilidad"
    ) {
        ComponenteSwitch(
            titulo = "Vibración háptica al deslizar",
            icono = Icons.Filled.Vibration,
            colorIcono = Color(0xFFE91E63),
            activo = ajustes.indiceHaptica,
            idFila = "03.4.8",
            mostrarId = ajustes.mostrarIdsAjustes,
            alCambiar = { haptica.tic(); vm.ajustarIndiceHaptica(it) }
        )

        ComponenteSeparador()
        ComponenteSlider(
            titulo = "Zona táctil de arrastre",
            valor = anchoTactil,
            valorTexto = "${anchoTactil.toInt()} dp",
            rango = 26f..90f,
            idFila = "03.4.9",
            mostrarId = ajustes.mostrarIdsAjustes,
            alCambiar = { vm.ajustarIndiceAnchoTactilDp(it) }
        )

        ComponenteSeparador()
        ComponenteSlider(
            titulo = "Tono y contraste de letras",
            valor = tono,
            valorTexto = "${tono.toInt()}%",
            rango = 10f..100f,
            idFila = "03.4.10",
            mostrarId = ajustes.mostrarIdsAjustes,
            alCambiar = { vm.ajustarIndiceTonoLetras(it) }
        )

        ComponenteSeparador()
        ComponenteSwitch(
            titulo = "Incluir letra Ñ",
            icono = Icons.AutoMirrored.Filled.Sort,
            colorIcono = Color(0xFF3F51B5),
            activo = ajustes.indiceIncluirEnie,
            idFila = "03.4.11",
            mostrarId = ajustes.mostrarIdsAjustes,
            alCambiar = { haptica.tic(); vm.ajustarIndiceIncluirEnie(it) }
        )

        ComponenteSeparador()
        ComponenteBotonFila(
            titulo = "Restablecer grupo",
            alPulsar = {
                vm.ajustarIndiceHaptica(true)
                vm.ajustarIndiceAnchoTactilDp(45f)
                vm.ajustarIndiceTonoLetras(80f)
                vm.ajustarIndiceIncluirEnie(true)
            }
        )
    }
}
