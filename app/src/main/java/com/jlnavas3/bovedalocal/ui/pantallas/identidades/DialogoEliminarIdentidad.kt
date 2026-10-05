package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo de confirmación para eliminar una [Identidad].
 * Informa al usuario sobre cuántas cuentas están asociadas y aclara
 * que las credenciales no serán eliminadas, solo desvinculadas.
 */
@Composable
fun DialogoEliminarIdentidad(
    identidad: Identidad,
    cantidadEntradasVinculadas: Int,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = forma,
        containerColor = ColorTarjetaAjustes,
        modifier = Modifier.then(
            if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                Modifier.border(GrosorBorde, ColorBordeActual, forma)
            } else Modifier
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = Peligro,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Eliminar identidad",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextoPrincipal
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "¿Deseas eliminar la identidad '${identidad.nombre}'?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoPrincipal
                )
                Spacer(Modifier.height(8.dp))
                if (cantidadEntradasVinculadas > 0) {
                    Text(
                        text = "Hay $cantidadEntradasVinculadas cuenta(s) vinculada(s). Las credenciales NO se borrarán, solo quedarán sin identidad asociada.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                } else {
                    Text(
                        text = "Esta acción no se puede deshacer.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = alConfirmar) {
                Text(text = "Eliminar", color = Peligro, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = alDescartar) {
                Text(text = "Cancelar", color = TextoSecundario)
            }
        }
    )
}
