package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/**
 * Banner informativo modular para mostrar el estado actual de la sesión
 * de transferencia directa (activo, listo para transferir, o desactivado).
 */
@Composable
fun BannerEstadoExportacionCxf(
    mensaje: String,
    estaHabilitado: Boolean,
    modifier: Modifier = Modifier
) {
    val colorFondo = if (estaHabilitado) ColorSalud else ColorAcento
    val forma = FormaTarjeta

    Surface(
        shape = forma,
        color = fondoBadgeParaTema(colorFondo),
        modifier = modifier
            .fillMaxWidth()
            .border(GrosorBorde.coerceAtLeast(1.dp), colorLegibleParaTema(colorFondo).copy(alpha = 0.35f), forma)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (estaHabilitado) Icons.Filled.CheckCircle else Icons.Filled.Lock,
                contentDescription = null,
                tint = colorLegibleParaTema(colorFondo),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = mensaje,
                color = colorLegibleParaTema(colorFondo),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}
