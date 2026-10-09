package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente con los botones de acción para una fila de campo personalizado:
 * lápiz para editar configuración y papelera para eliminarlo.
 */
@Composable
fun AccionesCampoPersonalizadoCompacto(
    alEditar: () -> Unit,
    alEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = alEditar,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Configurar campo",
                tint = ColorAcento,
                modifier = Modifier.size(19.dp)
            )
        }
        IconButton(
            onClick = alEliminar,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = "Eliminar campo",
                tint = TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
