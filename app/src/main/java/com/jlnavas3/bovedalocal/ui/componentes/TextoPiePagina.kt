package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono

/**
 * Texto atenuado y discreto para pies de página, versiones o notas de autoría.
 */
@Composable
fun TextoPiePagina(
    texto: String,
    modifier: Modifier = Modifier,
    color: Color = ColorAjusteGris.copy(alpha = 0.85f),
    alineacion: TextAlign = TextAlign.Start,
    monoespaciada: Boolean = false,
    maxLineas: Int = 1
) {
    val estiloBase = if (monoespaciada) EstiloMono.copy(fontSize = 10.5.sp) else MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp)

    Text(
        text = texto,
        style = estiloBase,
        color = color,
        modifier = modifier,
        textAlign = alineacion,
        maxLines = maxLineas,
        overflow = TextOverflow.Ellipsis
    )
}
