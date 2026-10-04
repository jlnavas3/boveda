package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun BotonPeligro(
    texto: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    alPulsar: () -> Unit
) {
    BotonBorde(
        texto = texto,
        modifier = modifier,
        color = Peligro,
        icono = icono,
        alPulsar = alPulsar
    )
}
