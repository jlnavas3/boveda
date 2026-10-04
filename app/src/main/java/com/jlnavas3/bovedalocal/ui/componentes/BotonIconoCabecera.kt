package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Botón de icono estandarizado para las cabeceras de pantalla (40.dp, recorte circular, icono de 20.dp).
 */
@Composable
fun BotonIconoCabecera(
    onClick: () -> Unit,
    icono: ImageVector,
    descripcion: String,
    modifier: Modifier = Modifier,
    tint: Color = ColorIconosInternos,
    colorFondo: Color = ColorTarjetaAjustes,
    habilitado: Boolean = true
) {
    IconButton(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (habilitado) colorFondo else colorFondo.copy(alpha = 0.5f))
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = if (habilitado) tint else tint.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}
