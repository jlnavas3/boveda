package com.jlnavas3.bovedalocal.ui.pantallas.desbloqueo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun CabeceraDesbloqueo(
    abriendo: Boolean,
    esAnimacionEngranajes: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!esAnimacionEngranajes) {
            PuertaBoveda(abierta = abriendo, tamano = 180)
        } else {
            Spacer(Modifier.height(250.dp))
        }

        Spacer(Modifier.height(18.dp))
        Text(
            text = "Bóveda cerrada",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = ColorTitulos
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Todo sigue cifrado en este dispositivo.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )
    }
}
