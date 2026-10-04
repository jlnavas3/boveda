package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Enlace de texto estilizado para acciones complementarias dentro de ajustes.
 */
@Composable
fun EnlaceAjuste(texto: String, alPulsar: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text(
        texto,
        color = ColorAcento,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .clickable { alPulsar() }
            .padding(vertical = 4.dp)
    )
}
