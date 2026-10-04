package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

@Composable
fun BotonRestablecerItem(
    texto: String = "Restablecer predefinido",
    modifier: Modifier = Modifier,
    alRestaurar: () -> Unit
) {
    ComponenteBotonFila(
        titulo = texto,
        icono = Icons.Filled.RestartAlt,
        colorIcono = ColorIconosInternos,
        alPulsar = alRestaurar,
        modifier = modifier
    )
}
