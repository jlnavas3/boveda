package com.jlnavas3.bovedalocal.ui.componentes

import android.os.SystemClock
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorAcentoFuerte
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

/** Puerta de bóveda: círculos concéntricos que rotan dinámicamente y se abren al desbloquear. */
@Composable
fun PuertaBoveda(
    abierta: Boolean,
    modifier: Modifier = Modifier,
    tamano: Int = 200,
    velocidadFactor: Float = 1.0f,
    grosorFactor: Float = 1.0f,
    colorPersonalizado: Color? = null
) {
    var progreso by remember { mutableFloatStateOf(0f) }
    var tiempoSegundos by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val inicio = SystemClock.uptimeMillis()
        while (isActive) {
            withFrameMillis { frameTime ->
                tiempoSegundos = (frameTime - inicio) / 1000f
            }
        }
    }

    LaunchedEffect(abierta) {
        if (abierta) {
            val inicio = SystemClock.uptimeMillis()
            val duracion = 500f
            while (isActive) {
                withFrameMillis { ahora ->
                    val t = ((ahora - inicio) / duracion).coerceIn(0f, 1f)
                    val factor = 1f - t
                    progreso = 1f - factor * factor * factor
                }
                if (progreso >= 1f) break
            }
        } else {
            progreso = 0f
        }
    }

    Box(modifier = modifier.size(tamano.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(tamano.dp)) {
            val centro = Offset(size.width / 2, size.height / 2)
            val radioBase = size.minDimension / 2
            val p = progreso

            val colorBase = colorPersonalizado ?: ColorAcento
            val colorClaro = if (colorPersonalizado != null) Color.White else Color(0xFFFFD54F)
            val colorSecundario = if (colorPersonalizado != null) colorPersonalizado.copy(alpha = 0.75f) else ColorAcentoFuerte

            // Velocidades angulares individuales continuas en grados por segundo (°/s)
            val velocidadesGrados = listOf(80f * velocidadFactor, -105f * velocidadFactor, 135f * velocidadFactor)
            val barridos = listOf(260f, 220f, 180f)
            val grosores = listOf(7.5.dp.toPx() * grosorFactor, 5.5.dp.toPx() * grosorFactor, 4.dp.toPx() * grosorFactor)

            for (anillo in 0..2) {
                val expansion = p * (anillo + 1) * radioBase * 0.15f
                val radio = radioBase * (0.92f - anillo * 0.22f) + expansion
                if (radio <= 0f) continue

                val direccionGiro = if (anillo % 2 == 0) 1f else -1f
                val anguloBase = (tiempoSegundos * velocidadesGrados[anillo]) + (p * 220f * direccionGiro)
                val alpha = (1f - p).coerceIn(0f, 1f)

                rotate(degrees = anguloBase, pivot = centro) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                colorBase.copy(alpha = alpha),
                                colorSecundario.copy(alpha = alpha * 0.6f),
                                colorClaro.copy(alpha = alpha),
                                colorBase.copy(alpha = alpha)
                            ),
                            center = centro
                        ),
                        startAngle = 0f,
                        sweepAngle = barridos[anillo],
                        useCenter = false,
                        topLeft = Offset(centro.x - radio, centro.y - radio),
                        size = Size(radio * 2, radio * 2),
                        style = Stroke(width = grosores[anillo], cap = StrokeCap.Round)
                    )

                    // Perno o muesca de seguridad en el hueco del anillo para realzar el giro
                    val radioPerno = (3.dp.toPx() - anillo * 0.5f).coerceAtLeast(1.5f) * grosorFactor
                    val anguloPernoRad = Math.toRadians((barridos[anillo] + 50.0))
                    val pernoX = centro.x + radio * cos(anguloPernoRad).toFloat()
                    val pernoY = centro.y + radio * sin(anguloPernoRad).toFloat()
                    drawCircle(
                        color = colorBase.copy(alpha = alpha * 0.8f),
                        radius = radioPerno,
                        center = Offset(pernoX, pernoY)
                    )
                }
            }

            // Núcleo central: Rueda/manivela de la bóveda
            val radioNucleo = radioBase * 0.22f * (1f + p * 0.35f)
            val rotacionNucleo = (tiempoSegundos * 45f * velocidadFactor) + (p * 180f)
            val alphaNucleo = (1f - p * 0.85f).coerceIn(0f, 1f)

            rotate(degrees = rotacionNucleo, pivot = centro) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(colorClaro, colorSecundario),
                        center = centro,
                        radius = radioNucleo
                    ),
                    radius = radioNucleo,
                    center = centro,
                    alpha = alphaNucleo
                )
                drawCircle(
                    color = Color(0xFF1E232E).copy(alpha = alphaNucleo),
                    radius = radioNucleo * 0.52f,
                    center = centro
                )
                for (radioIdx in 0..2) {
                    val radAngulo = Math.toRadians(radioIdx * 120.0)
                    val finX = centro.x + (radioNucleo * 0.88f) * cos(radAngulo).toFloat()
                    val finY = centro.y + (radioNucleo * 0.88f) * sin(radAngulo).toFloat()
                    drawLine(
                        color = ColorAcento.copy(alpha = alphaNucleo),
                        start = centro,
                        end = Offset(finX, finY),
                        strokeWidth = 2.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
