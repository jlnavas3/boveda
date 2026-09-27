package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Contenedor interactivo que gestiona el gesto de deslizamiento horizontal (swipe-to-action)
 * para copiar usuario o contraseña, con animaciones de resorte y retroalimentación háptica.
 */
@Composable
fun ContenedorDeslizamientoFila(
    entradaId: String,
    alturaFila: Dp,
    forma: Shape,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit,
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val scope = rememberCoroutineScope()
    val animOffset = remember { Animatable(0f) }
    var dioHapticaTope by remember { mutableStateOf(false) }

    LaunchedEffect(entradaId) {
        animOffset.snapTo(0f)
    }

    val densidad = LocalDensity.current
    val topeMaximo = remember(densidad) { with(densidad) { 160.dp.toPx() } }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(alturaFila)
    ) {
        val offsetActual = animOffset.value
        val limite = topeMaximo
        val progreso = if (limite > 0f) (abs(offsetActual) / limite).coerceIn(0f, 1f) else 0f

        if (offsetActual > 0f) {
            FondoDeslizamientoUsuario(
                forma = forma,
                progreso = progreso
            )
        } else if (offsetActual < 0f) {
            FondoDeslizamientoContrasena(
                forma = forma,
                progreso = progreso
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(animOffset.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val change = awaitHorizontalTouchSlopOrCancellation(down.id) { ch, _ ->
                            ch.consume()
                        }
                        if (change != null) {
                            dioHapticaTope = false
                            var dragOffset = animOffset.value
                            while (true) {
                                val event = awaitPointerEvent()
                                val dragChange = event.changes.firstOrNull { it.id == change.id } ?: break
                                if (dragChange.pressed) {
                                    val dragAmount = dragChange.positionChange().x
                                    if (dragAmount != 0f) {
                                        dragChange.consume()
                                        val maximo = topeMaximo
                                        if (maximo > 0f) {
                                            dragOffset = (dragOffset + dragAmount).coerceIn(-maximo, maximo)
                                            scope.launch { animOffset.snapTo(dragOffset) }

                                            val enTope = abs(dragOffset) >= maximo * 0.96f
                                            if (enTope && !dioHapticaTope) {
                                                haptica.tic()
                                                dioHapticaTope = true
                                            } else if (!enTope && dioHapticaTope) {
                                                dioHapticaTope = false
                                            }
                                        }
                                    }
                                } else {
                                    break
                                }
                            }
                            val maximo = topeMaximo
                            if (maximo > 0f) {
                                val alcanzado = abs(animOffset.value) >= maximo * 0.94f
                                if (alcanzado) {
                                    if (animOffset.value > 0f) {
                                        alCopiarUsuario()
                                    } else {
                                        alCopiarContrasena()
                                    }
                                }
                            }
                            dioHapticaTope = false
                            scope.launch {
                                animOffset.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }
                        }
                    }
                }
        ) {
            contenido()
        }
    }
}
