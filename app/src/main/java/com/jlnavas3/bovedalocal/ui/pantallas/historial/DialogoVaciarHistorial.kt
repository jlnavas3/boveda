package com.jlnavas3.bovedalocal.ui.pantallas.historial

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

@Composable
fun DialogoVaciarHistorial(
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "¿Vaciar historial de contraseñas?",
        mensaje = "Se borrarán permanentemente todas las contraseñas generadas registradas en el historial.",
        textoConfirmar = "Vaciar todo",
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.Delete,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}
