package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos

/**
 * Título principal de gran escala visual para portadas, bienvenida y pantallas introductorias.
 */
@Composable
fun TextoTituloHero(
    texto: String,
    modifier: Modifier = Modifier,
    color: Color = ColorTitulos,
    alineacion: TextAlign = TextAlign.Center
) {
    Text(
        text = texto,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp
        ),
        color = color,
        textAlign = alineacion,
        modifier = modifier
    )
}
