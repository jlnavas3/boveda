package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun AccionesWidget1x1(
    vm: VaultViewModel,
    haptica: Haptica
) {
    ComponenteGrupo {
        ComponenteBotonFila(
            titulo = "Restablecer aspecto predeterminado",
            icono = Icons.AutoMirrored.Filled.RotateLeft,
            colorIcono = ColorIconosInternos,
            alPulsar = {
                haptica.exito()
                vm.restablecerAjustesWidget1x1()
                vm.avisar("Aspecto del widget 1x1 restablecido")
            }
        )
    }
}
