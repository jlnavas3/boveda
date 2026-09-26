package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SelectorTipoWidgetAjustes(
    pestanaWidget: Int,
    mostrarId: Boolean,
    haptica: Haptica,
    vm: VaultViewModel,
    alSeleccionarWidget: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Widgets de escritorio",
        idGrupo = "03.3.G1",
        mostrarId = mostrarId,
        descripcion = "Elige el widget para ajustar su comportamiento y calibrar su aspecto",
        modifier = modifier
    ) {
        ComponenteRadio(
            titulo = "Códigos 2FA",
            icono = Icons.Filled.Timer,
            colorIcono = Color2FA,
            seleccionado = pestanaWidget == 0,
            idFila = "03.3.1",
            mostrarId = mostrarId,
            alSeleccionar = {
                haptica.tic()
                alSeleccionarWidget(0)
            }
        )
        ComponenteSeparador()
        ComponenteRadio(
            titulo = "Generador 1x1",
            icono = Icons.Filled.Key,
            colorIcono = ColorGenerador,
            seleccionado = pestanaWidget == 1,
            idFila = "03.3.2",
            mostrarId = mostrarId,
            alSeleccionar = {
                haptica.tic()
                alSeleccionarWidget(1)
            }
        )
        ComponenteSeparador()
        ComponenteNavegacion(
            titulo = "Calibración",
            icono = Icons.Filled.Tune,
            colorIcono = ColorIconosInternos,
            idFila = "03.3.3",
            mostrarId = mostrarId,
            alPulsar = {
                haptica.tic()
                if (pestanaWidget == 0) {
                    vm.ir(Pantalla.CalibracionWidgetTotp())
                } else {
                    vm.ir(Pantalla.CalibracionWidget1x1())
                }
            }
        )
    }
}
