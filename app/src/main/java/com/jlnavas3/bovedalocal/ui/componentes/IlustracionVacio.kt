package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorAcentoFuerte
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun IlustracionVacio(
    modifier: Modifier = Modifier,
    tamanoLupa: Dp = 120.dp
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(tamanoLupa)) {
            val centro = Offset(size.width / 2, size.height / 2)
            drawCircle(color = Borde, radius = size.minDimension / 2.2f, center = centro, style = Stroke(width = 5.dp.toPx()))
            drawCircle(
                brush = Brush.linearGradient(listOf(ColorAcento, ColorAcentoFuerte)),
                radius = size.minDimension / 7f,
                center = centro.copy(y = centro.y - size.minDimension / 14f),
                style = Stroke(width = 6.dp.toPx())
            )
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(ColorAcento, ColorAcentoFuerte)),
                topLeft = Offset(centro.x - size.minDimension / 26f, centro.y + size.minDimension / 30f),
                size = Size(size.minDimension / 13f, size.minDimension / 4.5f),
                cornerRadius = CornerRadius(10f, 10f)
            )
        }
        Text(
            "Tu bóveda está vacía",
            style = MaterialTheme.typography.headlineSmall,
            color = TextoPrincipal,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            "Guarda tu primera contraseña con el botón de abajo. Nada saldrá de este teléfono.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            modifier = Modifier.padding(top = 4.dp, start = 24.dp, end = 24.dp),
            textAlign = TextAlign.Center
        )
    }
}
