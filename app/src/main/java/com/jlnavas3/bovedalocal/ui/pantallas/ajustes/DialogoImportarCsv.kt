package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

@Composable
fun DialogoImportarCsv(
    alDescartar: () -> Unit,
    alConfirmar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "Importar CSV",
        mensaje = "El CSV que exportan Google, Chrome, Bitwarden o LastPass va sin cifrar: cualquiera que " +
            "lo abra ve las contraseñas en claro. Elige el archivo, se cifra al entrar en la bóveda, " +
            "y después borra ese CSV de donde lo tengas guardado.",
        textoConfirmar = "Elegir archivo",
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}
