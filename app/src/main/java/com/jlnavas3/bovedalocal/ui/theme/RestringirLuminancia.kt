package com.jlnavas3.bovedalocal.ui.theme

/**
 * Restringe la luminosidad a límites accesibles y flexibles según el modo:
 * - Modo Claro: Superficies claras (0.75..1.00), textos oscuros (0.00..0.45).
 * - Modo Oscuro: Superficies oscuras (0.00..0.38), permitiendo desde Negro Puro AMOLED (0%)
 *   hasta gris carbón suave (38%). Textos claros (0.48..1.00), permitiendo blanco puro (100%).
 */
fun restringirLuminancia(
    valor: Float,
    esOscuro: Boolean,
    esSuperficie: Boolean
): Float {
    return if (esOscuro) {
        if (esSuperficie) valor.coerceIn(0.00f, 0.38f) else valor.coerceIn(0.48f, 1.00f)
    } else {
        if (esSuperficie) valor.coerceIn(0.75f, 1.00f) else valor.coerceIn(0.00f, 0.45f)
    }
}

/**
 * Rango útil permitido para el slider en la interfaz según el modo y tipo de capa.
 */
fun rangoLuminanciaParaCapa(esOscuro: Boolean, esSuperficie: Boolean): ClosedFloatingPointRange<Float> {
    return if (esOscuro) {
        if (esSuperficie) 0.00f..0.38f else 0.48f..1.00f
    } else {
        if (esSuperficie) 0.75f..1.00f else 0.00f..0.45f
    }
}
