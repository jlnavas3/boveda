package com.jlnavas3.bovedalocal.ui.theme

/**
 * Restringe la luminosidad a los límites estrictos de legibilidad según el modo:
 * - Modo Claro: Superficies claras (0.80..1.00), textos oscuros (0.05..0.45).
 * - Modo Oscuro: Superficies oscuras (0.04..0.22), textos claros (0.55..0.98).
 */
fun restringirLuminancia(
    valor: Float,
    esOscuro: Boolean,
    esSuperficie: Boolean
): Float {
    return if (esOscuro) {
        if (esSuperficie) valor.coerceIn(0.04f, 0.22f) else valor.coerceIn(0.55f, 0.98f)
    } else {
        if (esSuperficie) valor.coerceIn(0.80f, 1.00f) else valor.coerceIn(0.05f, 0.45f)
    }
}
