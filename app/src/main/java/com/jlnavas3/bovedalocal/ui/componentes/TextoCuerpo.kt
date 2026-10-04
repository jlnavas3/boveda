package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Párrafo o texto informativo estándar adaptable al tema visual.
 */
@Composable
fun TextoCuerpo(
    texto: String,
    modifier: Modifier = Modifier,
    tamano: TamanoCuerpo = TamanoCuerpo.NORMAL,
    color: Color = TextoPrincipal,
    alineacion: TextAlign = TextAlign.Start,
    maxLineas: Int = Int.MAX_VALUE
) {
    val estilo = when (tamano) {
        TamanoCuerpo.NORMAL -> MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
        TamanoCuerpo.PEQUENO -> MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp)
        TamanoCuerpo.MINI -> MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, lineHeight = 15.sp)
    }

    Text(
        text = texto,
        style = estilo,
        color = color,
        textAlign = alineacion,
        maxLines = maxLineas,
        overflow = if (maxLineas < Int.MAX_VALUE) TextOverflow.Ellipsis else TextOverflow.Clip,
        modifier = modifier
    )
}
