package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Timer
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
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo

@Composable
fun BarraSuperiorGenerador(
    alVolver: () -> Unit,
    conSeparador: Boolean,
    alIrHistorial: () -> Unit,
    alIrAjustesPortapapeles: () -> Unit,
    modifier: Modifier = Modifier,
    idEtiqueta: String = "04-HER-GEN",
    mostrarId: Boolean = false
) {
    var menuAbierto by remember { mutableStateOf(false) }

    BarraSuperiorPantalla(
        titulo = "Generador",
        idEtiqueta = idEtiqueta,
        mostrarId = mostrarId,
        alVolver = alVolver,
        conSeparador = conSeparador,
        colorFondo = ColorAjustesFondo,
        acciones = {
            // Botón 3 puntos (Más opciones)
            Box {
                com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera(
                    onClick = { menuAbierto = true },
                    icono = Icons.Filled.MoreVert,
                    descripcion = "Más opciones"
                )

                com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false },
                    modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                ) {
                    com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                        texto = "Historial de contraseñas...",
                        icono = Icons.Filled.History,
                        onClick = {
                            menuAbierto = false
                            alIrHistorial()
                        }
                    )
                    com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu()
                    com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                        texto = "Ajustes de portapapeles...",
                        icono = Icons.Filled.Timer,
                        onClick = {
                            menuAbierto = false
                            alIrAjustesPortapapeles()
                        }
                    )
                }
            }
        }
    )
}
