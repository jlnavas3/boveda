package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Adapta dinámicamente un color para garantizar máxima legibilidad y contraste frente al fondo actual:
 * - En tema oscuro: si el color es demasiado oscuro (luminancia < 0.22f), eleva el brillo para que no se pierda.
 * - En tema claro: si el color es muy claro (luminancia > 0.28f), oscurece armónicamente los canales para
 *   evitar tonos pasteles deslavados ("muy claros") frente a superficies blancas.
 */
fun colorLegibleParaTema(color: Color, esOscuro: Boolean = esOscuroActivo): Color {
    val r = color.red
    val g = color.green
    val b = color.blue
    val lum = 0.2126f * r + 0.7152f * g + 0.0722f * b

    return if (esOscuro) {
        if (lum < 0.22f) {
            val factor = ((0.32f - lum) / 0.32f).coerceIn(0f, 1f) * 0.45f
            Color(
                red = (r + (1f - r) * factor).coerceIn(0f, 1f),
                green = (g + (1f - g) * factor).coerceIn(0f, 1f),
                blue = (b + (1f - b) * factor).coerceIn(0f, 1f),
                alpha = color.alpha
            )
        } else {
            color
        }
    } else {
        if (lum > 0.28f) {
            val ratio = (0.26f / lum).coerceIn(0.40f, 0.92f)
            Color(
                red = (r * ratio).coerceIn(0f, 1f),
                green = (g * ratio).coerceIn(0f, 1f),
                blue = (b * ratio).coerceIn(0f, 1f),
                alpha = color.alpha
            )
        } else {
            color
        }
    }
}
