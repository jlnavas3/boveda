package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.aHexConAlfa

/**
 * Microcomponente para los sliders de ajuste fino del acento personalizado:
 * Tono (Hue), Saturación, Brillo (Value) y Transparencia/Opacidad (Alpha).
 */
@Composable
fun ControlesSlidersAcentoLab(
    estadoLab: EstadoLaboratorioTemas,
    colorCampo: Color,
    colorBorde: Color,
    colorTextoPrincipal: Color,
    colorTextoSecundario: Color,
    marcarModificado: () -> Unit,
    modifier: Modifier = Modifier
) {
    with(estadoLab) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                .background(colorCampo)
                .border(1.dp, colorBorde, RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(colorAcentoActual)
                        .border(1.dp, colorBorde, CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Acento Personalizado",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = colorTextoPrincipal,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = colorAcentoActual.aHexConAlfa(),
                    style = EstiloMono.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                    color = colorTextoPrincipal
                )
            }

            Spacer(Modifier.height(12.dp))

            // Control 1: Tono / Color
            Text(
                text = "Tono: ${huePersonalizado.toInt()}°",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = colorTextoSecundario
            )
            SliderBoveda(
                value = huePersonalizado,
                onValueChange = {
                    huePersonalizado = it
                    esPersonalizadoActivo = true
                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                    marcarModificado()
                },
                valueRange = 0f..360f
            )

            Spacer(Modifier.height(8.dp))

            // Control 2: Saturación (0% es Gris puro)
            Text(
                text = "Saturación: ${(satPersonalizado * 100).toInt()}% ${if (satPersonalizado < 0.05f) "(Gris Puro)" else ""}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = colorTextoSecundario
            )
            SliderBoveda(
                value = satPersonalizado,
                onValueChange = {
                    satPersonalizado = it
                    esPersonalizadoActivo = true
                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                    marcarModificado()
                },
                valueRange = 0f..1f
            )

            Spacer(Modifier.height(8.dp))

            // Control 3: Brillo / Luminosidad
            Text(
                text = "Brillo: ${(valPersonalizado * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = colorTextoSecundario
            )
            SliderBoveda(
                value = valPersonalizado,
                onValueChange = {
                    valPersonalizado = it
                    esPersonalizadoActivo = true
                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                    marcarModificado()
                },
                valueRange = 0.10f..1f
            )

            Spacer(Modifier.height(8.dp))

            // Control 4: Transparencia / Opacidad (Alfa)
            Text(
                text = "Transparencia / Opacidad: ${(alfaPersonalizado * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (alfaPersonalizado < 1.0f) colorAcentoActual else colorTextoSecundario
                )
            )
            SliderBoveda(
                value = alfaPersonalizado,
                onValueChange = {
                    alfaPersonalizado = it
                    esPersonalizadoActivo = true
                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                    marcarModificado()
                },
                valueRange = 0.10f..1f
            )
        }
    }
}
