/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
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
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Barra superior de la pantalla de papelera con búsqueda, selección contextual y menú de opciones.
 */
@Composable
fun BarraSuperiorPapelera(
    tieneElementos: Boolean,
    mostrarId: Boolean,
    alVolver: () -> Unit,
    alAlternarSeleccionarTodo: () -> Unit,
    todoSeleccionado: Boolean = false,
    modoSeleccion: Boolean = false,
    cantidadSeleccionados: Int = 0,
    busquedaVisible: Boolean = false,
    textoBusqueda: String = "",
    alAlternarBusqueda: () -> Unit = {},
    alCambiarTextoBusqueda: (String) -> Unit = {},
    alCerrarBusqueda: () -> Unit = {},
    alIrADisenoLista: () -> Unit,
    alIrAAutodestruccion: () -> Unit,
    alCambiarRetencion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = if (modoSeleccion) {
                "$cantidadSeleccionados ${if (cantidadSeleccionados == 1) "seleccionada" else "seleccionadas"}"
            } else {
                "Papelera"
            },
            idEtiqueta = if (modoSeleccion) null else "03-LST-PAP",
            mostrarId = mostrarId && !modoSeleccion,
            alVolver = alVolver,
            colorFondo = ColorAjustesFondo,
            acciones = {
                if (modoSeleccion) {
                    if (tieneElementos) {
                        BotonIconoCabecera(
                            onClick = alAlternarSeleccionarTodo,
                            icono = if (todoSeleccionado) Icons.Filled.Deselect else Icons.Filled.SelectAll,
                            descripcion = if (todoSeleccionado) "Deseleccionar todo" else "Seleccionar todo",
                            tint = ColorIconosInternos
                        )
                    }
                } else {
                    if (tieneElementos) {
                        BotonIconoCabecera(
                            onClick = alAlternarBusqueda,
                            icono = Icons.Filled.Search,
                            descripcion = "Buscar en papelera",
                            tint = ColorIconosInternos
                        )
                    }
                }

                var menuAbierto by remember { mutableStateOf(false) }
                Box {
                    BotonIconoCabecera(
                        onClick = { menuAbierto = true },
                        icono = Icons.Filled.MoreVert,
                        descripcion = "Más opciones"
                    )

                    MenuDesplegableBoveda(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false },
                        modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                    ) {
                        ElementoMenuCompacto(
                            texto = "Diseño de lista...",
                            icono = Icons.Filled.Layers,
                            colorIcono = ColorIconosInternos,
                            onClick = {
                                menuAbierto = false
                                alIrADisenoLista()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Tiempo de retención...",
                            icono = Icons.Filled.Timer,
                            colorIcono = ColorIconosInternos,
                            onClick = {
                                menuAbierto = false
                                alCambiarRetencion()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Ajustes de autodestrucción...",
                            icono = Icons.Filled.DeleteForever,
                            colorIcono = ColorIconosInternos,
                            onClick = {
                                menuAbierto = false
                                alIrAAutodestruccion()
                            }
                        )
                    }
                }
            }
        )

        // Barra de búsqueda animada
        AnimatedVisibility(
            visible = !modoSeleccion && (busquedaVisible || textoBusqueda.isNotBlank()),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                BarraBusquedaAnimada(
                    valor = textoBusqueda,
                    alCambiar = alCambiarTextoBusqueda,
                    alCerrar = alCerrarBusqueda
                )
            }
        }
    }
}

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun BarraSuperiorPapeleraPreview() {
    com.jlnavas3.bovedalocal.ui.theme.BovedaTheme {
        BarraSuperiorPapelera(
            tieneElementos = true,
            mostrarId = false,
            alVolver = {},
            alAlternarSeleccionarTodo = {},
            todoSeleccionado = false,
            modoSeleccion = false,
            alIrADisenoLista = {},
            alIrAAutodestruccion = {},
            alCambiarRetencion = {}
        )
    }
}
