/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

/**
 * Diálogo modal desacoplado para confirmar la destrucción permanente de un lote de entradas seleccionadas.
 */
@Composable
fun DialogoConfirmarBorradoSeleccionadosPapelera(
    abierto: Boolean,
    cantidad: Int,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    if (!abierto) return

    val desc = if (cantidad == 1) {
        "Se destruirá definitivamente la entrada seleccionada. Esta acción no se puede deshacer."
    } else {
        "Se destruirán definitivamente las $cantidad entradas seleccionadas. Esta acción no se puede deshacer."
    }

    DialogoConfirmacionBoveda(
        titulo = "¿Eliminar definitivamente?",
        mensaje = desc,
        textoConfirmar = "Destruir definitivamente",
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.Delete,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}
