package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

@Composable
fun DialogoBorrarBoveda(
    alDescartar: () -> Unit,
    alConfirmar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "¿Borrar la bóveda entera?",
        mensaje = "Se elimina el archivo cifrado y la clave de la huella. Si no tienes copia, no hay vuelta atrás.",
        textoConfirmar = "Borrar todo",
        textoCancelar = "Cancelar",
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.DeleteForever,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}
