package com.jlnavas3.bovedalocal.ui.pantallas.desbloqueo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

@Composable
fun CabeceraDesbloqueo(
    abriendo: Boolean,
    tipoAnimacion: String,
    modifier: Modifier = Modifier
) {
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
                ContenedorIconoInsignia(
                    icono = if (abriendo) Icons.Filled.LockOpen else Icons.Filled.Lock,
                    tamano = TamanoInsignia.HERO,
                    colorFondo = ColorTarjetaAjustes,
                    colorIcono = ColorAcento,
                    conBorde = true,
                    descripcion = "Bóveda cifrada"
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        TextoTitulo(
            texto = "Bóveda cerrada",
            estilo = EstiloTitulo.GRANDE,
            alineacion = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        TextoSubtitulo(
            texto = "Todo sigue cifrado en este dispositivo.",
            alineacion = TextAlign.Center
        )
    }
}
