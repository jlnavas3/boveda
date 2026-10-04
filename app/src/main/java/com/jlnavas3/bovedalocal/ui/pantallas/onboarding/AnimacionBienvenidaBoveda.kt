package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.EngranajesBoveda
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.aEngranajesConfig
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas

/**
 * Visualizador mecánico de la bóveda para el paso de bienvenida
 * (Engranajes giratorios, puerta blindada o candado estilizado).
 */
@Composable
fun AnimacionBienvenidaBoveda(
    ajustes: AjustesApp,
    modifier: Modifier = Modifier
) {
    when (ajustes.animacionDesbloqueo) {
        "engranajes" -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                EngranajesBoveda(
                    abierta = false,
                    modifier = Modifier.fillMaxSize(),
                    config = ajustes.aEngranajesConfig()
                )
            }
        }
        "puerta" -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                PuertaBoveda(abierta = false, tamano = 175)
            }
        }
        else -> {
            Box(
                modifier = modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(18.dp)))
                    .background(ColorTarjetaAjustes),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}
