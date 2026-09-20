package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.background
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

/**
 * Fila para selección de color estilo Samsung One UI / Honor MagicOS.
 * Muestra una pastilla o muestra circular (swatch) con el color actualmente configurado,
 * que al pulsar invoca el diálogo o selector de color.
 */
@Composable
fun ComponenteColorPicker(
    titulo: String,
    colorActual: Color,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    valorTexto: String? = null,
    habilitado: Boolean = true
) {
    ComponenteFila(
        titulo = titulo,
        modifier = modifier,
        icono = icono,
        colorIcono = colorIcono,
        colorTinteIcono = colorTinteIcono,
        idFila = idFila,
        mostrarId = mostrarId,
        valorTexto = valorTexto,
        habilitado = habilitado,
        alPulsar = alPulsar,
        contenidoFinal = {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(colorActual)
                    .border(
                        width = 1.5.dp,
                        color = ColorAjusteGris.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            )
        }
    )
}
