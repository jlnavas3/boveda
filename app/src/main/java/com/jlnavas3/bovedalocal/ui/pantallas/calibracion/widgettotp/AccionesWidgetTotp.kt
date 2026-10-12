/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
                vm.restablecerAjustesWidget()
                vm.avisar("Aspecto del widget 2FA restablecido")
            }
        )
    }
}
