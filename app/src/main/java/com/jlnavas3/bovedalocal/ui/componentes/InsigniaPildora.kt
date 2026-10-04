package com.jlnavas3.bovedalocal.ui.componentes

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Insignia visual estilo píldora para estados criptográficos, badges de versión o garantías de seguridad.
 */
@Composable
fun InsigniaPildora(
    texto: String,
    modifier: Modifier = Modifier,
    colorAcento: Color = ColorAcento,
    mostrarPunto: Boolean = true,
    icono: ImageVector? = null
) {
    val forma = RoundedCornerShape(50)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(forma)
            .background(colorAcento.copy(alpha = 0.12f))
            .border(0.8.dp, colorAcento.copy(alpha = 0.35f), forma)
            .padding(horizontal = 14.dp, vertical = 5.dp)
    ) {
        if (mostrarPunto) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(colorAcento)
            )
            Spacer(Modifier.width(8.dp))
        } else if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorAcento,
                modifier = Modifier.size(13.dp)
            )
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            ),
            color = colorAcento
        )
    }
}
