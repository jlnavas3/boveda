package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda

/**
 * Microcomponente para los diálogos modales de confirmación del Laboratorio de Temas.
 */
@Composable
fun DialogoConfirmacionGuardarTema(
    visible: Boolean,
    modificadoOscuro: Boolean,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    if (!visible) return

    val modoModificadoTexto = if (modificadoOscuro) "Modo Oscuro" else "Modo Claro"
    val modoSinModificarTexto = if (modificadoOscuro) "Modo Claro" else "Modo Oscuro"

    DialogoConfirmacionBoveda(
        titulo = "Guardar Tema",
        mensaje = "Has modificado el $modoModificadoTexto, pero el $modoSinModificarTexto se mantendrá con su diseño actual.\n\n¿Deseas guardar los cambios?",
        textoConfirmar = "Guardar",
        alConfirmar = alConfirmar,
        alDescartar = alDescartar,
        textoCancelar = "Seguir editando",
        iconoHeader = Icons.Filled.Save
    )
}
