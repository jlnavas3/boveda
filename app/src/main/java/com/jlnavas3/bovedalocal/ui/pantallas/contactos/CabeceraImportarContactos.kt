package com.jlnavas3.bovedalocal.ui.pantallas.contactos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente de cabecera para el modal de importación de contactos,
 * incluyendo contador de seleccionados y alternador rápido de selección masiva.
 */
@Composable
fun CabeceraImportarContactos(
    totalContactos: Int,
    seleccionadosCount: Int,
    todosSeleccionados: Boolean,
    alAlternarTodos: () -> Unit,
    alCerrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Importar contactos",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            )
            Text(
                text = "$seleccionadosCount de $totalContactos seleccionado(s)",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (totalContactos > 0) {
                TextButton(onClick = alAlternarTodos) {
                    Text(
                        text = if (todosSeleccionados) "Deseleccionar" else "Todos",
                        color = ColorAcento,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            IconButton(
                onClick = alCerrar,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cerrar",
                    tint = TextoSecundario,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
