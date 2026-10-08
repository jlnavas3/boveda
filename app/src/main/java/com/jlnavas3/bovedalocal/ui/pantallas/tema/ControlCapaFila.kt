package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.aHexConAlfa

/**
 * Microcomponente que representa el control deslizante para ajustar la luminancia
 * y tono individual de una capa cromática (fondo, tarjeta, campo, borde, textos).
 */
@Composable
fun ControlCapaFila(
    etiqueta: String,
    colorActual: Color,
    luminancia: Float,
    alCambiarLuminancia: (Float) -> Unit,
    modifier: Modifier = Modifier,
    rangoLuminancia: ClosedFloatingPointRange<Float> = 0.0f..1.0f,
    mostrarControlTono: Boolean = false,
    tono: Float = 0f,
    alCambiarTono: ((Float) -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(colorActual)
                    .border(1.dp, Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = colorActual.aHexConAlfa(),
                style = EstiloMono.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Luminancia / Brillo",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Color.Gray
            )
            Text(
                text = "${(luminancia * 100).toInt()}%",
                style = EstiloMono.copy(fontSize = 11.sp),
                color = Color.Gray
            )
        }
        SliderBoveda(
            value = luminancia.coerceIn(rangoLuminancia.start, rangoLuminancia.endInclusive),
            onValueChange = alCambiarLuminancia,
            valueRange = rangoLuminancia
        )

        if (mostrarControlTono && alCambiarTono != null) {
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.hsv(tono, 0.8f, 0.9f))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Tono individual: ${tono.toInt()}°",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
            }
            SliderBoveda(
                value = tono,
                onValueChange = alCambiarTono,
                valueRange = 0.0f..360.0f
            )
        }
    }
}
