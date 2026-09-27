package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PieMetadatosDetalle(
    creadaEn: Long,
    modificadaEn: Long,
    modifier: Modifier = Modifier
) {
    if (creadaEn <= 0L && modificadaEn <= 0L) return

    val formatoFecha = remember {
        SimpleDateFormat("dd/MM/yy HH:mm", Locale.forLanguageTag("es-ES"))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        if (creadaEn > 0L) {
            Text(
                text = "Creada: ${formatoFecha.format(Date(creadaEn))}",
                style = MaterialTheme.typography.labelSmall,
                color = TextoSecundario
            )
        }
        if (modificadaEn > 0L && modificadaEn != creadaEn) {
            Text(
                text = "Editada: ${formatoFecha.format(Date(modificadaEn))}",
                style = MaterialTheme.typography.labelSmall,
                color = TextoSecundario
            )
        }
    }
}
