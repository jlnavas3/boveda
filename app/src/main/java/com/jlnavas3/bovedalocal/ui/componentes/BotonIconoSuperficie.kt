package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Botón cuadrado táctil para micro-acciones secundarias (compartir, copiar, alternar)
 * integrado con respuesta háptica y tokens de curvatura.
 */
@Composable
fun BotonIconoSuperficie(
    icono: ImageVector,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    colorIcono: Color = ColorIconosInternos,
    colorFondo: Color = ColorAcento.copy(alpha = 0.12f),
    fondo: Color? = null,
    tamano: Dp = 36.dp,
    tamanoIcono: Dp = 20.dp,
    descripcion: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val fondoFinal = fondo ?: colorFondo

    Box(
        modifier = modifier
            .size(tamano)
            .clip(FormaPequena)
            .background(fondoFinal)
            .clickable {
                haptica.toque()
                alPulsar()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = colorIcono,
            modifier = Modifier.size(tamanoIcono)
        )
    }
}
