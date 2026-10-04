package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Fila básica de configuración con título, descripción y conmutador SwitchBoveda.
 */
@Composable
fun FilaAjuste(
    titulo: String,
    descripcion: String,
    activo: Boolean,
    habilitado: Boolean = true,
    colorActivo: Color = ColorAcento,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(titulo, color = TextoPrincipal, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(2.dp))
            Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        }
        SwitchBoveda(
            checked = activo,
            enabled = habilitado,
            colorActivo = colorActivo,
            onCheckedChange = alCambiar
        )
    }
}
