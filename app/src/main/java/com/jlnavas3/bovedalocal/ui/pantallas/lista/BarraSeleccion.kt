package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun BarraSeleccion(
    cantidad: Int,
    todoSeleccionado: Boolean,
    alCancelar: () -> Unit,
    alRenombrar: () -> Unit = {},
    alSeleccionarTodo: () -> Unit,
    alBorrar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ColorTarjetaAjustes)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = alCancelar, modifier = Modifier.size(38.dp)) {
            Icon(Icons.Filled.Close, contentDescription = "Cancelar selección", tint = TextoPrincipal, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(6.dp))
        Text(
            text = "$cantidad ${if (cantidad == 1) "seleccionada" else "seleccionadas"}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
            color = ColorTitulos,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = alRenombrar, enabled = cantidad > 0, modifier = Modifier.size(38.dp)) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Renombrar título",
                tint = if (cantidad > 0) TextoPrincipal else TextoSecundario.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(onClick = alSeleccionarTodo, modifier = Modifier.size(38.dp)) {
            Icon(
                Icons.Filled.SelectAll,
                contentDescription = if (todoSeleccionado) "Deseleccionar todo" else "Seleccionar todo",
                tint = if (todoSeleccionado) ColorAcento else TextoPrincipal,
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(onClick = alBorrar, enabled = cantidad > 0, modifier = Modifier.size(38.dp)) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Borrar seleccionadas",
                tint = if (cantidad > 0) Peligro else Peligro.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
