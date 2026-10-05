package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Píldora compacta que muestra el nombre y color identificador de una [Identidad]
 * asociada a una credencial dentro del listado principal o pantallas de detalle.
 */
@Composable
fun InsigniaIdentidadEntrada(
    identidad: Identidad,
    modifier: Modifier = Modifier
) {
    val colorBase = parsearColorO(identidad.colorHex ?: "", ColorAcento)
    val forma = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .clip(forma)
            .background(colorBase.copy(alpha = 0.14f))
            .border(0.7.dp, colorBase.copy(alpha = 0.35f), forma)
            .padding(horizontal = 7.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(colorBase)
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = identidad.nombre,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            ),
            color = colorBase,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
