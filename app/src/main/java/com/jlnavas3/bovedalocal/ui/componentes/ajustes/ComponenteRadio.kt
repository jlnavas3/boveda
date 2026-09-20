package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Fila con selector de opción única (Radio Button) según el diseño de Honor MagicOS / Samsung One UI
 * (como en la captura "Modo de tema").
 */
@Composable
fun ComponenteRadio(
    titulo: String,
    seleccionado: Boolean,
    alSeleccionar: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    habilitado: Boolean = true,
    colorAcento: Color = ColorAcento
) {
    ComponenteFila(
        titulo = titulo,
        modifier = modifier,
        icono = icono,
        colorIcono = colorIcono,
        colorTinteIcono = colorTinteIcono,
        idFila = idFila,
        mostrarId = mostrarId,
        habilitado = habilitado,
        alPulsar = { if (habilitado && !seleccionado) alSeleccionar() },
        contenidoFinal = {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(
                        width = if (seleccionado) 6.dp else 1.5.dp,
                        color = if (seleccionado) colorAcento else ColorAjusteGris.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            )
        }
    )
}
