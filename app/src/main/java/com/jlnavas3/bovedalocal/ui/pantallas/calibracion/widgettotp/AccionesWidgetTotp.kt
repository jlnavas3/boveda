package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun AccionesWidgetTotp(
    vm: VaultViewModel,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(modifier = modifier) {
        ComponenteBotonFila(
            titulo = "Restablecer aspecto predeterminado",
            icono = Icons.AutoMirrored.Filled.RotateLeft,
            colorIcono = ColorIconosInternos,
            alPulsar = {
                haptica.exito()
                vm.ajustarWidgetGrosorBorde(0f)
                vm.ajustarWidgetCurvaturaEsquinas(0f)
                vm.ajustarWidgetTransparenciaFondo(0.50f)
                vm.ajustarWidgetColorBorde("#FFB300")
                vm.ajustarWidgetColorContador("#FFFFFF")
                vm.ajustarWidgetColorCodigo("#FFB300")
                vm.ajustarWidgetColorTituloIcono("#FFFFFF")
                vm.avisar("Aspecto del widget 2FA restablecido")
            }
        )
    }
}
