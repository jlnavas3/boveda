package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Indicador de cantidad de elementos seleccionados.
 */
@Composable
fun BarraControlesSeleccionExportar(
    seleccionadas: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$seleccionadas de $total seleccionadas",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            ),
            color = ColorAcento
        )
    }
}
