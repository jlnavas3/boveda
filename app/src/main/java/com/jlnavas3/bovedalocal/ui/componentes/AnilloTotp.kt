package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorAcentoFuerte
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun AnilloTotp(
    codigo: String,
    segundosRestantes: Long,
    tamano: Int = 92,
    periodo: Long = Totp.PERIODO_SEGUNDOS
) {
    val objetivo = (segundosRestantes.toFloat() / periodo).coerceIn(0f, 1f)
    val animada = remember { Animatable(objetivo) }
    LaunchedEffect(objetivo) {
        if (objetivo > animada.value) {
            animada.snapTo(objetivo)
        } else {
            animada.animateTo(objetivo, tween(durationMillis = 1000, easing = LinearEasing))
        }
    }
    val fraccion = animada.value
    val color by animateColorAsState(
        targetValue = when {
            segundosRestantes <= 5 -> Peligro
            segundosRestantes <= 10 -> ColorAcentoFuerte
            else -> ColorAcento
        },
        animationSpec = spring(dampingRatio = 0.7f),
        label = "colorAnillo"
    )
    Box(modifier = Modifier.size(tamano.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(tamano.dp)) {
            val grosor = (tamano * 0.09f).coerceIn(2.5f, 7f).dp.toPx()
            drawArc(
                color = Borde,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(grosor / 2, grosor / 2),
                size = Size(size.width - grosor, size.height - grosor),
                style = Stroke(width = grosor)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * fraccion,
                useCenter = false,
                topLeft = Offset(grosor / 2, grosor / 2),
                size = Size(size.width - grosor, size.height - grosor),
                style = Stroke(width = grosor, cap = StrokeCap.Round)
            )
        }
        if (codigo.isBlank()) {
            Text(
                text = segundosRestantes.toString(),
                fontSize = (tamano * 0.42f).sp,
                fontWeight = FontWeight.Bold,
                color = color,
                lineHeight = (tamano * 0.42f).sp
            )
            return@Box
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                codigo.forEachIndexed { indice, digito ->
                    AnimatedContent(
                        targetState = digito,
                        transitionSpec = {
                            (slideInVertically { alto -> alto } togetherWith slideOutVertically { alto -> -alto })
                        },
                        label = "digito$indice"
                    ) { valor ->
                        Text(text = valor.toString(), style = EstiloMono, color = TextoPrincipal)
                    }
                }
            }
            Text("${segundosRestantes}s", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
        }
    }
}
