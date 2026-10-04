package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Retorna el fondo translúcido óptimo para contenedores de íconos badges según el tema activo:
 * 0.14f en tema oscuro y 0.10f en tema claro.
 */
fun fondoBadgeParaTema(color: Color, esOscuro: Boolean = esOscuroActivo): Color {
    val colorAjustado = colorLegibleParaTema(color, esOscuro)
    return colorAjustado.copy(alpha = if (esOscuro) 0.14f else 0.10f)
}
