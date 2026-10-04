package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow

/**
 * Índice alfabético lateral estilo Niagara Launcher:
 * - Detección táctil a lo largo del borde derecho.
 * - Efecto de curvatura suave ("ola") que se adapta al dedo del usuario.
 * - Letras escaladas en tamaño a medida que suben por la pendiente de la ola hasta la cresta.
 * - Globo flotante aumentado proyectado hacia el centro de la pantalla.
 * - Respuesta háptica al cambiar de letra.
 * - Notificación en tiempo real de la letra arrastrada para resaltado de tarjetas.
 */
@Composable
fun IndiceAlfabetico(
    alSeleccionarLetra: (Char) -> Unit,
    modifier: Modifier = Modifier,
    efectoOla: Boolean = true,
    amplitudOlaDp: Float = 110f,
    radioOlaDp: Float = 250f,
    escalaMaximaLetras: Float = 1.6f,
    mostrarCirculo: Boolean = true,
    tamanoCirculoDp: Float = 78f,
    offsetCirculoDp: Float = 145f,
    hapticaActiva: Boolean = true,
    anchoZonaTactilDp: Float = 45f,
    tonoLetras: Float = 55f,
    incluirEnie: Boolean = true,
    alSeleccionarLetraConOffset: ((Char, Float) -> Unit)? = null,
    alCambiarLetraActiva: (Char?) -> Unit = {}
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val hapticaActivaActual by rememberUpdatedState(hapticaActiva)
    val alSeleccionarLetraActual by rememberUpdatedState(alSeleccionarLetra)
    val alSeleccionarLetraConOffsetActual by rememberUpdatedState(alSeleccionarLetraConOffset)
    val alCambiarLetraActivaActual by rememberUpdatedState(alCambiarLetraActiva)

    var arrastrando by remember { mutableStateOf(false) }
    var letraActual by remember { mutableStateOf<Char?>(null) }
    var touchY by remember { mutableFloatStateOf(0f) }
    var alturaTotalPx by remember { mutableFloatStateOf(1f) }

    val letras = remember(incluirEnie) { obtenerLetrasIndice(incluirEnie) }

    // Amplitud de la ola con física de resorte (spring) para entrada y retorno orgánico
    val amplitudOla by animateFloatAsState(
        targetValue = if (arrastrando && efectoOla && amplitudOlaDp > 0f) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.72f,
            stiffness = Spring.StiffnessLow
        ),
        label = "amplitudOla"
    )

    // Tono y luminosidad de las letras inactivas adaptado dinámicamente al tema (claro / oscuro)
    val factorTono = (tonoLetras.coerceIn(10f, 100f) / 100f)
    val colorLetraInactiva = if (esOscuroActivo) {
        lerp(
            Color(0xFF4A5568),
            Color(0xFFFFFFFF),
            factorTono
        )
    } else {
        lerp(
            Color(0xFF9AA3B8),
            Color(0xFF15171F),
            factorTono
        )
    }
    val alfaBase = if (esOscuroActivo) {
        0.35f + 0.65f * factorTono
    } else {
        0.50f + 0.50f * factorTono
    }

    Box(
        modifier = modifier
            .width(anchoZonaTactilDp.dp)
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                alturaTotalPx = coordinates.size.height.toFloat().coerceAtLeast(1f)
            }
            .pointerInput(letras) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    down.consume()
                    arrastrando = true
                    touchY = down.position.y
                    val letraNueva = calcularLetra(down.position.y, alturaTotalPx, letras)
                    letraActual = letraNueva
                    alCambiarLetraActivaActual(letraNueva)
                    if (hapticaActivaActual) haptica.tic()
                    alSeleccionarLetraActual(letraNueva)
                    alSeleccionarLetraConOffsetActual?.invoke(letraNueva, down.position.y)

                    val pointerId = down.id
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == pointerId } ?: break
                        if (!change.pressed) {
                            break
                        }
                        touchY = change.position.y
                        val l = calcularLetra(change.position.y, alturaTotalPx, letras)
                        if (l != letraActual) {
                            letraActual = l
                            alCambiarLetraActivaActual(l)
                            if (hapticaActivaActual) haptica.tic()
                            alSeleccionarLetraActual(l)
                            alSeleccionarLetraConOffsetActual?.invoke(l, change.position.y)
                        }
                        change.consume()
                    }
                    arrastrando = false
                    letraActual = null
                    alCambiarLetraActivaActual(null)
                }
            },
        contentAlignment = Alignment.CenterEnd
    ) {
        // Franja vertical con las letras del abecedario animadas en ola (Niagara wave effect)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(26.dp)
                .fillMaxHeight()
                .padding(end = 3.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            letras.forEachIndexed { indice, letra ->
                val fraccionVertical = (indice + 0.5f) / letras.size
                val esActiva = arrastrando && letraActual == letra

                Text(
                    text = letra.toString(),
                    color = if (esActiva) ColorAcento else colorLetraInactiva,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = if (esActiva) FontWeight.ExtraBold else if (esOscuroActivo) FontWeight.Medium else FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.graphicsLayer {
                        val yCentroLetra = fraccionVertical * alturaTotalPx
                        val deltaY = abs(yCentroLetra - touchY)
                        val radioSeguroDp = radioOlaDp.coerceIn(40f, 400f)
                        val radioPx = radioSeguroDp.dp.toPx()
                        val factor = if (deltaY < radioPx && amplitudOla > 0.001f && efectoOla && amplitudOlaDp > 0f) {
                            val cosVal = (1f + cos(Math.PI * (deltaY / radioPx))).toFloat() / 2f
                            cosVal.pow(1.15f) * amplitudOla
                        } else {
                            0f
                        }

                        // Desplazamiento horizontal de cada letra a lo largo de la curvatura de la ola
                        val desplazoMaxPx = amplitudOlaDp.dp.toPx()
                        translationX = -factor * desplazoMaxPx

                        // Escalado continuo: las letras van subiendo a la ola haciéndose más grandes
                        val factorEscala = (escalaMaximaLetras - 1f).coerceAtLeast(0f)
                        val escala = 1f + factorEscala * factor
                        scaleX = escala
                        scaleY = escala
                        alpha = if (esActiva) 1f else (alfaBase + (1f - alfaBase) * factor).coerceIn(0f, 1f)
                    }
                )
            }
        }

        // Círculo aumentado en la cresta de la ola
        if (mostrarCirculo) {
            GloboLetraFlotanteIndice(
                visible = arrastrando,
                letra = letraActual,
                touchY = touchY,
                alturaTotalPx = alturaTotalPx,
                tamanoCirculoDp = tamanoCirculoDp,
                offsetCirculoDp = offsetCirculoDp,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

private fun calcularLetra(posicionY: Float, alturaTotal: Float, letras: List<Char>): Char {
    val fraccion = (posicionY / alturaTotal).coerceIn(0f, 0.999f)
    val indice = (fraccion * letras.size).toInt().coerceIn(0, letras.lastIndex)
    return letras[indice]
}
