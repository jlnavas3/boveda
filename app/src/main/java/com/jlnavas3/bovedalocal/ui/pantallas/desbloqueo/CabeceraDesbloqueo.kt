package com.jlnavas3.bovedalocal.ui.pantallas.desbloqueo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun CabeceraDesbloqueo(
    abriendo: Boolean,
    tipoAnimacion: String,
    modifier: Modifier = Modifier
) {
    val formaCandado = RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(16.dp))

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (tipoAnimacion) {
            "engranajes" -> {
                Spacer(Modifier.height(250.dp))
            }
            "puerta" -> {
                PuertaBoveda(abierta = abriendo, tamano = 180)
            }
            else -> {
                // Modo estático "ninguna": 0% uso de GPU/CPU, sin ciclos de animación continua
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(formaCandado)
                        .background(ColorTarjetaAjustes)
                        .then(
                            if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                                Modifier.border(GrosorBorde, ColorBordeActual, formaCandado)
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (abriendo) Icons.Filled.LockOpen else Icons.Filled.Lock,
                        contentDescription = "Bóveda cifrada",
                        tint = ColorAcento,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
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
