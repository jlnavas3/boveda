@file:OptIn(ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoActivo
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoDuracionMs
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoIntensidad
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoRepeticiones
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Provee el identificador destino actual para navegación profunda en Ajustes.
 */
val LocalDestinoHighlight = compositionLocalOf<String?> { null }

/**
 * Provee el coordinador de scroll y alumbrado activo.
 */
val LocalCoordinadorResaltado = compositionLocalOf<CoordinadorResaltadoAjustes?> { null }

/**
 * Proveedor de contexto para resaltar y hacer scroll hacia una fila o grupo objetivo.
 */
@Composable
fun ProveedorResaltadoAjustes(
    seccionDestino: String?,
    scrollState: ScrollState? = null,
    contenido: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val coordinador = remember(scrollState) {
        CoordinadorResaltadoAjustes(seccionDestino, scrollState, coroutineScope)
    }

    LaunchedEffect(seccionDestino) {
        coordinador.seccionDestino = seccionDestino
    }

    CompositionLocalProvider(
        LocalDestinoHighlight provides seccionDestino,
        LocalCoordinadorResaltado provides coordinador
    ) {
        contenido()
    }
}

/**
 * Estado que gestiona la animación de alumbrado ("glow") en borde y fondo.
 */
class EstadoAlumbradoFila(
    val idFila: String?,
    val bringIntoViewRequester: BringIntoViewRequester
) {
    val colorFondoAnimado = Animatable(Color.Transparent)
    val colorBordeAnimado = Animatable(Color.Transparent)

    suspend fun dispararEfectoAlumbrado(
        colorAcento: Color,
        activo: Boolean = AlumbradoActivo,
        intensidad: Float = AlumbradoIntensidad,
        repeticiones: Int = AlumbradoRepeticiones,
        duracionMs: Int = AlumbradoDuracionMs
    ) {
        if (!activo) return

        // Factor de intensidad (0.1f..1.0f) configurado por el usuario
        val factor = intensidad.coerceIn(0.1f, 1.0f)
        val alphaMax = 0.12f + (0.36f * factor)
        val veces = repeticiones.coerceIn(1, 5)
        val duracionCiclo = duracionMs.coerceIn(300, 2000)

        // Duración proporcional de cada subfase en un pulso
        val tiempoSubida = (duracionCiclo * 0.30f).roundToInt().coerceAtLeast(80)
        val tiempoPico = (duracionCiclo * 0.15f).roundToInt().coerceAtLeast(50)
        val tiempoBajada = (duracionCiclo * 0.40f).roundToInt().coerceAtLeast(100)
        val tiempoPausaOscura = (duracionCiclo * 0.15f).roundToInt().coerceAtLeast(70)

        try {
            colorFondoAnimado.snapTo(Color.Transparent)
            for (i in 1..veces) {
                // 1. Subida luminosa nítida
                colorFondoAnimado.animateTo(
                    targetValue = colorAcento.copy(alpha = alphaMax),
                    animationSpec = tween(tiempoSubida, easing = FastOutSlowInEasing)
                )

                // 2. Mantenimiento en el pico
                delay(tiempoPico.toLong())

                // 3. Bajada completa hasta transparente
                colorFondoAnimado.animateTo(
                    targetValue = Color.Transparent,
                    animationSpec = tween(durationMillis = tiempoBajada, easing = FastOutSlowInEasing)
                )

                // 4. Pausa oscura entre destellos sucesivos
                if (i < veces) {
                    delay(tiempoPausaOscura.toLong())
                }
            }
        } finally {
            colorFondoAnimado.snapTo(Color.Transparent)
        }
    }
}
