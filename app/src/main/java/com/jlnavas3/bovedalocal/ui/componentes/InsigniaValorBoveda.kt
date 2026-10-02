package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Píldora o insignia de valor numérico/etiqueta con estilo nativo de Bóveda Local.
 */
@Composable
fun InsigniaValorBoveda(
    texto: String,
    modifier: Modifier = Modifier,
    colorAcento: Color = ColorAcento
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(colorAcento.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = colorAcento,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun InsigniaValorBovedaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            InsigniaValorBoveda(texto = "969")
            InsigniaValorBoveda(texto = "2FA", colorAcento = com.jlnavas3.bovedalocal.ui.theme.Menta)
            InsigniaValorBoveda(texto = "Alerta", colorAcento = com.jlnavas3.bovedalocal.ui.theme.Peligro)
        }
    }
}

