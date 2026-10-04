package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente para alternar rápidamente el estado de sensibilidad o secreto de un campo.
 */
@Composable
fun ToggleSensibilidadCampo(
    esSensible: Boolean,
    alAlternar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(FormaPequena)
            .clickable(onClick = alAlternar)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Security,
            contentDescription = null,
            tint = if (esSensible) ColorIconosInternos else TextoSecundario,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = if (esSensible) "Marcado como secreto (toca para desmarcar)" else "Marcar como secreto / sensible",
            style = MaterialTheme.typography.labelSmall,
            color = if (esSensible) ColorIconosInternos else TextoSecundario
        )
    }
}
