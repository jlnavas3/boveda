package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Microcomponente para mostrar notificaciones flotantes animadas en la cámara QR (errores o estado).
 */
@Composable
fun MensajeFlotanteQr(
    mensaje: String?,
    esError: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = mensaje != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .statusBarsPadding()
            .padding(top = 80.dp)
    ) {
        mensaje?.let { texto ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .clip(FormaPequena)
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = texto,
                    color = if (esError) Peligro else Menta,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
