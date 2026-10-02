package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.util.Dominios
import kotlin.math.abs

fun coloresMonograma(semilla: String): Pair<Color, Color> {
    val base = Dominios.raiz(semilla).ifEmpty { semilla }.lowercase()
    var hash = 2166136261u.toInt()
    for (c in base) {
        hash = (hash xor c.code) * 16777619
    }
    val tono = (abs(hash) % 360).toFloat()
    val primero = Color.hsv(tono, 0.55f, 0.92f)
    val segundo = Color.hsv((tono + 28f) % 360f, 0.72f, 0.78f)
    return primero to segundo
}

@Composable
fun Monograma(titulo: String, semilla: String, tamano: Int = 46) {
    val (a, b) = remember(semilla, titulo) { coloresMonograma(semilla.ifBlank { titulo }) }
    val pincelFondo = remember(a, b) { Brush.linearGradient(listOf(a, b)) }
    val colorTexto = remember(a) { colorContraste(a) }
    val forma = CircleShape

    val letras = remember(titulo) {
        val t = titulo.trim()
        if (t.isEmpty()) "?"
        else {
            val partes = t.split(' ').filter { it.isNotEmpty() }
            if (partes.size >= 2) {
                "${partes[0].first().uppercaseChar()}${partes[1].first().uppercaseChar()}"
            } else {
                "${partes[0].first().uppercaseChar()}"
            }
        }
    }

    Box(
        modifier = Modifier
            .size(tamano.dp)
            .clip(forma)
            .background(pincelFondo)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual.copy(alpha = 0.35f), forma)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letras,
            color = colorTexto,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano / 2.4f).sp
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun MonogramaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            Monograma(titulo = "Google", semilla = "google.com")
            Monograma(titulo = "GitHub Inc", semilla = "github.com")
            Monograma(titulo = "Amazon Web", semilla = "aws.amazon.com")
            Monograma(titulo = "Netflix", semilla = "netflix.com")
        }
    }
}

