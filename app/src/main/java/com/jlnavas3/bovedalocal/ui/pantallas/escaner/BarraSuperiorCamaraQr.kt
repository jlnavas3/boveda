package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena

/**
 * Microcomponente para la barra superior de acciones del escáner QR:
 * botón volver, linterna, escaneo desde galería, entrada manual y ajustes de cámara.
 */
@Composable
fun BarraSuperiorCamaraQr(
    flashEncendido: Boolean,
    alAlternarFlash: () -> Unit,
    alEscanearImagen: () -> Unit,
    alEntradaManual: () -> Unit,
    alAbrirAjustes: () -> Unit,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Botón Volver
            IconButton(
                onClick = alVolver,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            // Acciones rápidas flotantes: Luz, Escanear imagen, Manual, Ajustes
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Luz / Flash
                BotonAccionSuperior(
                    icono = if (flashEncendido) Icons.Filled.FlashlightOn else Icons.Filled.FlashlightOff,
                    etiqueta = "Luz",
                    activo = flashEncendido,
                    alPulsar = alAlternarFlash
                )

                // Escanear Imagen
                BotonAccionSuperior(
                    icono = Icons.Filled.Image,
                    etiqueta = "Escanear imag…",
                    activo = false,
                    alPulsar = alEscanearImagen
                )

                // Entrada Manual
                BotonAccionSuperior(
                    icono = Icons.Filled.Keyboard,
                    etiqueta = "Manual",
                    activo = false,
                    alPulsar = alEntradaManual
                )

                // Ajustes de Cámara
                BotonAccionSuperior(
                    icono = Icons.Filled.Tune,
                    etiqueta = "Ajustes",
                    activo = false,
                    alPulsar = alAbrirAjustes
                )
            }
        }
    }
}

/**
 * Botón con icono y etiqueta vertical estilo la app de referencia.
 */
@Composable
private fun BotonAccionSuperior(
    icono: ImageVector,
    etiqueta: String,
    activo: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(FormaPequena)
            .clickable(onClick = alPulsar)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (activo) ColorAcento.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.45f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = etiqueta,
                tint = if (activo) ColorAcento else Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.height(3.dp))
        Text(
            text = etiqueta,
            color = if (activo) ColorAcento else Color.White,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp, fontWeight = FontWeight.Medium),
            maxLines = 1
        )
    }
}
