/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.Menta
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Dibuja la máscara oscura semitransparente con una ventana central recortada
 * y 4 esquinas angulares destacadas, respetando fielmente la curvatura global
 * configurada en "Formas y bordes".
 *
 * Incluye un manejador táctil en la esquina inferior derecha para redimensionar
 * el marco de captura, un botón de restablecimiento en la esquina inferior izquierda
 * y una tarjeta informativa con el porcentaje de carga de trabajo.
 */
@Composable
fun VisorMascaraQr(
    codigoDetectado: Boolean,
    modifier: Modifier = Modifier,
    tamanoVentanaDp: Dp = 260.dp,
    desplazamientoVerticalDp: Dp = (-20).dp,
    alCambiarTamano: (Dp) -> Unit = {},
    alRestablecerTamano: () -> Unit = {}
) {
    val densidad = LocalDensity.current
    val ambito = rememberCoroutineScope()
    var arrastrando by remember { mutableStateOf(false) }
    var mostrarInfo by remember { mutableStateOf(false) }
    var temporizadorOcultar by remember { mutableStateOf<Job?>(null) }
    var tamanoAcumuladoPx by remember { mutableFloatStateOf(0f) }

    val colorEsquinas by animateColorAsState(
        targetValue = if (codigoDetectado) Menta else ColorAcento,
        animationSpec = tween(durationMillis = 200),
        label = "colorEsquinas"
    )

    val transicionInfinita = rememberInfiniteTransition(label = "pulsoEsquinas")
    val pulsoAlpha by transicionInfinita.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsoAlpha"
    )

    val tamanoVentanaPx = with(densidad) { tamanoVentanaDp.toPx() }
    val despVerticalPx = with(densidad) { desplazamientoVerticalDp.toPx() }
    val longitudBrazoPx = with(densidad) { 32.dp.toPx() }
    val grosorTrazoPx = with(densidad) { 4.5.dp.toPx() }

    // Sincronización estricta con la curvatura de esquinas configurada por el usuario
    val radioEsquinaPx = with(densidad) {
        CurvaturaEsquinas.toPx().coerceAtLeast(0f).coerceAtMost(tamanoVentanaPx / 4f)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val anchoPantalla = size.width
            val altoPantalla = size.height

            val left = (anchoPantalla - tamanoVentanaPx) / 2f
            val top = ((altoPantalla - tamanoVentanaPx) / 2f) + despVerticalPx
            val right = left + tamanoVentanaPx
            val bottom = top + tamanoVentanaPx

            // 1. Fondo oscurecido con recorte central EvenOdd
            val pathMascara = Path().apply {
                fillType = PathFillType.EvenOdd
                // Rectángulo exterior completo
                addRect(Rect(0f, 0f, anchoPantalla, altoPantalla))
                // Ventana central recortada respetando CurvaturaEsquinas
                addRoundRect(
                    RoundRect(
                        rect = Rect(left, top, right, bottom),
                        cornerRadius = CornerRadius(radioEsquinaPx, radioEsquinaPx)
                    )
                )
            }
            drawPath(
                path = pathMascara,
                color = Color.Black.copy(alpha = 0.65f)
            )

            // 2. Trazar las 4 esquinas angulares independientes
            val colorFinal = colorEsquinas.copy(alpha = if (codigoDetectado) 1f else pulsoAlpha)
            val estiloTrazo = Stroke(
                width = grosorTrazoPx,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )

            // Esquina Superior Izquierda ⌜
            val pathTopLeft = Path().apply {
                if (radioEsquinaPx > 1f) {
                    moveTo(left, top + longitudBrazoPx)
                    lineTo(left, top + radioEsquinaPx)
                    quadraticTo(left, top, left + radioEsquinaPx, top)
                    lineTo(left + longitudBrazoPx, top)
                } else {
                    moveTo(left, top + longitudBrazoPx)
                    lineTo(left, top)
                    lineTo(left + longitudBrazoPx, top)
                }
            }
            drawPath(pathTopLeft, colorFinal, style = estiloTrazo)

            // Esquina Superior Derecha ⌝
            val pathTopRight = Path().apply {
                if (radioEsquinaPx > 1f) {
                    moveTo(right - longitudBrazoPx, top)
                    lineTo(right - radioEsquinaPx, top)
                    quadraticTo(right, top, right, top + radioEsquinaPx)
                    lineTo(right, top + longitudBrazoPx)
                } else {
                    moveTo(right - longitudBrazoPx, top)
                    lineTo(right, top)
                    lineTo(right, top + longitudBrazoPx)
                }
            }
            drawPath(pathTopRight, colorFinal, style = estiloTrazo)

            // Esquina Inferior Izquierda ⌞
            val pathBottomLeft = Path().apply {
                if (radioEsquinaPx > 1f) {
                    moveTo(left, bottom - longitudBrazoPx)
                    lineTo(left, bottom - radioEsquinaPx)
                    quadraticTo(left, bottom, left + radioEsquinaPx, bottom)
                    lineTo(left + longitudBrazoPx, bottom)
                } else {
                    moveTo(left, bottom - longitudBrazoPx)
                    lineTo(left, bottom)
                    lineTo(left + longitudBrazoPx, bottom)
                }
            }
            drawPath(pathBottomLeft, colorFinal, style = estiloTrazo)

            // Esquina Inferior Derecha ⌟
            val pathBottomRight = Path().apply {
                if (radioEsquinaPx > 1f) {
                    moveTo(right - longitudBrazoPx, bottom)
                    lineTo(right - radioEsquinaPx, bottom)
                    quadraticTo(right, bottom, right, bottom - radioEsquinaPx)
                    lineTo(right, bottom - longitudBrazoPx)
                } else {
                    moveTo(right - longitudBrazoPx, bottom)
                    lineTo(right, bottom)
                    lineTo(right, bottom - longitudBrazoPx)
                }
            }
            drawPath(pathBottomRight, colorFinal, style = estiloTrazo)
        }

        // Posiciones de los controles inferiores en las esquinas del visor
        val mitadAncho = tamanoVentanaDp / 2f
        val posY = mitadAncho + desplazamientoVerticalDp - 26.dp
        val posXIzq = -mitadAncho + 10.dp
        val posXDer = mitadAncho - 26.dp

        // 3. Botón de restablecimiento a tamaño estándar en esquina inferior izquierda
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = posXIzq, y = posY)
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        alRestablecerTamano()
                        tamanoAcumuladoPx = with(densidad) { 260.dp.toPx() }
                        mostrarInfo = true
                        temporizadorOcultar?.cancel()
                        temporizadorOcultar = ambito.launch {
                            delay(2500)
                            if (!arrastrando) mostrarInfo = false
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.RestartAlt,
                contentDescription = "Restablecer tamaño predeterminado",
                tint = colorEsquinas.copy(alpha = 0.9f),
                modifier = Modifier.size(19.dp)
            )
        }

        // 4. Manejador de arrastre táctil para redimensionar en esquina inferior derecha
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = posXDer, y = posY)
                .size(48.dp)
                .pointerInput(densidad) {
                    detectDragGestures(
                        onDragStart = {
                            arrastrando = true
                            mostrarInfo = true
                            temporizadorOcultar?.cancel()
                            tamanoAcumuladoPx = with(densidad) { tamanoVentanaDp.toPx() }
                        },
                        onDragEnd = {
                            arrastrando = false
                            temporizadorOcultar = ambito.launch {
                                delay(2200)
                                if (!arrastrando) mostrarInfo = false
                            }
                        },
                        onDragCancel = {
                            arrastrando = false
                            temporizadorOcultar = ambito.launch {
                                delay(1200)
                                if (!arrastrando) mostrarInfo = false
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        val deltaPx = (dragAmount.x + dragAmount.y) / 2f
                        val minPx = with(densidad) { 170.dp.toPx() }
                        val maxPx = with(densidad) { 340.dp.toPx() }
                        tamanoAcumuladoPx = (tamanoAcumuladoPx + deltaPx * 2f).coerceIn(minPx, maxPx)
                        val nuevoTamano = with(densidad) { tamanoAcumuladoPx.toDp() }
                        alCambiarTamano(nuevoTamano)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = if (arrastrando) 0.7f else 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.OpenInFull,
                    contentDescription = "Arrastrar para cambiar área de escaneo",
                    tint = if (arrastrando) ColorAcento else colorEsquinas.copy(alpha = 0.9f),
                    modifier = Modifier.size(17.dp)
                )
            }
        }

        // 5. Tarjeta informativa flotante de carga de trabajo centrada dentro del visor
        TarjetaInfoCargaEscaneoQr(
            visible = mostrarInfo,
            tamanoVentanaDp = tamanoVentanaDp,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = desplazamientoVerticalDp - 8.dp)
        )
    }
}
