package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Menta

/**
 * Dibuja la máscara oscura semitransparente con una ventana central recortada
 * y 4 esquinas angulares destacadas (estilo viewfinder de cámara profesional).
 *
 * Si [codigoDetectado] es true, las esquinas se iluminan en color Menta/Verde
 * y pulsan sutilmente como confirmación visual de escaneo exitoso.
 */
@Composable
fun VisorMascaraQr(
    codigoDetectado: Boolean,
    modifier: Modifier = Modifier,
    tamanoVentanaDp: Dp = 260.dp,
    desplazamientoVerticalDp: Dp = (-20).dp
) {
    val densidad = LocalDensity.current
    val colorEsquinas by animateColorAsState(
        targetValue = if (codigoDetectado) Menta else ColorAcento,
        animationSpec = tween(durationMillis = 200),
        label = "colorEsquinas"
    )

    val transicionInfinita = rememberInfiniteTransition(label = "pulsoEsquinas")
    val pulsoAlpha by transicionInfinita.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsoAlpha"
    )

    val tamanoVentanaPx = with(densidad) { tamanoVentanaDp.toPx() }
    val despVerticalPx = with(densidad) { desplazamientoVerticalDp.toPx() }
    val longitudBrazoPx = with(densidad) { 32.dp.toPx() }
    val grosorTrazoPx = with(densidad) { 4.5.dp.toPx() }
    val radioEsquinaPx = with(densidad) { 10.dp.toPx() }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val anchoPantalla = size.width
            val altoPantalla = size.height

            val left = (anchoPantalla - tamanoVentanaPx) / 2f
            val top = ((altoPantalla - tamanoVentanaPx) / 2f) + despVerticalPx
            val right = left + tamanoVentanaPx
            val bottom = top + tamanoVentanaPx

            // 1. Fondo oscurecido con recorte central EvenOdd
            val pathMascara = Path().apply {
                fillType = PathFillType.EvenOdd
                // Rectángulo exterior completo
                addRect(Rect(0f, 0f, anchoPantalla, altoPantalla))
                // Ventana central recortada con esquinas suavemente redondeadas
                addRoundRect(
                    RoundRect(
                        rect = Rect(left, top, right, bottom),
                        cornerRadius = CornerRadius(radioEsquinaPx, radioEsquinaPx)
                    )
                )
            }
            drawPath(
                path = pathMascara,
                color = Color.Black.copy(alpha = 0.65f)
            )

            // 2. Trazar las 4 esquinas angulares independientes
            val colorFinal = colorEsquinas.copy(alpha = if (codigoDetectado) 1f else pulsoAlpha)
            val estiloTrazo = Stroke(
                width = grosorTrazoPx,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )

            // Esquina Superior Izquierda ⌜
            val pathTopLeft = Path().apply {
                moveTo(left, top + longitudBrazoPx)
                lineTo(left, top + radioEsquinaPx)
                quadraticTo(left, top, left + radioEsquinaPx, top)
                lineTo(left + longitudBrazoPx, top)
            }
            drawPath(pathTopLeft, colorFinal, style = estiloTrazo)

            // Esquina Superior Derecha ⌝
            val pathTopRight = Path().apply {
                moveTo(right - longitudBrazoPx, top)
                lineTo(right - radioEsquinaPx, top)
                quadraticTo(right, top, right, top + radioEsquinaPx)
                lineTo(right, top + longitudBrazoPx)
            }
            drawPath(pathTopRight, colorFinal, style = estiloTrazo)

            // Esquina Inferior Izquierda ⌞
            val pathBottomLeft = Path().apply {
                moveTo(left, bottom - longitudBrazoPx)
                lineTo(left, bottom - radioEsquinaPx)
                quadraticTo(left, bottom, left + radioEsquinaPx, bottom)
                lineTo(left + longitudBrazoPx, bottom)
            }
            drawPath(pathBottomLeft, colorFinal, style = estiloTrazo)

            // Esquina Inferior Derecha ⌟
            val pathBottomRight = Path().apply {
                moveTo(right - longitudBrazoPx, bottom)
                lineTo(right - radioEsquinaPx, bottom)
                quadraticTo(right, bottom, right, bottom - radioEsquinaPx)
                lineTo(right, bottom - longitudBrazoPx)
            }
            drawPath(pathBottomRight, colorFinal, style = estiloTrazo)
        }

        // 3. Ícono decorativo de expansión en esquina inferior derecha
        val posX = with(densidad) {
            val centroX = (tamanoVentanaDp.value / 2f)
            (centroX - 26).dp
        }
        val posY = with(densidad) {
            val centroY = (tamanoVentanaDp.value / 2f)
            (centroY + desplazamientoVerticalDp.value - 26).dp
        }

        Box(
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.Center)
                .offset(x = posX, y = posY)
        ) {
            Icon(
                imageVector = Icons.Filled.OpenInFull,
                contentDescription = null,
                tint = colorEsquinas.copy(alpha = 0.85f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
