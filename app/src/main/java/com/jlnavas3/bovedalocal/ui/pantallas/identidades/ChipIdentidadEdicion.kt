package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Chip seleccionable individual para vincular una identidad dentro del formulario de edición.
 * Delegado en el componente estandarizado ChipBoveda.
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
        mostrarPunto = mostrarPunto,
        mostrarCheck = mostrarIconoCheck,
        alPulsar = alPulsar,
        modifier = modifier
    )
}

