package com.jlnavas3.bovedalocal.ui.pantallas.emergencia

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Print
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

@Composable
fun BotonesAccionKitEmergencia(
    alImprimirPdf: () -> Unit,
    alCopiarTexto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BotonColorido(
            texto = "Imprimir / PDF",
            color = ColorSeguridad,
            icono = Icons.Filled.Print,
            modifier = Modifier.weight(1f),
            alPulsar = alImprimirPdf
        )

        BotonColorido(
            texto = "Copiar texto",
            color = ColorSeguridad,
            icono = Icons.Filled.ContentCopy,
            modifier = Modifier.weight(1f),
            alPulsar = alCopiarTexto
        )
    }
}
