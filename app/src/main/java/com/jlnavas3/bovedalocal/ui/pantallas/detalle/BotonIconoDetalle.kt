package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Botón de acción con icono estandarizado para la pantalla de detalle (40x40 dp, icono 20 dp).
 */
@Composable
fun BotonIconoDetalle(
    icono: ImageVector,
    descripcion: String,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = ColorIconosInternos
) {
    IconButton(
        onClick = alPulsar,
        modifier = modifier.size(40.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}
