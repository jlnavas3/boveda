package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Insignia visual estilo píldora con las garantías criptográficas esenciales.
 */
@Composable
fun InsigniaSeguridadOnboarding(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(ColorAcento.copy(alpha = 0.12f))
            .border(0.8.dp, ColorAcento.copy(alpha = 0.35f), RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(ColorAcento)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "ARGON2ID · AES-256 · SIN CONEXIÓN",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            ),
            color = ColorAcento
        )
    }
}
