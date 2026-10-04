package com.jlnavas3.bovedalocal.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Renderiza el icono de una aplicación instalada con máscara circular recortada
 * y escala ampliada (1.22f) para destacar el logotipo sin bordes excesivos.
 */
@Composable
fun IconoAppCircular(
    bitmap: ImageBitmap,
    descripcion: String,
    tamanoDp: Dp,
    modifier: Modifier = Modifier
) {
    val forma = CircleShape
    Box(
        modifier = modifier
            .size(tamanoDp)
            .clip(forma),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = descripcion,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(1.22f)
        )
        if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(GrosorBorde, ColorBordeActual.copy(alpha = 0.35f), forma)
            )
        }
    }
}
