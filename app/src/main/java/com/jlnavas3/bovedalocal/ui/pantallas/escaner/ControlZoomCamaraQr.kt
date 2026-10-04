package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena

/**
 * Microcomponente para el control inferior de zoom de la cámara y la pista de texto orientativa.
 */
@Composable
fun ControlZoomCamaraQr(
    zoomRatio: Float,
    alCambiarZoom: (Float) -> Unit,
    alAumentarZoom: () -> Unit,
    alReducirZoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 24.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Slider de Zoom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(FormaPequena)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Zoom (-)
            IconButton(
                onClick = alReducirZoom,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ZoomOut,
                    contentDescription = "Reducir zoom",
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(22.dp)
                )
            }

            // Slider continuo
            Slider(
                value = zoomRatio,
                onValueChange = alCambiarZoom,
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = ColorAcento,
                    activeTrackColor = ColorAcento,
                    inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                ),
                modifier = Modifier.weight(1f)
            )

            // Zoom (+)
            IconButton(
                onClick = alAumentarZoom,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ZoomIn,
                    contentDescription = "Aumentar zoom",
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Apunta al código QR para escanearlo automáticamente",
            color = Color.White.copy(alpha = 0.70f),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            textAlign = TextAlign.Center
        )
    }
}
