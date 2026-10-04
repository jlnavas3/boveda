package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorAcentoFuerte

@Composable
fun BarraProgresoForja(modifier: Modifier = Modifier) {
    val transicion = rememberInfiniteTransition(label = "forja")
    val fase by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1_400, easing = LinearEasing)),
        label = "fase"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(8.dp)) {
            drawRoundRect(color = Borde, cornerRadius = CornerRadius(8f, 8f))
            val ancho = size.width * 0.35f
            val x = (size.width + ancho) * fase - ancho
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(Color.Transparent, ColorAcento, ColorAcentoFuerte, Color.Transparent)),
                topLeft = Offset(x, 0f),
                size = Size(ancho, size.height),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
    }
}
