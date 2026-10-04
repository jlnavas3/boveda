package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento

/**
 * Chip que representa un filtro activo con botón para removerlo.
 * Formato y dimensiones idénticos a los chips de la barra de colecciones.
 */
@Composable
fun ChipFiltroActivo(
    texto: String,
    alLimpiar: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null
) {
    val formaChip = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .clip(formaChip)
            .background(ColorAcento)
            .then(
                Modifier.border(0.8.dp, ColorAcento, formaChip)
            )
            .clickable { alLimpiar() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = ColorSobreAcento,
                modifier = Modifier.size(15.dp)
            )
        }
        Text(
            text = texto,
            color = ColorSobreAcento,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        )
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Quitar filtro",
            tint = ColorSobreAcento,
            modifier = Modifier.size(14.dp)
        )
    }
}
