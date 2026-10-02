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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoActivo
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoDuracionMs
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoIntensidad
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoRepeticiones
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    var seccionDestino: String?,
    val scrollState: ScrollState?,
    val coroutineScope: CoroutineScope
) {
    var contenedorCoordinates: LayoutCoordinates? = null
    private var destinoYaProcesado = false

    private val elementosRegistrados = mutableMapOf<String, Pair<LayoutCoordinates, EstadoAlumbradoFila>>()

    fun coincideDestino(id: String?): Boolean {
        if (id.isNullOrBlank() || seccionDestino.isNullOrBlank()) return false
        val dest = seccionDestino!!.trim()
        val item = id.trim()
        if (dest == item) return true

        // Equivalencias y jerarquías conocidas
        if (dest == "03.2.G2" && item == "03.2.6") return true
        if (dest == "03.2.6" && item == "03.2.G2") return true
        if ((dest == "02-APA-THM-G04" || dest == "02-APA-THM-DAT" || dest == "02-APA-THM-G02") && (item == "02-APA-THM-G04" || item == "02-APA-THM-DAT" || item == "02-APA-THM-G02")) return true
        if (dest == "05.1.G2" && item == "05.1.6") return true
        if (dest == "05.1.6" && item == "05.1.G2") return true
        if (dest == "05-COP-MAN-G02" && item == "05-COP-MAN") return true
        if (dest == "03.3.G1" && item == "03.3.3") return true
        if (dest == "03.3.3" && item == "03.3.G1") return true
        if (dest == "04-HER-WGT-G01" && item == "04-HER-WGT-TOT") return true
        if ((dest == "03-COP-AUT" || dest == "05-COP-ATM") && (item == "05-COP-ATM-G01" || item.startsWith("05-COP-ATM") || item == "03.2.1")) return true
        if (dest == "03-LST-CAM" && (item == "03-LST-FMT" || item == "03-LST-FMT-G01" || item.startsWith("03-LST-FMT"))) return true
        if (dest == "01-SEG-DAT" && (item == "01-SEG-DAT-G01" || item.startsWith("01-SEG-DAT"))) return true
        if (dest == "01-SEG-BIO" && (item == "01-SEG-BIO-G01" || item.startsWith("01-SEG-BIO"))) return true
        if (dest == "04-HER-CAM" && (item == "04-HER-CAM-G01" || item.startsWith("04-HER-CAM"))) return true
        if ((dest == "04-HER-HIS" || dest == "04-HER-HST") && (item == "04-HER-HST-G01" || item.startsWith("04-HER-HST"))) return true
        if ((dest == "05-COP-SEG" || dest == "05-COP-MAN") && (item == "05-COP-MAN-G01" || item.startsWith("05-COP-MAN"))) return true
        if (dest == "06-AVN-IDS" && (item == "06-SIS-AVZ-G01" || item.startsWith("06-SIS-AVZ-COL"))) return true

        return false
    }

    fun registrarElemento(id: String?, coordinates: LayoutCoordinates, estadoAlumbrado: EstadoAlumbradoFila) {
        if (!id.isNullOrBlank()) {
            elementosRegistrados[id.trim()] = Pair(coordinates, estadoAlumbrado)
        }
    }

    fun resaltarAjuste(id: String, colorAcento: Color = ColorAcento) {
        val idLimpio = id.trim()
        val registrado = elementosRegistrados[idLimpio]
        val scroll = scrollState
        val contenedor = contenedorCoordinates

        coroutineScope.launch {
            if (registrado != null) {
                val (itemCoords, estado) = registrado
                if (scroll != null && contenedor != null && itemCoords.isAttached && contenedor.isAttached) {
                    try {
                        val posItemEnContenedor = contenedor.localPositionOf(itemCoords, Offset.Zero)
                        val itemTop = posItemEnContenedor.y
                        val itemHeight = itemCoords.size.height.toFloat()
                        val itemBottom = itemTop + itemHeight
                        val viewportHeight = contenedor.size.height.toFloat()

                        val paddingSeguridad = 16f
                        val estaCompletamenteVisible = (itemTop >= paddingSeguridad) && (itemBottom <= viewportHeight - paddingSeguridad)

                        if (!estaCompletamenteVisible) {
                            val posicionAbsoluta = scroll.value + itemTop
                            val destinoScroll = (posicionAbsoluta - paddingSeguridad)
                                .coerceIn(0f, scroll.maxValue.toFloat())
                                .roundToInt()

                            scroll.animateScrollTo(
                                value = destinoScroll,
                                animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
                            )
                        }
                    } catch (_: Exception) {
                        try {
                            estado.bringIntoViewRequester.bringIntoView()
                        } catch (_: Exception) {}
                    }
                } else {
                    try {
                        estado.bringIntoViewRequester.bringIntoView()
                    } catch (_: Exception) {}
                }

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

    fun registrarYEjecutarSiCoincide(
        id: String?,
        itemCoordinates: LayoutCoordinates,
        estadoAlumbrado: EstadoAlumbradoFila,
        colorAcento: Color
    ) {
        registrarElemento(id, itemCoordinates, estadoAlumbrado)

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
                    val itemTop = posItemEnContenedor.y
                    val itemHeight = itemCoordinates.size.height.toFloat()
                    val itemBottom = itemTop + itemHeight
                    val viewportHeight = contenedor.size.height.toFloat()

                    val paddingSeguridad = 16f
                    val estaCompletamenteVisible = (itemTop >= paddingSeguridad) && (itemBottom <= viewportHeight - paddingSeguridad)

                    if (!estaCompletamenteVisible) {
                        val posicionAbsoluta = scroll.value + itemTop
                        val destinoScroll = (posicionAbsoluta - paddingSeguridad)
                            .coerceIn(0f, scroll.maxValue.toFloat())
                            .roundToInt()

                        scroll.animateScrollTo(
                            value = destinoScroll,
                            animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
                        )
                    }
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
 * Modificador para asignar al contenedor con scroll vertical para medir su posición relativa y dotarlo de rebote elástico.
 */
fun Modifier.contenedorScrollAjustes(coordinador: CoordinadorResaltadoAjustes?): Modifier =
    this
        .reboteElastico()
        .then(
            if (coordinador != null) {
                Modifier.onGloballyPositioned { coords ->
                    coordinador.contenedorCoordinates = coords
                }
            } else Modifier
        )

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
                ((destinoActual == "02-APA-THM-G04" || destinoActual == "02-APA-THM-DAT" || destinoActual == "02-APA-THM-G02") &&
                 (idFila == "02-APA-THM-G04" || idFila == "02-APA-THM-DAT" || idFila == "02-APA-THM-G02")) ||
                (destinoActual == "05.1.G2" && idFila == "05.1.6") ||
                (destinoActual == "05-COP-MAN-G02" && idFila == "05-COP-MAN")
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
