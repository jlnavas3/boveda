@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Provee el identificador destino actual para navegación profunda en Ajustes.
 */
val LocalDestinoHighlight = compositionLocalOf<String?> { null }

/**
 * Proveedor de contexto para resaltar y hacer scroll hacia una fila objetivo.
 */
@Composable
fun ProveedorResaltadoAjustes(
    seccionDestino: String?,
    contenido: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalDestinoHighlight provides seccionDestino) {
        contenido()
    }
}

/**
 * Estado que gestiona la animación de alumbrado ("glow") y el desplazamiento a la vista
 * cuando el ID coincide con el destino.
 */
class EstadoAlumbradoFila(
    val idFila: String?,
    val bringIntoViewRequester: BringIntoViewRequester
) {
    val colorFondoAnimado = Animatable(Color.Transparent)

    suspend fun dispararAlumbrado(colorAcento: Color) {
        // Traer a la vista
        try {
            bringIntoViewRequester.bringIntoView()
        } catch (_: Exception) {
            // No-op si aún no está montado en pantalla
        }
        // Destello rápido y desvanecimiento suave (1500 ms)
        colorFondoAnimado.snapTo(colorAcento.copy(alpha = 0.35f))
        colorFondoAnimado.animateTo(
            targetValue = Color.Transparent,
            animationSpec = tween(durationMillis = 1500)
        )
    }
}

@Composable
fun recordarEstadoAlumbrado(idFila: String?): EstadoAlumbradoFila {
    val requester = remember { BringIntoViewRequester() }
    val estado = remember(idFila) { EstadoAlumbradoFila(idFila, requester) }
    val destinoActual = LocalDestinoHighlight.current
    val colorAcento = ColorAcento

    LaunchedEffect(destinoActual, idFila) {
        if (!idFila.isNullOrBlank() && !destinoActual.isNullOrBlank()) {
            if (idFila == destinoActual || destinoActual == idFila) {
                estado.dispararAlumbrado(colorAcento)
            }
        }
    }

    return estado
}
