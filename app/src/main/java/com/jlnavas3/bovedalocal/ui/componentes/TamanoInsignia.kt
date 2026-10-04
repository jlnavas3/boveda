package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Escala de dimensiones para contenedores de íconos e insignias.
 */
enum class TamanoInsignia(
    val tamanoCaja: Dp,
    val tamanoIcono: Dp
) {
    PEQUENO(tamanoCaja = 24.dp, tamanoIcono = 14.dp),
    MEDIANO(tamanoCaja = 36.dp, tamanoIcono = 20.dp),
    GRANDE(tamanoCaja = 48.dp, tamanoIcono = 26.dp),
    HERO(tamanoCaja = 100.dp, tamanoIcono = 50.dp)
}
