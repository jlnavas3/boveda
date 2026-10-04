package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.ui.pantallas.colecciones.DialogoCrearEditarColeccion

/**
 * Diálogos y modales de la pantalla de edición: selector de aplicaciones instaladas y creador de colecciones.
 */
@Composable
fun ModalesEdicion(
    mostrarSelectorApp: Boolean,
    mostrarDialogoNuevaColeccion: Boolean,
    alDescartarSelectorApp: () -> Unit,
    alSeleccionarApp: (String) -> Unit,
    alDescartarNuevaColeccion: () -> Unit,
    alGuardarNuevaColeccion: (String, String, String?) -> Unit
) {
    if (mostrarSelectorApp) {
        SelectorAppModal(
            alDescartar = alDescartarSelectorApp,
            alSeleccionarApp = alSeleccionarApp
        )
    }

    if (mostrarDialogoNuevaColeccion) {
        DialogoCrearEditarColeccion(
            alGuardar = alGuardarNuevaColeccion,
            alDescartar = alDescartarNuevaColeccion
        )
    }
}
