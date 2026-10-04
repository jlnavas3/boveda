package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

@Composable
fun DialogoModoCompatible(
    motivo: String,
    alDescartar: () -> Unit,
    alConfirmar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "Modo compatible",
        mensaje = motivo + "\n\nEn este modo la huella o el PIN los comprueba Android y la app abre la bóveda. " +
            "La clave maestra sigue envuelta por el Keystore y no sale del móvil, pero no queda atada " +
            "al chip como en el modo fuerte: es algo más débil. Tu contraseña maestra sigue siendo la " +
            "única llave real, y puedes volver al modo fuerte cuando quieras.",
        textoConfirmar = "Activar modo compatible",
        textoCancelar = "Ahora no",
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}
