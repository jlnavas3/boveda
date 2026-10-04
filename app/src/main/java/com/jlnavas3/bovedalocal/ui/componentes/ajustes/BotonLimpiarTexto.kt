package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun BotonLimpiarTexto(
    onLimpiar: () -> Unit
) {
    IconButton(onClick = onLimpiar) {
        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = "Limpiar texto",
            tint = TextoSecundario,
            modifier = Modifier.size(18.dp)
        )
    }
}
