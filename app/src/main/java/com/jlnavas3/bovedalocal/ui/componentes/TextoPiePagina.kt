package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris

/**
 * Texto atenuado y discreto para pies de página, versiones o notas de autoría.
 */
@Composable
fun TextoPiePagina(
    texto: String,
    modifier: Modifier = Modifier,
    color: Color = ColorAjusteGris.copy(alpha = 0.85f),
    alineacion: TextAlign = TextAlign.Center
) {
    Text(
        text = texto,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
        color = color,
        modifier = modifier.fillMaxWidth(),
        textAlign = alineacion,
        maxLines = 1
    )
}
