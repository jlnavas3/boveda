package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar

/**
 * Fila de ajuste con interruptor basculante (Switch) estilo Samsung One UI / Honor MagicOS.
 * Pulsar en cualquier lugar de la fila conmuta el switch automáticamente.
 */
@Composable
fun ComponenteSwitch(
    titulo: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    colorTinteIcono: Color = Color.White,
    idFila: String? = null,
    mostrarId: Boolean = false,
    habilitado: Boolean = true,
    colorActivo: Color = Ambar
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
        alPulsar = { if (habilitado) alCambiar(!activo) },
        contenidoFinal = {
            SwitchBoveda(
                checked = activo,
                onCheckedChange = if (habilitado) alCambiar else null,
                enabled = habilitado,
                colorActivo = colorActivo
            )
        }
    )
}
