package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

/**
 * Fila de acción para Ajustes (restablecer o ejecutar comandos) diseñada al estilo
 * de las opciones de sistema de Honor MagicOS y Samsung One UI.
 *
 * En lugar de botones flotantes descontextualizados, se integra como una fila
 * de tarjeta con chevron a la derecha (>) y texto conciso:
 * - "Restablecer" (para un ajuste concreto)
 * - "Restablecer grupo" (para varios valores de la tarjeta)
 * - "Restablecer módulo" (para la pantalla completa)
 */
@Composable
fun ComponenteBotonFila(
    titulo: String,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = Icons.Filled.RestartAlt,
    colorIcono: Color? = ColorIconosInternos,
    colorTinteIcono: Color = Color.White,
    valorTexto: String? = null,
    idFila: String? = null,
    mostrarId: Boolean = false,
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
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ColorAjusteGris.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    )
}
