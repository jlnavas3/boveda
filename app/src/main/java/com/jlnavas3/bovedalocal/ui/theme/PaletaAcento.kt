package com.jlnavas3.bovedalocal.ui.theme

import androidx.compose.ui.graphics.Color

/** Paletas de acento que puede elegir el usuario en Ajustes > Apariencia: optimizadas para contraste en tema oscuro y claro. */
enum class PaletaAcento(
    val clave: String,
    val etiqueta: String,
    val baseOscura: Color,
    val baseClara: Color,
    val fuerteOscuro: Color,
    val fuerteClaro: Color
) {
    MENTA("menta", "Menta", Color(0xFF57E6B4), Color(0xFF0D9488), Color(0xFF23C08D), Color(0xFF0F766E)),
    AZUL("azul", "Azul", Color(0xFF6FA8FF), Color(0xFF2563EB), Color(0xFF3D7EFF), Color(0xFF1D4ED8)),
    ROSA("rosa", "Rosa", Color(0xFFFF8FCB), Color(0xFFDB2777), Color(0xFFFF5FA8), Color(0xFFBE185D)),
    VIOLETA("violeta", "Violeta", Color(0xFFB98BFF), Color(0xFF7C3AED), Color(0xFF8C5CFF), Color(0xFF6D28D9)),
    ROJO("rojo", "Rojo", Color(0xFFF44336), Color(0xFFDC2626), Color(0xFFD32F2F), Color(0xFFB91C1C)),
    PURPURA("purpura", "Púrpura", Color(0xFF9C27B0), Color(0xFF7E22CE), Color(0xFF7B1FA2), Color(0xFF6B21A8)),
    PURPURA_OSCURO("purpura_oscuro", "Púrpura oscuro", Color(0xFF673AB7), Color(0xFF5B21B6), Color(0xFF512DA8), Color(0xFF4C1D95)),
    INDIGO("indigo", "Índigo", Color(0xFF3F51B5), Color(0xFF3730A3), Color(0xFF303F9F), Color(0xFF312E81)),
    CELESTE("celeste", "Celeste", Color(0xFF03A9F4), Color(0xFF0284C7), Color(0xFF0288D1), Color(0xFF0369A1)),
    CIAN("cian", "Cian", Color(0xFF00BCD4), Color(0xFF0891B2), Color(0xFF0097A7), Color(0xFF0E7490)),
    VERDE("verde", "Verde", Color(0xFF4CAF50), Color(0xFF16A34A), Color(0xFF388E3C), Color(0xFF15803D)),
    VERDE_CLARO("verde_claro", "Verde claro", Color(0xFF8BC34A), Color(0xFF4D7C0F), Color(0xFF689F38), Color(0xFF3F6212)),
    LIMA("lima", "Lima", Color(0xFFCDDC39), Color(0xFF4D7C0F), Color(0xFFAFB42B), Color(0xFF3F6212)),
    AMARILLO("amarillo", "Amarillo", Color(0xFFFFEB3B), Color(0xFFB45309), Color(0xFFFBC02D), Color(0xFF92400E)),
    NARANJA("naranja", "Naranja", Color(0xFFFF9800), Color(0xFFC2410C), Color(0xFFF57C00), Color(0xFF9A3412)),
    NARANJA_OSCURO("naranja_oscuro", "Naranja oscuro", Color(0xFFFF5722), Color(0xFFC2410C), Color(0xFFE64A19), Color(0xFF9A3412)),
    MARRON("marron", "Marrón", Color(0xFF8D6E63), Color(0xFF5D4037), Color(0xFF6D4C41), Color(0xFF4E342E)),
    GRIS("gris", "Gris", Color(0xFFB0BEC5), Color(0xFF475569), Color(0xFF90A4AE), Color(0xFF334155)),
    GRIS_AZULADO("gris_azulado", "Gris azulado", Color(0xFF90A4AE), Color(0xFF334155), Color(0xFF78909C), Color(0xFF1E293B));

    val base: Color get() = if (esOscuroActivo) baseOscura else baseClara
    val fuerte: Color get() = if (esOscuroActivo) fuerteOscuro else fuerteClaro

    companion object {
        fun desde(clave: String): PaletaAcento = entries.firstOrNull { it.clave == clave } ?: GRIS
    }
}

fun aplicarPaletaAcento(paleta: PaletaAcento) {
    ColorAcento = paleta.base
    ColorAcentoFuerte = paleta.fuerte
}
