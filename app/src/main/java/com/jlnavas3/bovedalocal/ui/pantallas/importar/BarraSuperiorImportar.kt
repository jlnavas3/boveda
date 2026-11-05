/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Barra superior de la pantalla de importación selectiva con menú de 3 puntos.
 */
@Composable
fun BarraSuperiorImportar(
    subtitulo: String,
    alVolver: () -> Unit,
    modoDetallado: Boolean,
    alAlternarModoDetallado: () -> Unit,
    alSeleccionarTodo: () -> Unit,
    alDeseleccionarTodo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuAbierto by remember { mutableStateOf(false) }

    BarraSuperiorPantalla(
        titulo = "Importar respaldo",
        subtitulo = subtitulo,
        alVolver = alVolver,
        colorFondo = ColorAjustesFondo,
        estiloTitulo = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        acciones = {
            Box {
                BotonIconoCabecera(
                    onClick = { menuAbierto = true },
                    icono = Icons.Filled.MoreVert,
                    descripcion = "Más opciones de importación",
                    tint = ColorIconosInternos
                )

                MenuOpcionesImportar(
                    expandido = menuAbierto,
                    alCerrar = { menuAbierto = false },
                    modoDetallado = modoDetallado,
                    alAlternarModoDetallado = alAlternarModoDetallado,
                    alSeleccionarTodo = alSeleccionarTodo,
                    alDeseleccionarTodo = alDeseleccionarTodo
                )
            }
        },
        modifier = modifier
    )
}
