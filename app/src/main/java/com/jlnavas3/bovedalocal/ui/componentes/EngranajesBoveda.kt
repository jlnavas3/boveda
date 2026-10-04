package com.jlnavas3.bovedalocal.ui.componentes

import android.os.SystemClock
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive

/**
 * Engranajes mecánicos de bóveda / relojería:
 * Tren de ruedas dentadas acopladas matemáticamente en Canvas animadas con física continua.
 */
@Composable
fun EngranajesBoveda(
    abierta: Boolean,
    modifier: Modifier = Modifier,
    tamano: Int? = null,
    config: EngranajesConfig = EngranajesConfig()
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

    // Geometría canónica de radios de paso según módulo constante m = 4.888889f
    val rPaso24 = 58.67f
    val rPaso20 = 48.89f
    val rPaso18 = 44.00f
    val rPaso16 = 39.11f
    val rPaso14 = 34.22f
    val rPaso12 = 29.33f
    val rPaso10 = 24.44f
    val rPasoCore = 17.00f

    val pathZ24 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso24, 24, config.alturaDientes, config.anchoDientes) }
    val pathZ20 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso20, 20, config.alturaDientes, config.anchoDientes) }
    val pathZ18 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso18, 18, config.alturaDientes, config.anchoDientes) }
    val pathZ16 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso16, 16, config.alturaDientes, config.anchoDientes) }
    val pathZ14 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso14, 14, config.alturaDientes, config.anchoDientes) }
    val pathZ12 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso12, 12, config.alturaDientes, config.anchoDientes) }
    val pathZ10 = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPaso10, 10, config.alturaDientes, config.anchoDientes) }
    val pathCore = remember(config.alturaDientes, config.anchoDientes) { crearPathEngranaje(rPasoCore, 10, config.alturaDientes, config.anchoDientes) }

    val boxModifier = if (tamano != null) modifier.size(tamano.dp) else modifier

    Box(modifier = boxModifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            dibujarTrenEngranajesMecanicos(
                tiempoSegundos = tiempoSegundos,
                progreso = progreso,
                config = config,
                pathZ24 = pathZ24,
                pathZ20 = pathZ20,
                pathZ18 = pathZ18,
                pathZ16 = pathZ16,
                pathZ14 = pathZ14,
                pathZ12 = pathZ12,
                pathZ10 = pathZ10,
                pathCore = pathCore,
                rPaso24 = rPaso24,
                rPaso20 = rPaso20,
                rPaso18 = rPaso18,
                rPaso16 = rPaso16,
                rPaso14 = rPaso14,
                rPaso12 = rPaso12,
                rPaso10 = rPaso10,
                rPasoCore = rPasoCore
            )
        }
    }
}
