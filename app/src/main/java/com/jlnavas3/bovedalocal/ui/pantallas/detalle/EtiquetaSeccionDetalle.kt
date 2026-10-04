package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris

/**
 * Encabezado de sección normalizado al estilo One UI para grupos de datos en detalle.
 */
@Composable
fun EtiquetaSeccionDetalle(
    texto: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = texto.uppercase(),
        color = ColorAjusteGris,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        ),
        modifier = modifier.padding(start = 16.dp, bottom = 8.dp, top = 4.dp)
    )
}
