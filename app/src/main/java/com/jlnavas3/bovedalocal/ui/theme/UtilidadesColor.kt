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

/**
 * Retorna el fondo translúcido óptimo para contenedores de íconos badges según el tema activo:
 * 0.14f en tema oscuro y 0.10f en tema claro.
 */
fun fondoBadgeParaTema(color: Color, esOscuro: Boolean = esOscuroActivo): Color {
    val colorAjustado = colorLegibleParaTema(color, esOscuro)
    return colorAjustado.copy(alpha = if (esOscuro) 0.14f else 0.10f)
}

/**
 * Parsea un código hexadecimal (#RRGGBB o #AARRGGBB) a [Color] con valor por defecto seguro ante errores.
 */
fun parsearColorO(hex: String, porDefecto: Color): Color {
    if (hex.isBlank()) return porDefecto
    return try {
        val limpio = hex.removePrefix("#").trim()
        val valorLong = limpio.toLong(16)
        when (limpio.length) {
            6 -> Color((0xFF000000 or valorLong).toInt())
            8 -> Color(valorLong)
            else -> porDefecto
        }
    } catch (_: Exception) {
        porDefecto
    }
}

/**
 * Convierte un [Color] Compose a su representación hexadecimal estándar `#RRGGBB`.
 */
fun Color.aHex(): String {
    val r = (red * 255f).toInt().coerceIn(0, 255)
    val g = (green * 255f).toInt().coerceIn(0, 255)
    val b = (blue * 255f).toInt().coerceIn(0, 255)
    return String.format("#%02X%02X%02X", r, g, b)
}

/**
 * Convierte un [Color] a formato `#RRGGBB` o `#AARRGGBB` si tiene transparencia.
 */
fun Color.aHexConAlfa(): String {
    val a = (alpha * 255f).toInt().coerceIn(0, 255)
    val r = (red * 255f).toInt().coerceIn(0, 255)
    val g = (green * 255f).toInt().coerceIn(0, 255)
    val b = (blue * 255f).toInt().coerceIn(0, 255)
    return if (a == 255) {
        String.format("#%02X%02X%02X", r, g, b)
    } else {
        String.format("#%02X%02X%02X%02X", a, r, g, b)
    }
}

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
