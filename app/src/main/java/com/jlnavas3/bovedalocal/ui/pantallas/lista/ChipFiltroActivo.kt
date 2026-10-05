package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Chip que representa un filtro activo con botón para removerlo.
 * Estandarizado con el componente molecular ChipBoveda.
 */
@Composable
fun ChipFiltroActivo(
    texto: String,
    alLimpiar: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null
) {
    ChipBoveda(
        texto = texto,
        icono = icono,
        seleccionado = true,
        colorBase = ColorAcento,
        alPulsar = alLimpiar,
        alRemover = alLimpiar,
        modifier = modifier
    )
}

