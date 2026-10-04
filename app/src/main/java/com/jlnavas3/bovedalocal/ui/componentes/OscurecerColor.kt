package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.graphics.Color

/**
 * Aplica un factor de escala lumínica para oscurecer o sombrear un color dado.
 */
fun oscurecerColor(color: Color, factor: Float): Color {
    return Color(
        red = (color.red * factor).coerceIn(0f, 1f),
        green = (color.green * factor).coerceIn(0f, 1f),
        blue = (color.blue * factor).coerceIn(0f, 1f),
        alpha = color.alpha
    )
}
