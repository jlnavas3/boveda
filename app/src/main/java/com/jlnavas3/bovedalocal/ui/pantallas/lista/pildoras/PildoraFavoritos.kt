package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val ColorEstrellaFavoritos = Color(0xFFF59E0B)

/**
 * Píldora ultra-compacta para el filtro de favoritos activo.
 * Solo muestra el icono de estrella con la cruz de descarte [ ★ ✕ ].
 */
@Composable
fun PildoraFavoritos(
    activo: Boolean,
    alAlternarFavoritos: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!activo) return

    PildoraFiltroBase(
        icono = Icons.Filled.Star,
        texto = null,
        activo = true,
        colorAcento = ColorEstrellaFavoritos,
        alPulsar = alAlternarFavoritos,
        alLimpiar = alAlternarFavoritos,
        modifier = modifier
    )
}
