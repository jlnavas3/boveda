package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Chip interactivo para conmutar filtros o etiquetas en la lista de entradas.
 * Formato y dimensiones idénticos a los chips de la barra de colecciones.
 */
@Composable
fun ChipFiltro(
    texto: String,
    activo: Boolean,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    alPulsar: () -> Unit
) {
    val formaChip = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .clip(formaChip)
            .background(if (activo) ColorAcento else ColorTarjetaAjustes)
            .then(
                if (activo) {
                    Modifier.border(0.8.dp, ColorAcento, formaChip)
                } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                } else {
                    Modifier.border(0.8.dp, ColorSeparadorAjustes, formaChip)
                }
            )
            .clickable { alPulsar() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = if (activo) ColorSobreAcento else ColorAcento,
                    modifier = Modifier.size(15.dp)
                )
            }
            Text(
                text = texto,
                color = if (activo) ColorSobreAcento else TextoPrincipal,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp
                )
            )
        }
    }
}
