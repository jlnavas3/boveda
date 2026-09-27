package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
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
fun SeccionCirculoCresta(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica
) {
    val tamanoCirculo = ajustes.indiceTamanoCirculoDp
    val offset = ajustes.indiceOffsetCirculoDp

    ComponenteGrupo(
        etiqueta = "Círculo en la cresta",
        idGrupo = "03.4.G3",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Muestra la letra activa proyectada hacia el centro"
    ) {
        ComponenteSwitch(
            titulo = "Mostrar círculo en cresta",
            icono = Icons.Filled.Circle,
            colorIcono = Color(0xFF00ACC1),
            activo = ajustes.indiceMostrarCirculo,
            idFila = "03.4.5",
            mostrarId = ajustes.mostrarIdsAjustes,
            alCambiar = { haptica.tic(); vm.ajustarIndiceMostrarCirculo(it) }
        )

        if (ajustes.indiceMostrarCirculo) {
            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Tamaño del círculo",
                valor = tamanoCirculo,
                valorTexto = "${tamanoCirculo.toInt()} dp",
                rango = 50f..110f,
                idFila = "03.4.6",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = { vm.ajustarIndiceTamanoCirculoDp(it) }
            )

            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Desplazamiento del círculo",
                valor = offset,
                valorTexto = "${offset.toInt()} dp",
                rango = 50f..160f,
                idFila = "03.4.7",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = { vm.ajustarIndiceOffsetCirculoDp(it) }
            )
        }

        ComponenteSeparador()
        ComponenteBotonFila(
            titulo = "Restablecer grupo",
            alPulsar = {
                vm.ajustarIndiceMostrarCirculo(true)
                vm.ajustarIndiceTamanoCirculoDp(50f)
                vm.ajustarIndiceOffsetCirculoDp(136f)
            }
        )
    }
}
