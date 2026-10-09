package com.jlnavas3.bovedalocal.ui.componentes.seguridad

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.jlnavas3.bovedalocal.ui.componentes.blindajeSemanticoSensible

/**
 * Texto protegido visualmente.
 * Soporta desenfoque por hardware (Android 12+) con animación suave de enfoque/desenfoque,
 * o fallback seguro a puntos de longitud fija / real.
 * Incorpora blindaje semántico para impedir que servicios de accesibilidad lean el secreto.
 */
@Composable
fun TextoSeguroVisual(
    texto: String,
    oculto: Boolean,
    estilo: String = "desenfoque",
    modifier: Modifier = Modifier,
    estiloTexto: TextStyle = LocalTextStyle.current,
    colorTexto: Color = Color.Unspecified,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    val usaBlur = estilo == "desenfoque" && UtilesSeguridadVisual.soportaDesenfoqueHardware()

    val modificadorBlindado = modifier.blindajeSemanticoSensible(
        esSensible = true,
        etiquetaAccesible = if (oculto) "Contenido protegido" else null
    )

    if (usaBlur) {
        val radioDesenfoque by animateDpAsState(
            targetValue = if (oculto) 7.dp else 0.dp,
            animationSpec = tween(durationMillis = 200),
            label = "blur_anim"
        )
        Box(modifier = modificadorBlindado) {
            Text(
                text = texto,
                style = estiloTexto,
                color = colorTexto,
                maxLines = maxLines,
                overflow = overflow,
                modifier = if (radioDesenfoque > 0.dp) Modifier.blur(radioDesenfoque) else Modifier
            )
        }
    } else {
        val textoAMostrar = if (oculto) {
            UtilesSeguridadVisual.enmascarar(texto, estilo)
        } else {
            texto
        }
        Text(
            text = textoAMostrar,
            style = estiloTexto,
            color = colorTexto,
            maxLines = maxLines,
            overflow = overflow,
            modifier = modificadorBlindado
        )
    }
}
