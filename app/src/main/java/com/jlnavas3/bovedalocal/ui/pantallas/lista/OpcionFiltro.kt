package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Opción individual dentro del menú de filtrado.
 */
@Composable
fun OpcionFiltro(texto: String, icono: ImageVector, activo: Boolean, alPulsar: () -> Unit) {
    DropdownMenuItem(
        leadingIcon = {
            Icon(
                icono,
                contentDescription = null,
                tint = if (activo) ColorAcento else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        },
        text = {
            Text(
                texto,
                color = if (activo) ColorAcento else TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingIcon = {
            if (activo) Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp))
        },
        onClick = alPulsar
    )
}
