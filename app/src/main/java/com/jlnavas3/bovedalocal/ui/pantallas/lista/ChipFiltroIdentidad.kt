package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Chip individual para el filtrado rápido por identidad en la cabecera de la lista principal.
 * Delegado en el componente estandarizado ChipBoveda.
 */
@Composable
fun ChipFiltroIdentidad(
    titulo: String,
    conteo: Int,
    seleccionado: Boolean,
    colorBase: Color = ColorAcento,
    mostrarPunto: Boolean = false,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChipBoveda(
        texto = titulo,
        conteo = conteo,
        seleccionado = seleccionado,
        colorBase = colorBase,
        mostrarPunto = mostrarPunto,
        alPulsar = alPulsar,
        modifier = modifier
    )
}

