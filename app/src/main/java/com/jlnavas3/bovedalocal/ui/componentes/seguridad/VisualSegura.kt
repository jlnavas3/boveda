package com.jlnavas3.bovedalocal.ui.componentes.seguridad

import android.os.Build
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import kotlinx.coroutines.delay

/**
 * Utilidades y componentes microgranulares de seguridad visual.
 * Permite enmascarar datos sensibles con efecto cristal (blur) o puntos discretos,
 * con temporizador configurable de auto-ocultamiento.
 */
object UtilesSeguridadVisual {

    fun enmascarar(
        texto: String,
        estilo: String,
        longitudFija: Int = 8
    ): String {
        if (texto.isEmpty()) return ""
        return when (estilo) {
            "puntos_reales" -> "•".repeat(texto.length.coerceIn(4, 28))
            else -> "•".repeat(longitudFija)
        }
    }

    fun soportaDesenfoqueHardware(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

/**
 * Efecto para volver a ocultar un dato sensible tras el tiempo configurado.
 * Si [tiempoSegundos] es <= 0, el dato no se auto-oculta (modo manual).
 */
@Composable
fun TemporizadorAutoOcultar(
    revelado: Boolean,
    tiempoSegundos: Int,
    alAutoOcultar: () -> Unit
) {
    if (revelado && tiempoSegundos > 0) {
        LaunchedEffect(revelado, tiempoSegundos) {
            delay(tiempoSegundos * 1000L)
            alAutoOcultar()
        }
    }
}

/**
 * Texto protegido visualmente.
 * Soporta desenfoque por hardware (Android 12+) con animación suave de enfoque/desenfoque,
 * o fallback seguro a puntos de longitud fija / real.
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

    if (usaBlur) {
        val radioDesenfoque by animateDpAsState(
            targetValue = if (oculto) 7.dp else 0.dp,
            animationSpec = tween(durationMillis = 200),
            label = "blur_anim"
        )
        Box(modifier = modifier) {
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
        val estiloEfectivo = if (oculto) {
            EstiloMonoGrande.copy(letterSpacing = 2.sp, fontSize = estiloTexto.fontSize)
        } else {
            estiloTexto
        }

        Text(
            text = textoAMostrar,
            style = estiloEfectivo,
            color = colorTexto,
            maxLines = maxLines,
            overflow = overflow,
            modifier = modifier
        )
    }
}
