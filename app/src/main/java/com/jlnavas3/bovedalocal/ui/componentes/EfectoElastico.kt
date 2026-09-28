package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Añade un efecto de rebote elástico (Spring Overscroll / Rubber-banding) al contenido scrolleable,
 * idéntico al comportamiento elástico de las aplicaciones de Google y Material 3 Expressive.
 *
 * Cuando el usuario llega al tope o al fondo y continúa deslizando, el contenido se estira
 * con resistencia física progresiva y regresa con una suave animación de resorte al soltar.
 * Se procesa en la GPU mediante graphicsLayer para garantizar 120 FPS sin recomposiciones.
 */
fun Modifier.reboteElastico(
    intensidadResistencia: Float = 0.38f,
    maximoEstiramientoDp: Float = 140f
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    val animableOffset = remember { Animatable(0f) }
    val densidad = LocalDensity.current
    val maximoPx = with(densidad) { maximoEstiramientoDp.dp.toPx() }

    val conexion = remember(coroutineScope, maximoPx, intensidadResistencia) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val actual = animableOffset.value
                if (actual != 0f) {
                    val opuesto = (actual > 0f && available.y < 0f) || (actual < 0f && available.y > 0f)
                    if (opuesto) {
                        val consumo = if (actual > 0f) {
                            available.y.coerceAtLeast(-actual)
                        } else {
                            available.y.coerceAtMost(-actual)
                        }
                        coroutineScope.launch {
                            animableOffset.snapTo(actual + consumo)
                        }
                        return Offset(0f, consumo)
                    }
                }
                return Offset.Zero
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput && available.y != 0f) {
                    val actual = animableOffset.value
                    val factorResistencia = (1f - (abs(actual) / maximoPx).coerceIn(0f, 1f)) * intensidadResistencia
                    val delta = available.y * factorResistencia
                    val nuevo = (actual + delta).coerceIn(-maximoPx, maximoPx)
                    coroutineScope.launch {
                        animableOffset.snapTo(nuevo)
                    }
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (animableOffset.value != 0f) {
                    animableOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                    return available
                }
                return Velocity.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (animableOffset.value != 0f) {
                    animableOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                }
                return Velocity.Zero
            }
        }
    }

    this
        .nestedScroll(conexion)
        .graphicsLayer {
            translationY = animableOffset.value
        }
}
