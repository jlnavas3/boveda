package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Barra superior de la pantalla de papelera con acciones de vaciar y menú de opciones rápidas.
 */
@Composable
fun BarraSuperiorPapelera(
    tieneElementos: Boolean,
    mostrarId: Boolean,
    alVolver: () -> Unit,
    alConfirmarVaciar: () -> Unit,
    alIrADisenoLista: () -> Unit,
    alIrAAutodestruccion: () -> Unit
) {
    BarraSuperiorPantalla(
        titulo = "Papelera",
        idEtiqueta = "03-LST-PAP",
        mostrarId = mostrarId,
        alVolver = alVolver,
        colorFondo = ColorAjustesFondo,
        acciones = {
            if (tieneElementos) {
                IconButton(onClick = alConfirmarVaciar) {
                    Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = "Vaciar papelera",
                        tint = Peligro,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            var menuAbierto by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { menuAbierto = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Más opciones",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(22.dp)
                    )
                }

                MenuDesplegableBoveda(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false },
                    modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                ) {
                    ElementoMenuCompacto(
                        texto = "Diseño de lista...",
                        icono = Icons.Filled.Layers,
                        colorIcono = ColorAcento,
                        onClick = {
                            menuAbierto = false
                            alIrADisenoLista()
                        }
                    )
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Ajustes de autodestrucción...",
                        icono = Icons.Filled.Timer,
                        colorIcono = ColorAcento,
                        onClick = {
                            menuAbierto = false
                            alIrAAutodestruccion()
                        }
                    )
                }
            }
        }
    )
}
