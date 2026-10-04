package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun ColumnaInterruptor(
    simbolo: String,
    etiqueta: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { alCambiar(!activo) }
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Text(
            text = simbolo,
            color = if (activo) ColorTitulos else TextoSecundario,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = etiqueta,
            color = if (activo) TextoPrincipal else TextoSecundario,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(Modifier.height(4.dp))
        SwitchBoveda(
            checked = activo,
            onCheckedChange = alCambiar
        )
    }
}
