package com.jlnavas3.bovedalocal.ui.pantallas.migracion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo

/**
 * Vista de estado vacío cuando el código QR o URI de migración no contiene cuentas válidas.
 */
@Composable
fun EstadoVacioMigracion(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        TextoSubtitulo(
            texto = "El código QR o enlace no contiene credenciales de migración válidas.",
            alineacion = TextAlign.Center
        )
    }
}
