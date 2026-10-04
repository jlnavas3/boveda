package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.DialogoCompartirQr
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

/**
 * Gestor modular de diálogos de la pantalla de detalle:
 * - Diálogo de confirmación para mover a la papelera.
 * - Diálogo de compartir credencial por código QR.
 */
@Composable
fun DialogosDetalle(
    confirmarBorrado: Boolean,
    entradaActual: Entrada?,
    alConfirmarBorrado: () -> Unit,
    alDescartarBorrado: () -> Unit,
    mostrarDialogoQr: Boolean,
    alDescartarQr: () -> Unit
) {
    if (confirmarBorrado && entradaActual != null) {
        DialogoConfirmacionBoveda(
            titulo = "¿Mover a la papelera?",
            mensaje = "Se puede restaurar desde Ajustes > Papelera durante 30 días; pasado ese tiempo se borra permanentemente.",
            textoConfirmar = "Mover a la papelera",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = alConfirmarBorrado,
            alDescartar = alDescartarBorrado
        )
    }

    if (mostrarDialogoQr && entradaActual != null) {
        DialogoCompartirQr(
            entrada = entradaActual,
            alCerrar = alDescartarQr
        )
    }
}
