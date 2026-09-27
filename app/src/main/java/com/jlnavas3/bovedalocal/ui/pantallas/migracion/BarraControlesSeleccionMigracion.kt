package com.jlnavas3.bovedalocal.ui.pantallas.migracion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun BarraControlesSeleccionMigracion(
    cuantasSeleccionadas: Int,
    totalCuentas: Int,
    alSeleccionarSoloNuevas: () -> Unit,
    alSeleccionarTodas: () -> Unit,
    alSeleccionarNinguna: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$cuantasSeleccionadas de $totalCuentas seleccionadas",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = ColorAcento
        )

        Row {
            TextButton(onClick = alSeleccionarSoloNuevas) {
                Text("Solo nuevas", style = MaterialTheme.typography.bodySmall, color = ColorAcento)
            }

            TextButton(onClick = alSeleccionarTodas) {
                Text("Todas", style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
            }

            TextButton(onClick = alSeleccionarNinguna) {
                Text("Ninguna", style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
            }
        }
    }
}
