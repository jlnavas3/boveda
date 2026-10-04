package com.jlnavas3.bovedalocal.ui.componentes.seleccion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente que maneja el botón y menú desplegable de más opciones (3 puntos)
 * en la barra inferior de selección múltiple (Respaldar, Transferir CXF, Eliminar).
 */
@Composable
fun MenuOverflowSeleccion(
    habilitado: Boolean,
    alRespaldar: (() -> Unit)?,
    alTransferirCxf: (() -> Unit)?,
    alBorrar: (() -> Unit)?,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    var menuMasAbierto by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        ItemAccionSeleccion(
            icono = Icons.Filled.MoreVert,
            texto = "Más",
            habilitado = habilitado,
            colorIcono = TextoPrincipal,
            alPulsar = {
                haptica.toque()
                menuMasAbierto = true
            }
        )

        MenuDesplegableBoveda(
            expanded = menuMasAbierto,
            onDismissRequest = { menuMasAbierto = false },
            modifier = Modifier.widthIn(min = 200.dp, max = 260.dp)
        ) {
            if (alRespaldar != null) {
                ElementoMenuCompacto(
                    texto = "Respaldar",
                    icono = Icons.Filled.Backup,
                    colorIcono = ColorAcento,
                    onClick = {
                        menuMasAbierto = false
                        haptica.tic()
                        alRespaldar()
                    }
                )
            }

            if (alTransferirCxf != null) {
                if (alRespaldar != null) SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Transferir",
                    icono = Icons.Filled.VpnKey,
                    colorIcono = ColorPasskeys,
                    onClick = {
                        menuMasAbierto = false
                        haptica.toque()
                        alTransferirCxf()
                    }
                )
            }

            if (alBorrar != null) {
                if (alRespaldar != null || alTransferirCxf != null) SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Eliminar",
                    icono = Icons.Filled.Delete,
                    colorIcono = Peligro,
                    colorTexto = Peligro,
                    onClick = {
                        menuMasAbierto = false
                        haptica.error()
                        alBorrar()
                    }
                )
            }
        }
    }
}
