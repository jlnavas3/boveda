package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun OpcionGeneradorCompacta(
    texto: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(78.dp)) {
        Text(texto, color = TextoSecundario, style = MaterialTheme.typography.labelSmall, maxLines = 1)
        SwitchBoveda(
            checked = activo,
            onCheckedChange = alCambiar
        )
    }
}
