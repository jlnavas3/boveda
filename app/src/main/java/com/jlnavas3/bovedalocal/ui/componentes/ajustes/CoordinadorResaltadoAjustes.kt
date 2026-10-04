@file:OptIn(ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
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
 * Coordinador que orquesta el scroll animado hacia la parte superior de la pantalla
 * y el efecto visual de alumbrado (glow) en borde y fondo del elemento objetivo.
 */
class CoordinadorResaltadoAjustes(
    var seccionDestino: String?,
    val scrollState: ScrollState?,
    val coroutineScope: CoroutineScope
) {
    var contenedorCoordinates: LayoutCoordinates? = null

    private var destinoYaProcesado: Boolean = false
    private val elementosRegistrados = mutableMapOf<String, Pair<LayoutCoordinates, EstadoAlumbradoFila>>()

    fun coincideDestino(id: String?): Boolean {
        if (id.isNullOrBlank() || seccionDestino.isNullOrBlank()) return false
        val dest = seccionDestino!!.trim()
        val item = id.trim()
        if (dest == item) return true

        // Equivalencias y jerarquías conocidas
        if (dest == "03.2.G2" && item == "03.2.6") return true
        if (dest == "03.2.6" && item == "03.2.G2") return true
        if ((dest == "02-APA-THM-G04" || dest == "02-APA-THM-DAT" || dest == "02-APA-THM-G02") &&
            (item == "02-APA-THM-G04" || item == "02-APA-THM-DAT" || item == "02-APA-THM-G02")) return true
        if (dest == "05.1.G2" && item == "05.1.6") return true
        if (dest == "05.1.6" && item == "05.1.G2") return true
        if (dest == "05-COP-MAN-G02" && item == "05-COP-MAN") return true
        if (dest == "03.3.G1" && item == "03.3.3") return true
        if (dest == "03.3.3" && item == "03.3.G1") return true
        if (dest == "04-HER-WGT-G01" && item == "04-HER-WGT-TOT") return true
        if ((dest == "03-COP-AUT" || dest == "05-COP-ATM") &&
            (item == "05-COP-ATM-G01" || item.startsWith("05-COP-ATM") || item == "03.2.1")) return true
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

    private suspend fun animarYAlumbrar(
        itemCoordinates: LayoutCoordinates,
        estadoAlumbrado: EstadoAlumbradoFila,
        colorAcento: Color
    ) {
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

        // Una vez centrado/posicionado, se ejecuta el efecto de alumbrado
        estadoAlumbrado.dispararEfectoAlumbrado(
            colorAcento = colorAcento,
            activo = AlumbradoActivo,
            intensidad = AlumbradoIntensidad,
            repeticiones = AlumbradoRepeticiones,
            duracionMs = AlumbradoDuracionMs
        )
    }

    fun resaltarAjuste(id: String, colorAcento: Color = ColorAcento) {
        val idLimpio = id.trim()
        val registrado = elementosRegistrados[idLimpio] ?: return
        coroutineScope.launch {
            animarYAlumbrar(registrado.first, registrado.second, colorAcento)
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
            animarYAlumbrar(itemCoordinates, estadoAlumbrado, colorAcento)
        }
    }
}
