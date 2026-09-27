package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionResaltadoDeslizar(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica
) {
    ComponenteGrupo(
        etiqueta = "Resaltado al deslizar",
        idGrupo = "03.4.G5",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Destaca visualmente las entradas de la letra activa"
    ) {
        ComponenteSwitch(
            titulo = "Resaltar entradas al deslizar",
            icono = Icons.Filled.Highlight,
            colorIcono = ColorIconosInternos,
            activo = ajustes.indiceResaltarEntradas,
            idFila = "03.4.12",
            mostrarId = ajustes.mostrarIdsAjustes,
            alCambiar = { haptica.tic(); vm.ajustarIndiceResaltarEntradas(it) }
        )

        if (ajustes.indiceResaltarEntradas) {
            ComponenteSeparador()
            ComponenteSwitch(
                titulo = "Resaltar solo la primera entrada",
                icono = Icons.Filled.Visibility,
                colorIcono = Color(0xFF43A047),
                activo = ajustes.indiceResaltarSoloPrimera,
                idFila = "03.4.13",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = { haptica.tic(); vm.ajustarIndiceResaltarSoloPrimera(it) }
            )
        }

        ComponenteSeparador()
        ComponenteBotonFila(
            titulo = "Restablecer grupo",
            alPulsar = {
                vm.ajustarIndiceResaltarEntradas(true)
                vm.ajustarIndiceResaltarSoloPrimera(true)
            }
        )
    }
}
