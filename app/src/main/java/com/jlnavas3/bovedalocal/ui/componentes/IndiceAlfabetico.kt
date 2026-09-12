package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.ItemAgrupado
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Lista de caracteres del abecedario ordenado con localización en español ('Ñ' incluida)
 * y símbolo '#' al inicio para números y caracteres especiales.
 */
val LETRAS_INDICE: List<Char> = listOf('#') + ('A'..'N').toList() + listOf('Ñ') + ('O'..'Z').toList()

fun normalizarCaracterIndice(c: Char): Char {
    val mayus = c.uppercaseChar()
    return when (mayus) {
        'Á', 'À', 'Ä', 'Â', 'Ã' -> 'A'
        'É', 'È', 'Ë', 'Ê' -> 'E'
        'Í', 'Ì', 'Ï', 'Î' -> 'I'
        'Ó', 'Ò', 'Ö', 'Ô', 'Õ' -> 'O'
        'Ú', 'Ù', 'Ü', 'Û' -> 'U'
        'Ñ' -> 'Ñ'
        in 'A'..'Z' -> mayus
        else -> '#'
    }
}

fun letraInicialIndice(texto: String): Char {
    val limpia = texto.trim()
    if (limpia.isEmpty()) return '#'
    return normalizarCaracterIndice(limpia.first())
}

fun ItemAgrupado.tituloParaIndice(): String = when (this) {
    is ItemAgrupado.Suelto -> entrada.titulo
    is ItemAgrupado.Grupo -> clave.removePrefix("www.")
    is ItemAgrupado.Hijo -> entrada.titulo
}

/**
 * Encuentra el índice más cercano del elemento en la lista correspondiente a la letra solicitada.
 * Si no existe ningún elemento que comience por esa letra, avanza a la siguiente letra disponible en el abecedario.
 */
fun encontrarIndiceParaLetra(items: List<ItemAgrupado>, letra: Char): Int? {
    if (items.isEmpty()) return null
    if (letra == '#') {
        val primeroSimbolo = items.indexOfFirst { letraInicialIndice(it.tituloParaIndice()) == '#' }
        return if (primeroSimbolo >= 0) primeroSimbolo else 0
    }

    // 1. Coincidencia directa
    val exacto = items.indexOfFirst { letraInicialIndice(it.tituloParaIndice()) == letra }
    if (exacto >= 0) return exacto

    // 2. Si no existe, buscar la siguiente letra disponible en el orden alfabético
    val pos = LETRAS_INDICE.indexOf(letra)
    if (pos >= 0) {
        for (i in (pos + 1)..LETRAS_INDICE.lastIndex) {
            val sigLetra = LETRAS_INDICE[i]
            val siguiente = items.indexOfFirst { letraInicialIndice(it.tituloParaIndice()) == sigLetra }
            if (siguiente >= 0) return siguiente
        }
    }
    return items.lastIndex
}

/**
 * Componente modular de índice alfabético vertical para navegación y desplazamiento rápido
 * con efecto de ola fluida estilo Niagara Launcher.
 * Al tocar o arrastrar el dedo:
 * - Toda la columna de letras se curva hacia la izquierda formando una ola interactiva continua.
 * - Emite retroalimentación háptica en cada cambio de letra.
 * - En la cresta de la ola se sitúa la letra principal en un círculo grande sin borde que llega casi a media pantalla.
 * - Notifica la letra seleccionada para desplazar el listado.
 */
@Composable
fun IndiceAlfabetico(
    alSeleccionarLetra: (Char) -> Unit,
    modifier: Modifier = Modifier,
    efectoOla: Boolean = true,
    amplitudOlaDp: Float = 95f,
    radioOlaDp: Float = 220f,
    escalaMaximaLetras: Float = 1.9f,
    mostrarCirculo: Boolean = true,
    offsetCirculoDp: Float = 145f,
    hapticaActiva: Boolean = true,
    anchoZonaTactilDp: Float = 50f
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var arrastrando by remember { mutableStateOf(false) }
    var letraActual by remember { mutableStateOf<Char?>(null) }
    var touchY by remember { mutableFloatStateOf(0f) }
    var alturaTotalPx by remember { mutableFloatStateOf(1f) }

    val densidad = LocalDensity.current

    // Amplitud de la ola con física de resorte (spring) para entrada y retorno orgánico
    val amplitudOla by animateFloatAsState(
        targetValue = if (arrastrando && efectoOla && amplitudOlaDp > 0f) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.72f,
            stiffness = Spring.StiffnessLow
        ),
        label = "amplitudOla"
    )

    Box(
        modifier = modifier
            .width(anchoZonaTactilDp.dp)
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                alturaTotalPx = coordinates.size.height.toFloat().coerceAtLeast(1f)
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    down.consume()
                    arrastrando = true
                    touchY = down.position.y
                    val letraNueva = calcularLetra(down.position.y, alturaTotalPx)
                    if (letraNueva != letraActual) {
                        letraActual = letraNueva
                        if (hapticaActiva) haptica.tic()
                        alSeleccionarLetra(letraNueva)
                    }

                    val pointerId = down.id
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == pointerId } ?: break
                        if (!change.pressed) {
                            break
                        }
                        touchY = change.position.y
                        val l = calcularLetra(change.position.y, alturaTotalPx)
                        if (l != letraActual) {
                            letraActual = l
                            if (hapticaActiva) haptica.tic()
                            alSeleccionarLetra(l)
                        }
                        change.consume()
                    }
                    arrastrando = false
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
            LETRAS_INDICE.forEachIndexed { indice, letra ->
                val fraccionVertical = (indice + 0.5f) / LETRAS_INDICE.size
                val esActiva = arrastrando && letraActual == letra

                Text(
                    text = letra.toString(),
                    color = if (esActiva) Ambar else TextoSecundario.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = if (esActiva) FontWeight.ExtraBold else FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.graphicsLayer {
                        val yCentroLetra = fraccionVertical * alturaTotalPx
                        val deltaY = abs(yCentroLetra - touchY)
                        val radioPx = radioOlaDp.dp.toPx()
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
                        // hasta llegar a la cima con su tamaño máximo, y decrecen al descender.
                        val factorEscala = (escalaMaximaLetras - 1f).coerceAtLeast(0f)
                        val escala = 1f + factorEscala * factor
                        scaleX = escala
                        scaleY = escala
                        alpha = (0.55f + 0.45f * factor).coerceIn(0f, 1f)
                    }
                )
            }
        }

        // Círculo aumentado en la cresta de la ola (sin borde, gran formato, proyectado casi a media pantalla)
        if (mostrarCirculo) {
            AnimatedVisibility(
                visible = arrastrando && letraActual != null,
                enter = fadeIn(animationSpec = tween(90)) + scaleIn(initialScale = 0.5f, animationSpec = tween(90)),
                exit = fadeOut(animationSpec = tween(140)) + scaleOut(targetScale = 0.5f, animationSpec = tween(140)),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset {
                        val diametroPx = with(densidad) { 78.dp.roundToPx() }
                        val yPx = (touchY - diametroPx / 2f).coerceIn(0f, alturaTotalPx - diametroPx).roundToInt()
                        val xPx = with(densidad) { (-offsetCirculoDp).dp.roundToPx() }
                        IntOffset(xPx, yPx)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(78.dp)
                        .shadow(elevation = 14.dp, shape = CircleShape)
                        .clip(CircleShape)
                        .background(DegradadoAmbar),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (letraActual ?: ' ').toString(),
                        color = ColorSobreAcento,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 38.sp
                        )
                    )
                }
            }
        }
    }
}

private fun calcularLetra(posicionY: Float, alturaTotal: Float): Char {
    val fraccion = (posicionY / alturaTotal).coerceIn(0f, 0.999f)
    val indice = (fraccion * LETRAS_INDICE.size).toInt().coerceIn(0, LETRAS_INDICE.lastIndex)
    return LETRAS_INDICE[indice]
}
