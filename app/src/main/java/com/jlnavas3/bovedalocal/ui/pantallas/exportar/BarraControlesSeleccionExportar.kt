package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Indicador de cantidad de elementos seleccionados en la pantalla de exportación selectiva.
 */
@Composable
fun BarraControlesSeleccionExportar(
    seleccionadas: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
    ) {
        TextoSubtitulo(
            texto = "$seleccionadas de $total seleccionadas",
            color = ColorAcento,
            maxLineas = 1
        )
    }
}
