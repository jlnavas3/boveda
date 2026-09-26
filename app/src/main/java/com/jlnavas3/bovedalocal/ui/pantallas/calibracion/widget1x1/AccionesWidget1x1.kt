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
                vm.ajustarWidget1x1GrosorBorde(0f)
                vm.ajustarWidget1x1CurvaturaEsquinas(15f)
                vm.ajustarWidget1x1TransparenciaFondo(1.0f)
                vm.ajustarWidget1x1Tamano(55f)
                vm.ajustarWidget1x1Ancho(55f)
                vm.ajustarWidget1x1Alto(51f)
                vm.ajustarWidget1x1BloquearProporcion(false)
                vm.ajustarWidget1x1OffsetX(0f)
                vm.ajustarWidget1x1OffsetY(4f)
                vm.ajustarWidget1x1Alineamiento("arriba")
                vm.ajustarWidget1x1ColorBorde("#33332E")
                vm.ajustarWidget1x1ColorIcono("#E6FCFF")
                vm.ajustarWidget1x1ColorFondo("#2E3333")
                vm.avisar("Aspecto del widget 1x1 restablecido")
            }
        )
    }
}
