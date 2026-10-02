package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Chip interactivo para conmutar filtros o etiquetas en la lista de entradas.
 */
@Composable
fun ChipFiltro(texto: String, activo: Boolean, alPulsar: () -> Unit) {
    val forma = FormaPequena
    Box(
        modifier = Modifier
            .clip(forma)
            .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(ColorTarjetaAjustes, ColorTarjetaAjustes)))
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, if (activo) Ambar else ColorBordeActual, forma)
                } else Modifier
            )
            .clickable { alPulsar() }
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text(
            texto,
            color = if (activo) ColorSobreAcento else TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Chip que representa un filtro activo con botón para removerlo.
 */
@Composable
fun ChipFiltroActivo(
    texto: String,
    alLimpiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(FormaPequena)
            .background(Ambar.copy(alpha = 0.16f))
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, Ambar.copy(alpha = 0.4f), FormaPequena)
                } else Modifier
            )
            .clickable { alLimpiar() }
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            color = Ambar,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Quitar filtro",
            tint = Ambar,
            modifier = Modifier.size(14.dp)
        )
    }
}

@BovedaPreview
@Composable
private fun PreviewChipsFiltroLista() {
    PreviewTemaBoveda {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                ChipFiltro(texto = "Todos", activo = true, alPulsar = {})
                Spacer(Modifier.width(8.dp))
                ChipFiltro(texto = "Favoritos", activo = false, alPulsar = {})
            }
            Spacer(Modifier.padding(top = 12.dp))
            ChipFiltroActivo(texto = "google.com", alLimpiar = {})
        }
    }
}

