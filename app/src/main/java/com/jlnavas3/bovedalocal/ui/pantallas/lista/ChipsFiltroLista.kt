package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sell
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
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

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

@BovedaPreview
@Composable
private fun PreviewChipsFiltroLista() {
    PreviewTemaBoveda {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipFiltro(texto = "Todos", activo = true, icono = Icons.Filled.Sell, alPulsar = {})
                ChipFiltro(texto = "Favoritos", activo = false, icono = Icons.Filled.Sell, alPulsar = {})
            }
            Spacer(Modifier.padding(top = 12.dp))
            ChipFiltroActivo(texto = "google.com", icono = Icons.Filled.Sell, alLimpiar = {})
        }
    }
}

