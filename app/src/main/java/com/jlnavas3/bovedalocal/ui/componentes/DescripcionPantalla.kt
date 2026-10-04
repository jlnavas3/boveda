package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Descripción contextual que vive dentro del área con desplazamiento.
 * Desaparece al hacer scroll hacia abajo para priorizar las opciones y tarjetas.
 */
@Composable
fun DescripcionPantalla(
    subtitulo: String,
    modifier: Modifier = Modifier
) {
    if (subtitulo.isNotBlank()) {
        Text(
            text = subtitulo,
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        )
    }
}
