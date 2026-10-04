package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema

@Composable
fun IconoInicioCampoTexto(
    icono: ImageVector,
    colorIcono: Color?,
    esOscuro: Boolean
) {
    Icon(
        imageVector = icono,
        contentDescription = null,
        tint = colorLegibleParaTema(colorIcono ?: ColorAcento, esOscuro),
        modifier = Modifier.size(20.dp)
    )
}
