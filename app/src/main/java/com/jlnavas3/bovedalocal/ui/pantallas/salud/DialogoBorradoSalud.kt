package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

/**
 * Diálogo modal de confirmación para eliminar las entradas seleccionadas en la pantalla de salud.
 */
@Composable
fun DialogoBorradoSalud(
    visible: Boolean,
    cantidad: Int,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    if (!visible) return
    DialogoConfirmacionBoveda(
        titulo = "¿Eliminar $cantidad entrada${if (cantidad > 1) "s" else ""}?",
        mensaje = "Las entradas seleccionadas se enviarán a la papelera. Podrás recuperarlas en los próximos 30 días si lo necesitas.",
        textoConfirmar = "Eliminar",
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.Delete,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}
