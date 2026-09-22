@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoActivo
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoIntensidad
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoRepeticiones
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoDuracionMs
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
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
 * Coordinador que orquesta el scroll animado hacia la parte superior de la pantalla
 * y el efecto visual de alumbrado (glow) en borde y fondo del elemento objetivo.
 */
class CoordinadorResaltadoAjustes(
    val seccionDestino: String?,
    val scrollState: ScrollState?,
    val coroutineScope: CoroutineScope
) {
    var contenedorCoordinates: LayoutCoordinates? = null
    private var destinoYaProcesado = false

    fun coincideDestino(id: String?): Boolean {
        if (id.isNullOrBlank() || seccionDestino.isNullOrBlank()) return false
        val dest = seccionDestino.trim()
        val item = id.trim()
        if (dest == item) return true

        // Equivalencias y jerarquías conocidas
        if (dest == "03.2.G2" && item == "03.2.6") return true
        if (dest == "03.2.6" && item == "03.2.G2") return true
        if (dest == "02.1.G2" && item == "02.1.4") return true
        if (dest == "02.1.4" && item == "02.1.G2") return true
        if (dest == "03.3.G1" && item == "03.3.3") return true
        if (dest == "03.3.3" && item == "03.3.G1") return true

        return false
    }

    fun registrarYEjecutarSiCoincide(
        id: String?,
        itemCoordinates: LayoutCoordinates,
        estadoAlumbrado: EstadoAlumbradoFila,
        colorAcento: Color
    ) {
        if (destinoYaProcesado || !coincideDestino(id)) return
        destinoYaProcesado = true

        coroutineScope.launch {
            // Retardo breve para que las transiciones de Compose asienten el layout
            delay(120)

            val scroll = scrollState
            val contenedor = contenedorCoordinates

            if (scroll != null && contenedor != null && itemCoordinates.isAttached && contenedor.isAttached) {
                try {
                    val posItemEnContenedor = contenedor.localPositionOf(itemCoordinates, Offset.Zero)
                    val posicionAbsoluta = scroll.value + posItemEnContenedor.y
                    val margenSuperiorPx = 24f // Margen estético para que no quede pegado al borde superior
                    val destinoScroll = (posicionAbsoluta - margenSuperiorPx)
                        .coerceIn(0f, scroll.maxValue.toFloat())
                        .roundToInt()

                    scroll.animateScrollTo(
                        value = destinoScroll,
                        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
                    )
                } catch (_: Exception) {
                    try {
                        estadoAlumbrado.bringIntoViewRequester.bringIntoView()
                    } catch (_: Exception) {}
                }
            } else {
                try {
                    estadoAlumbrado.bringIntoViewRequester.bringIntoView()
                } catch (_: Exception) {}
            }

            // Una vez centrado/posicionado, se ejecuta el efecto de alumbrado de alto impacto
            estadoAlumbrado.dispararEfectoAlumbrado(
                colorAcento = colorAcento,
                activo = AlumbradoActivo,
                intensidad = AlumbradoIntensidad,
                repeticiones = AlumbradoRepeticiones,
                duracionMs = AlumbradoDuracionMs
            )
        }
    }
}

/**
 * Modificador para asignar al contenedor con scroll vertical para medir su posición relativa.
 */
fun Modifier.contenedorScrollAjustes(coordinador: CoordinadorResaltadoAjustes?): Modifier =
    if (coordinador != null) {
        this.onGloballyPositioned { coords ->
            coordinador.contenedorCoordinates = coords
        }
    } else {
        this
    }

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
    val coordinador = remember(seccionDestino, scrollState) {
        if (!seccionDestino.isNullOrBlank()) {
            CoordinadorResaltadoAjustes(seccionDestino, scrollState, coroutineScope)
        } else null
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

        // Duración proporcional de cada subfase en un pulso:
        // 30% subida, 15% pico, 40% bajada a transparente (apagado), 15% pausa oscura entre destellos
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

                // 2. Mantenimiento en el pico para que la retina perciba claramente el destello
                delay(tiempoPico.toLong())

                // 3. Bajada completa hasta transparente (apagado total del destello)
                colorFondoAnimado.animateTo(
                    targetValue = Color.Transparent,
                    animationSpec = tween(durationMillis = tiempoBajada, easing = FastOutSlowInEasing)
                )

                // 4. Pausa oscura entre destellos sucesivos (para que no se fusionen visualmente)
                if (i < veces) {
                    delay(tiempoPausaOscura.toLong())
                }
            }
        } finally {
            colorFondoAnimado.snapTo(Color.Transparent)
        }
    }
}

@Composable
fun recordarEstadoAlumbrado(idFila: String?): EstadoAlumbradoFila {
    val requester = remember { BringIntoViewRequester() }
    val estado = remember(idFila) { EstadoAlumbradoFila(idFila, requester) }
    val destinoActual = LocalDestinoHighlight.current
    val coordinador = LocalCoordinadorResaltado.current
    val colorAcento = ColorAcento

    LaunchedEffect(destinoActual, idFila) {
        if (!idFila.isNullOrBlank() && !destinoActual.isNullOrBlank() && coordinador == null) {
            val coincide = (idFila == destinoActual) ||
                (destinoActual == "03.2.G2" && idFila == "03.2.6") ||
                (destinoActual == "02.1.G2" && idFila == "02.1.4")
            if (coincide) {
                delay(120)
                try {
                    estado.bringIntoViewRequester.bringIntoView()
                } catch (_: Exception) {}
                estado.dispararEfectoAlumbrado(
                    colorAcento = colorAcento,
                    activo = AlumbradoActivo,
                    intensidad = AlumbradoIntensidad,
                    repeticiones = AlumbradoRepeticiones,
                    duracionMs = AlumbradoDuracionMs
                )
            }
        }
    }

    return estado
}
