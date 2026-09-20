package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes

/**
 * Separador con sangría interior para agrupar filas al estilo Samsung One UI / Honor MagicOS.
 * Inicia a la altura del texto (60.dp) para no cortar visualmente el contenedor del icono.
 */
@Composable
fun ComponenteSeparador(
    modifier: Modifier = Modifier,
    sangriaInicio: Dp = 60.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = sangriaInicio)
            .height(0.5.dp)
            .background(ColorSeparadorAjustes)
    )
}
