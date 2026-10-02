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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.AmbarFuerte
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

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
    val colorBase = colorPersonalizado ?: Ambar
    val color by animateColorAsState(
        targetValue = if (colorPersonalizado != null) {
            colorPersonalizado
        } else {
            when {
                segundosRestantes <= 5 -> Peligro
                segundosRestantes <= 10 -> AmbarFuerte
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

@Composable
fun AnilloTotp(
    codigo: String,
    segundosRestantes: Long,
    tamano: Int = 92,
    periodo: Long = Totp.PERIODO_SEGUNDOS
) {
    val objetivo = (segundosRestantes.toFloat() / periodo).coerceIn(0f, 1f)
    // El vaciado va continuo, no a saltos de un segundo. Cuando el ciclo se reinicia
    // (la fraccion sube) el anillo salta al maximo de golpe: rellenarse despacio
    // se veria al reves de lo que pasa.
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
            segundosRestantes <= 10 -> AmbarFuerte
            else -> Ambar
        },
        animationSpec = spring(dampingRatio = 0.7f),
        label = "colorAnillo"
    )
    Box(modifier = Modifier.size(tamano.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(tamano.dp)) {
            // Grosor proporcional: en 26dp un trazo de 7dp se come el círculo.
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
            // Anillo suelto (la lista): dentro va la cuenta atrás, que es lo único
            // que hay que leer. El texto se escala al círculo para que quepa.
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

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TotpComponentesPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {
                IndicadorTotpTarta(segundosRestantes = 25)
                IndicadorTotpTarta(segundosRestantes = 8)
                IndicadorTotpTarta(segundosRestantes = 3)
            }

            AnilloTotp(codigo = "482 910", segundosRestantes = 18, tamano = 92)
        }
    }
}

