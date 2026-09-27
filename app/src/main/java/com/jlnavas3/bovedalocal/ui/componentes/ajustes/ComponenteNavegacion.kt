package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris

/**
 * Fila de navegación para Ajustes al estilo Honor MagicOS / Samsung One UI.
 * Contiene el icono squircle opcional, el título, subtítulo opcional, valor opcional y la flecha '>' a la derecha.
 */
@Composable
fun ComponenteNavegacion(
    titulo: String,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    valorTexto: String? = null,
    habilitado: Boolean = true,
    estadoAlumbrado: EstadoAlumbradoFila? = null
) {
    ComponenteFila(
        titulo = titulo,
        modifier = modifier,
        subtitulo = subtitulo,
        icono = icono,
        colorIcono = colorIcono,
        colorTinteIcono = colorTinteIcono,
        idFila = idFila,
        mostrarId = mostrarId,
        valorTexto = valorTexto,
        habilitado = habilitado,
        alPulsar = alPulsar,
        estadoAlumbrado = estadoAlumbrado,
        contenidoFinal = {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ColorAjusteGris.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    )
}
