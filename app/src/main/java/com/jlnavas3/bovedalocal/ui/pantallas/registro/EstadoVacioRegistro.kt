package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

@Composable
fun EstadoVacioRegistro(estaVacio: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(FormaTarjeta)
                .background(fondoBadgeParaTema(ColorAcento)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = null,
                tint = colorLegibleParaTema(ColorAcento),
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = if (estaVacio) "(Registro vacío todavía)" else "No hay eventos con los filtros actuales",
            color = ColorAjusteGris,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
