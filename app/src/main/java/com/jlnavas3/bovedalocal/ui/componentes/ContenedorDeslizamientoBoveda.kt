package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Modelo para configurar una acción al deslizar una fila en la UI.
 */
data class AccionDeslizamiento(
    val texto: String,
    val icono: ImageVector,
    val color: Color,
    val alEjecutar: () -> Unit
)

/**
 * Contenedor genérico interactivo que gestiona el gesto de deslizamiento horizontal (swipe-to-action)
 * revelando acciones a la izquierda (deslizar a la derecha) o derecha (deslizar a la izquierda).
 *
 * Incluye atenuación progresiva de la tarjeta arrastrada (`alpha = 1 - progreso`) para máxima legibilidad,
 * animaciones elásticas de retorno y retroalimentación háptica en el tope de activación.
 */
@Composable
fun ContenedorDeslizamientoBoveda(
    idItem: String,
    modifier: Modifier = Modifier,
    forma: Shape = FormaTarjeta,
    accionIzquierda: AccionDeslizamiento? = null, // Revelada al arrastrar hacia la DERECHA
    accionDerecha: AccionDeslizamiento? = null,   // Revelada al arrastrar hacia la IZQUIERDA
    enGrupo: Boolean = false,
    alturaFila: Dp? = null,
    contenido: @Composable () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val scope = rememberCoroutineScope()
    val animOffset = remember { Animatable(0f) }
    var dioHapticaTope by remember { mutableStateOf(false) }

    LaunchedEffect(idItem) {
        animOffset.snapTo(0f)
    }

    val densidad = LocalDensity.current
    val topeMaximo = remember(densidad) { with(densidad) { 160.dp.toPx() } }

    val baseModifier = if (alturaFila != null) {
        modifier.fillMaxWidth().height(alturaFila)
    } else {
        modifier.fillMaxWidth()
    }

    Box(modifier = baseModifier) {
        val offsetActual = animOffset.value
        val limite = topeMaximo
        val progreso = if (limite > 0f) (abs(offsetActual) / limite).coerceIn(0f, 1f) else 0f

        // Fondo de acción cuando se arrastra hacia la derecha (revela accionIzquierda)
        if (offsetActual > 0f && accionIzquierda != null) {
            FondoAccionDeslizamiento(
                accion = accionIzquierda,
                forma = forma,
                progreso = progreso,
                enGrupo = enGrupo,
                esInicio = true,
                modifier = Modifier.matchParentSize()
            )
        } else if (offsetActual < 0f && accionDerecha != null) {
            // Fondo de acción cuando se arrastra hacia la izquierda (revela accionDerecha)
            FondoAccionDeslizamiento(
                accion = accionDerecha,
                forma = forma,
                progreso = progreso,
                enGrupo = enGrupo,
                esInicio = false,
                modifier = Modifier.matchParentSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(animOffset.value.roundToInt(), 0) }
                .graphicsLayer {
                    alpha = (1f - progreso).coerceIn(0f, 1f)
                }
                .pointerInput(idItem, accionIzquierda, accionDerecha) {
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
                                        val permitirDerecha = accionIzquierda != null
                                        val permitirIzquierda = accionDerecha != null
                                        val nuevoOffset = dragOffset + dragAmount

                                        if ((nuevoOffset > 0f && !permitirDerecha) || (nuevoOffset < 0f && !permitirIzquierda)) {
                                            // No arrastrar hacia direcciones sin acción configurada
                                        } else {
                                            dragChange.consume()
                                            val maximo = topeMaximo
                                            if (maximo > 0f) {
                                                dragOffset = nuevoOffset.coerceIn(-maximo, maximo)
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
                                        accionIzquierda?.alEjecutar?.invoke()
                                    } else {
                                        accionDerecha?.alEjecutar?.invoke()
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

@Composable
private fun FondoAccionDeslizamiento(
    accion: AccionDeslizamiento,
    forma: Shape,
    progreso: Float,
    enGrupo: Boolean,
    esInicio: Boolean,
    modifier: Modifier = Modifier
) {
    val formaFondo = if (enGrupo) RectangleShape else forma
    val escala = 0.85f + 0.20f * progreso
    val opacidad = 0.4f + 0.6f * progreso

    Box(
        modifier = modifier
            .clip(formaFondo)
            .background(accion.color.copy(alpha = 0.14f + 0.10f * progreso))
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && !enGrupo) {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFondo)
                } else Modifier
            )
            .padding(horizontal = 20.dp),
        contentAlignment = if (esInicio) Alignment.CenterStart else Alignment.CenterEnd
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.scale(escala)
        ) {
            if (esInicio) {
                Icon(
                    imageVector = accion.icono,
                    contentDescription = null,
                    tint = accion.color.copy(alpha = opacidad),
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = accion.texto.trim(),
                    color = accion.color.copy(alpha = opacidad),
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = accion.texto.trim(),
                    color = accion.color.copy(alpha = opacidad),
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(10.dp))
                Icon(
                    imageVector = accion.icono,
                    contentDescription = null,
                    tint = accion.color.copy(alpha = opacidad),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
