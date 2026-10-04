package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorAcentoFuerte
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Indicador de cuenta atrás circular estilo Google Authenticator:
 * Un círculo relleno tipo "tarta" que se va vaciando de forma continua según transcurren los segundos.
 */
@Composable
fun IndicadorTotpTarta(
    segundosRestantes: Long,
    periodo: Long = Totp.PERIODO_SEGUNDOS,
    tamano: Dp = 14.dp,
    colorPersonalizado: Color? = null,
    modifier: Modifier = Modifier
) {
    val periodoValido = periodo.coerceAtLeast(1L)
    val objetivo = (segundosRestantes.toFloat() / periodoValido).coerceIn(0f, 1f)
    val animada = remember { Animatable(objetivo) }
    LaunchedEffect(objetivo) {
        if (objetivo > animada.value) {
            animada.snapTo(objetivo)
        } else {
            animada.animateTo(objetivo, tween(durationMillis = 1000, easing = LinearEasing))
        }
    }
    val fraccion = animada.value
    val colorBase = colorPersonalizado ?: ColorAcento
    val color by animateColorAsState(
        targetValue = if (colorPersonalizado != null) {
            colorPersonalizado
        } else {
            when {
                segundosRestantes <= 5 -> Peligro
                segundosRestantes <= 10 -> ColorAcentoFuerte
                else -> colorBase
            }
        },
        animationSpec = spring(dampingRatio = 0.7f),
        label = "colorTartaTotp"
    )

    Canvas(modifier = modifier.size(tamano)) {
        val radio = size.minDimension / 2f
        drawCircle(
            color = color.copy(alpha = 0.22f),
            radius = radio
        )
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * fraccion,
            useCenter = true
        )
    }
}
