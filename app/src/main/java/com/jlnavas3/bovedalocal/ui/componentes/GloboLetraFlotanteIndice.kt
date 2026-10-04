package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAcento
import kotlin.math.roundToInt

/**
 * Microcomponente que renderiza el globo flotante aumentado en la cresta de la ola al arrastrar
 * el dedo a lo largo del índice alfabético lateral.
 */
@Composable
fun GloboLetraFlotanteIndice(
    visible: Boolean,
    letra: Char?,
    touchY: Float,
    alturaTotalPx: Float,
    tamanoCirculoDp: Float,
    offsetCirculoDp: Float,
    modifier: Modifier = Modifier
) {
    val densidad = LocalDensity.current

    AnimatedVisibility(
        visible = visible && letra != null,
        enter = fadeIn(animationSpec = tween(90)) + scaleIn(initialScale = 0.5f, animationSpec = tween(90)),
        exit = fadeOut(animationSpec = tween(140)) + scaleOut(targetScale = 0.5f, animationSpec = tween(140)),
        modifier = modifier
            .wrapContentSize(align = Alignment.TopEnd, unbounded = true)
            .offset {
                val diametroPx = with(densidad) { tamanoCirculoDp.dp.toPx() }
                val xPx = with(densidad) { (-offsetCirculoDp).dp.toPx() + diametroPx / 2f }.roundToInt()
                val yPx = (touchY - diametroPx / 2f).coerceIn(0f, (alturaTotalPx - diametroPx).coerceAtLeast(0f)).roundToInt()
                IntOffset(xPx, yPx)
            }
    ) {
        Box(
            modifier = Modifier
                .requiredSize(tamanoCirculoDp.dp)
                .shadow(elevation = 14.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(DegradadoAcento),
            contentAlignment = Alignment.Center
        ) {
            @Suppress("DEPRECATION")
            Text(
                text = (letra ?: ' ').toString(),
                color = ColorSobreAcento,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = (tamanoCirculoDp * 0.48f).sp,
                    lineHeight = (tamanoCirculoDp * 0.48f).sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}
