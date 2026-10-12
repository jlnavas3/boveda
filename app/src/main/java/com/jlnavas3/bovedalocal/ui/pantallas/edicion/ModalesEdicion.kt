/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.DialogoEditarCategoria

/**
 * Diálogos y modales de la pantalla de edición: selector de aplicaciones instaladas y creador de categorías.
 */
@Composable
fun ModalesEdicion(
    mostrarSelectorApp: Boolean,
    mostrarDialogoNuevaCategoria: Boolean,
    alDescartarSelectorApp: () -> Unit,
    alSeleccionarApp: (String) -> Unit,
    alDescartarNuevaCategoria: () -> Unit,
    alGuardarNuevaCategoria: (String, String, String?) -> Unit
) {
    if (mostrarSelectorApp) {
        SelectorAppModal(
            alDescartar = alDescartarSelectorApp,
            alSeleccionarApp = alSeleccionarApp
        )
    }

    if (mostrarDialogoNuevaCategoria) {
        DialogoEditarCategoria(
            categoriaAEditar = null,
            alGuardar = alGuardarNuevaCategoria,
            alDescartar = alDescartarNuevaCategoria
        )
    }
}
