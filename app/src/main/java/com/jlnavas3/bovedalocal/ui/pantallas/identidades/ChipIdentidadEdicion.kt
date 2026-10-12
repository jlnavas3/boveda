/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Chip seleccionable individual para vincular una identidad dentro del formulario de edición.
 * Delegado en el componente estandarizado ChipBoveda con icono representativo de identidad.
 */
@Composable
fun ChipIdentidadEdicion(
    titulo: String,
    seleccionado: Boolean,
    colorBase: Color = ColorAcento,
    mostrarPunto: Boolean = false,
    mostrarIconoCheck: Boolean = false,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChipBoveda(
        texto = titulo,
        seleccionado = seleccionado,
        colorBase = colorBase,
        icono = Icons.Filled.Person,
        mostrarPunto = mostrarPunto,
        mostrarCheck = mostrarIconoCheck,
        alPulsar = alPulsar,
        modifier = modifier
    )
}

