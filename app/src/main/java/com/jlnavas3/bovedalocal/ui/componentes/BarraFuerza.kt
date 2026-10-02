package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.AmbarFuerte
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import kotlin.math.round

@Composable
fun BarraFuerza(
    fraccion: Float,
    etiqueta: String,
    tiempo: String,
    bits: Double? = null
) {
    val anchoAnimado by animateFloatAsState(
        targetValue = fraccion,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "fuerza"
    )
    val color by animateColorAsState(
        targetValue = when {
            fraccion < 0.35f -> Peligro
            fraccion < 0.65f -> AmbarFuerte
            fraccion < 0.85f -> Ambar
            else -> Menta
        },
        animationSpec = spring(dampingRatio = 0.7f),
        label = "colorFuerza"
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(Borde.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(anchoAnimado)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(etiqueta, color = color, style = MaterialTheme.typography.labelLarge)
                if (bits != null && bits > 0) {
                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(color.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${round(bits).toInt()} bits",
                            color = color,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
            Text(
                tiempo,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun BarraFuerzaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            BarraFuerza(fraccion = 0.25f, etiqueta = "Débil", tiempo = "3 minutos", bits = 28.0)
            BarraFuerza(fraccion = 0.55f, etiqueta = "Media", tiempo = "5 meses", bits = 52.0)
            BarraFuerza(fraccion = 0.95f, etiqueta = "Excelente", tiempo = "siglos", bits = 96.0)
        }
    }
}

