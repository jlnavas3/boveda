package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun IconoAlternarContrasena(
    esVisible: Boolean,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (esVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            contentDescription = if (esVisible) "Ocultar contraseña" else "Mostrar contraseña",
            tint = TextoSecundario,
            modifier = Modifier.size(20.dp)
        )
    }
}
