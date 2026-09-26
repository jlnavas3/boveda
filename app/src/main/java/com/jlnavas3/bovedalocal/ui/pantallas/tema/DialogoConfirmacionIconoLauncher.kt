package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento

@Composable
fun DialogoConfirmacionIconoLauncher(
    paleta: PaletaAcento,
    alConfirmar: (PaletaAcento) -> Unit,
    alDescartar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "Cambiar icono a \"${paleta.etiqueta}\"",
        mensaje = "Android cerrará la aplicación por un instante para que el launcher actualice su icono. Ya quedará guardado antes de cerrarse.",
        textoConfirmar = "Cambiar y cerrar",
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        iconoHeader = Icons.Filled.Palette,
        alConfirmar = { alConfirmar(paleta) },
        alDescartar = alDescartar
    )
}
