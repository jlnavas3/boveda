/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Contenedor orquestador que gestiona la fila compacta, el botón de alternancia Más/Menos
 * y el panel expandido verticalmente con todas las opciones.
 */
@Composable
fun ContenedorSugerenciasBusqueda(
    textoBusqueda: String,
    alCambiarTextoBusqueda: (String) -> Unit,
    alAbrirAyuda: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Fila horizontal compacta de sugerencias
        FilaSugerenciasBusqueda(
            textoBusqueda = textoBusqueda,
            sugerencias = ProveedorSugerenciasBusqueda.destacadas,
            alSeleccionarSugerencia = { item ->
                val nuevoTexto = ManejadorInsercionFiltro.alternarFiltro(textoBusqueda, item.comando)
                alCambiarTextoBusqueda(nuevoTexto)
            }
        )

        // Botón "Más filtros" / "Menos" alineado a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, end = 2.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonAlternarExpansionSugerencias(
                expandido = expandido,
                alAlternar = { expandido = !expandido }
            )
        }

        // Panel desplegable con animación hacia abajo
        AnimatedVisibility(
            visible = expandido,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                Spacer(Modifier.height(4.dp))
                PanelSugerenciasExpandido(
                    textoBusqueda = textoBusqueda,
                    alSeleccionarSugerencia = { item ->
                        val nuevoTexto = ManejadorInsercionFiltro.alternarFiltro(textoBusqueda, item.comando)
                        alCambiarTextoBusqueda(nuevoTexto)
                    },
                    alAbrirAyuda = alAbrirAyuda
                )
            }
        }
    }
}
