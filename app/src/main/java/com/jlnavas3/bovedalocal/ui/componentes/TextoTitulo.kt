package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos

/**
 * Título tipográfico estructurado para pantallas, cabeceras, tarjetas y modales.
 */
@Composable
fun TextoTitulo(
    texto: String,
    modifier: Modifier = Modifier,
    estilo: EstiloTitulo = EstiloTitulo.MEDIANO,
    color: Color = ColorTitulos,
    alineacion: TextAlign = TextAlign.Start,
    maxLineas: Int = Int.MAX_VALUE
) {
    val estiloTexto = when (estilo) {
        EstiloTitulo.HERO -> MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        EstiloTitulo.GRANDE -> MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        EstiloTitulo.MEDIANO -> MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        EstiloTitulo.PEQUENO -> MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
    }

    Text(
        text = texto,
        style = estiloTexto,
        color = color,
        textAlign = alineacion,
        maxLines = maxLineas,
        overflow = if (maxLineas < Int.MAX_VALUE) TextOverflow.Ellipsis else TextOverflow.Clip,
        modifier = modifier
    )
}
