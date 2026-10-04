package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Cabecera superior para la pantalla de creación de contraseña ("Forjar la bóveda")
 * con botón de regreso y menú desplegable de opciones (Consejo, Acerca de).
 */
@Composable
fun BarraSuperiorCrearContrasena(
    alVolver: () -> Unit,
    conSeparador: Boolean,
    alAbrirConsejo: () -> Unit,
    alAbrirAcercaDe: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuAbierto by remember { mutableStateOf(false) }

    BarraSuperiorPantalla(
        titulo = "Contraseña",
        alVolver = alVolver,
        conSeparador = conSeparador,
        colorFondo = ColorAjustesFondo,
        acciones = {
            Box {
                BotonIconoCabecera(
                    onClick = { menuAbierto = true },
                    icono = Icons.Default.MoreVert,
                    descripcion = "Más opciones"
                )

                MenuDesplegableBoveda(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false },
                    modifier = Modifier.widthIn(min = 200.dp, max = 260.dp)
                ) {
                    ElementoMenuCompacto(
                        texto = "Consejo",
                        icono = Icons.Filled.Lightbulb,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuAbierto = false
                            alAbrirConsejo()
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
                }
            }
        },
        modifier = modifier
    )
}
