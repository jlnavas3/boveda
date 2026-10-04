package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.graphics.Color

/** Convierte Color de Compose a HSV (tono 0..360, saturación 0..1, brillo 0..1). */
fun colorAhsv(color: Color): Triple<Float, Float, Float> {
    val r = color.red
    val g = color.green
    val b = color.blue
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min
    val h = when {
        delta == 0f -> 0f
        max == r -> ((g - b) / delta * 60f + 360f) % 360f
        max == g -> ((b - r) / delta * 60f + 120f) % 360f
        else -> ((r - g) / delta * 60f + 240f) % 360f
    }
    val s = if (max == 0f) 0f else delta / max
    val v = max
    return Triple(h, s, v)
}
