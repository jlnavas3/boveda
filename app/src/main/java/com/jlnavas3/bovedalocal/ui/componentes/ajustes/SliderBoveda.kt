package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual

/**
 * Control deslizante (Slider) canónico para toda la app con diseño nativo Honor MagicOS / Samsung One UI.
 * Encapsula la paleta de colores del tema y la coherencia visual.
 */
@Composable
fun SliderBoveda(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    colorAcento: Color = Ambar,
    colors: SliderColors? = null,
    onValueChangeFinished: (() -> Unit)? = null
) {
    val coloresEfectivos = colors ?: SliderDefaults.colors(
        thumbColor = colorAcento,
        activeTrackColor = colorAcento,
        inactiveTrackColor = ColorBordeActual.copy(alpha = 0.3f),
        disabledThumbColor = ColorAjusteGris,
        disabledActiveTrackColor = ColorAjusteGris.copy(alpha = 0.4f),
        disabledInactiveTrackColor = ColorAjusteGris.copy(alpha = 0.2f)
    )

    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        colors = coloresEfectivos
    )
}
