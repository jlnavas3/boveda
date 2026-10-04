package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Subtítulo explicativo contextual con interlineado dinámico y color secundario.
 */
@Composable
fun TextoSubtitulo(
    texto: String,
    modifier: Modifier = Modifier,
    color: Color = TextoSecundario,
    alineacion: TextAlign = TextAlign.Start,
    maxLineas: Int = Int.MAX_VALUE
) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
        color = color,
        textAlign = alineacion,
        maxLines = maxLineas,
        modifier = modifier
    )
}
