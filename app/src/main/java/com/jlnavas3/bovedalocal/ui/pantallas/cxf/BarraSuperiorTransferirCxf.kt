/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Microcomponente de barra superior para la pantalla de exportación interactiva CXF:
 * - Título y subtítulo con conteo de selección y nombre de la app receptora.
 * - Acciones de cabecera: Búsqueda, Seleccionar todo y Deseleccionar todo.
 * - Despliegue animado del campo de búsqueda.
 */
@Composable
fun BarraSuperiorTransferirCxf(
    gestorReceptor: String,
    totalDisponibles: Int,
    seleccionadasCount: Int,
    busquedaVisible: Boolean,
    textoBusqueda: String,
    mostrarId: Boolean,
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alCambiarTextoBusqueda: (String) -> Unit,
    alCerrarBusqueda: () -> Unit,
    alSeleccionarTodas: () -> Unit,
    alDeseleccionarTodas: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subtitulo = remember(seleccionadasCount, totalDisponibles, gestorReceptor) {
        val base = "$seleccionadasCount de $totalDisponibles"
        if (gestorReceptor.isNotBlank()) "$base · $gestorReceptor" else base
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Transferir credenciales",
            subtitulo = subtitulo,
            idEtiqueta = "05-COP-CXF-EXP",
            mostrarId = mostrarId,
            alVolver = alVolver,
            colorFondo = ColorAjustesFondo,
            estiloTitulo = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            acciones = {
                if (totalDisponibles > 1) {
                    BotonIconoCabecera(
                        onClick = alAlternarBusqueda,
                        icono = if (busquedaVisible) Icons.Filled.Close else Icons.Filled.Search,
                        descripcion = if (busquedaVisible) "Cerrar búsqueda" else "Buscar credenciales",
                        tint = if (busquedaVisible || textoBusqueda.isNotBlank()) ColorAcento else ColorIconosInternos
                    )
                    BotonIconoCabecera(
                        onClick = alSeleccionarTodas,
                        icono = Icons.Filled.SelectAll,
                        descripcion = "Seleccionar todas",
                        tint = if (seleccionadasCount == totalDisponibles && totalDisponibles > 0) ColorAcento else ColorIconosInternos
                    )
                    BotonIconoCabecera(
                        onClick = alDeseleccionarTodas,
                        icono = Icons.Filled.Deselect,
                        descripcion = "Deseleccionar todas",
                        tint = ColorIconosInternos,
                        habilitado = seleccionadasCount > 0
                    )
                }
            }
        )

        AnimatedVisibility(
            visible = busquedaVisible || textoBusqueda.isNotBlank(),
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
