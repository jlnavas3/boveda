package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Asigna un color distintivo según el identificador de grupo o prefijo de sección.
 */
fun colorParaGrupoId(id: String?): Color {
    if (id.isNullOrBlank()) return ColorAcento
    val matchG = Regex("""G(\d+)""").find(id)
    if (matchG != null) {
        val num = matchG.groupValues[1].toIntOrNull() ?: 1
        return when (num % 6) {
            1 -> Color(0xFFFB8C00)
            2 -> Color(0xFF5C6BC0)
            3 -> Color(0xFF00BFA5)
            4 -> Color(0xFF8E24AA)
            5 -> Color(0xFF43A047)
            0 -> Color(0xFF1E88E5)
            else -> ColorAcento
        }
    }
    val prefijo = id.trimStart().take(2)
    return when (prefijo) {
        "01" -> ColorSeguridad
        "02" -> ColorSalud
        "03" -> Color(0xFF8E24AA)
        "04" -> ColorGenerador
        "05" -> Color(0xFF1E88E5)
        else -> ColorAcento
    }
}
