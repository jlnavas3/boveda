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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Dominios

fun coloresMonograma(semilla: String): Pair<Color, Color> {
    return Color(0xFF2A2D30) to Color(0xFF2A2D30)
}

@Composable
fun Monograma(titulo: String, semilla: String, tamano: Int = 46) {
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
            .background(ColorCampoAjustes)
            .border(0.8.dp, ColorSeparadorAjustes, forma),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letras,
            color = TextoPrincipal,
            fontWeight = FontWeight.SemiBold,
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

