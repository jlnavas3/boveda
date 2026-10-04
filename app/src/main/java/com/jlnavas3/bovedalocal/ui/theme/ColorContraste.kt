package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Calcula un color de contraste accesible (texto claro sobre fondo oscuro o viceversa)
 * basándose en la luminancia relativa WCAG estándar y calibrado al tono frío 215°.
 */
fun colorContraste(fondo: Color): Color {
    val luminancia = 0.2126f * fondo.red + 0.7152f * fondo.green + 0.0722f * fondo.blue
    return if (luminancia > 0.45f) Color(0xFF13171F) else Color(0xFFF0F4F8)
}
