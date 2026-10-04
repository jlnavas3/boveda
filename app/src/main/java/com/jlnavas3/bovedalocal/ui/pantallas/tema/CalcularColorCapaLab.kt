package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.crearGris
import com.jlnavas3.bovedalocal.ui.theme.restringirLuminancia

fun calcularColorCapaLab(
    tono: Float,
    lum: Float,
    saturacionTinte: Float,
    modoOscuro: Boolean,
    esSuperficie: Boolean
): Color {
    val lumRestringida = restringirLuminancia(lum, modoOscuro, esSuperficie)
    return if (saturacionTinte <= 0.001f) {
        crearGris(lumRestringida)
    } else {
        Color.hsv(tono, saturacionTinte.coerceIn(0f, 1f), lumRestringida)
    }
}
