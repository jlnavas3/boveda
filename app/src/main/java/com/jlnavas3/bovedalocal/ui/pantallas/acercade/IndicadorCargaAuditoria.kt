package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun IndicadorCargaAuditoria(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = ColorSeguridad)
            Spacer(Modifier.width(14.dp))
            Text("Leyendo auditoría y estado del hardware…", color = TextoSecundario)
        }
    }
}
