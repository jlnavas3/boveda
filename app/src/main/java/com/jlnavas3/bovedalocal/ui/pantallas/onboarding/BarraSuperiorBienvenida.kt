package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Shield
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Cabecera minimalista superior para el paso de bienvenida de onboarding
 * con menú contextual de opciones (Privacidad, Acerca de, Salir).
 */
@Composable
fun BarraSuperiorBienvenida(
    alAbrirGarantias: () -> Unit,
    alAbrirAcercaDe: () -> Unit,
    alSalir: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuAbierto by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            BotonIconoCabecera(
                onClick = { menuAbierto = true },
                icono = Icons.Default.MoreVert,
                descripcion = "Más opciones"
            )

            MenuDesplegableBoveda(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false },
                modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
            ) {
                ElementoMenuCompacto(
                    texto = "Privacidad y cifrado",
                    icono = Icons.Filled.Shield,
                    colorIcono = ColorIconosInternos,
                    colorTexto = TextoPrincipal,
                    onClick = {
                        menuAbierto = false
                        alAbrirGarantias()
                    }
                )

                ElementoMenuCompacto(
                    texto = "Acerca de",
                    icono = Icons.Filled.Info,
                    colorIcono = ColorIconosInternos,
                    colorTexto = TextoPrincipal,
                    onClick = {
                        menuAbierto = false
                        alAbrirAcercaDe()
                    }
                )

                SeparadorOpcionMenu()
                Spacer(Modifier.height(42.dp))

                ElementoMenuCompacto(
                    texto = "Salir",
                    icono = Icons.AutoMirrored.Filled.ExitToApp,
                    colorIcono = Peligro,
                    colorTexto = Peligro,
                    onClick = {
                        menuAbierto = false
                        alSalir()
                    }
                )
            }
        }
    }
}
