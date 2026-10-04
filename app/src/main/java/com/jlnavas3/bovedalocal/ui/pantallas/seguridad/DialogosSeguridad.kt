package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoModoCompatible

/**
 * Diálogos modales de la pantalla de seguridad y biometría.
 */
@Composable
fun DialogosSeguridad(
    dialogoCompatible: String?,
    confirmarDesactivarSecure: Boolean,
    alDescartarCompatible: () -> Unit,
    alConfirmarCompatible: () -> Unit,
    alConfirmarDesactivarSecure: () -> Unit,
    alDescartarDesactivarSecure: () -> Unit
) {
    dialogoCompatible?.let { motivo ->
        DialogoModoCompatible(
            motivo = motivo,
            alDescartar = alDescartarCompatible,
            alConfirmar = alConfirmarCompatible
        )
    }

    if (confirmarDesactivarSecure) {
        DialogoDesactivarSecure(
            alConfirmar = alConfirmarDesactivarSecure,
            alDescartar = alDescartarDesactivarSecure
        )
    }
}
