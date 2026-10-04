package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

@Composable
fun DialogoBorrarSeleccion(
    cantidad: Int,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "¿Mover $cantidad entradas a la papelera?",
        mensaje = "Se pueden restaurar desde la papelera durante 30 días.",
        textoConfirmar = "Mover a la papelera",
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.Delete,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}
