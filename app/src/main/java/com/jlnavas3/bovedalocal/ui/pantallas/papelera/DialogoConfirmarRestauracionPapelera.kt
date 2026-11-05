/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

/**
 * Diálogo modal desacoplado para confirmar la restauración de entradas seleccionadas o de toda la papelera.
 */
@Composable
fun DialogoConfirmarRestauracionPapelera(
    abierto: Boolean,
    cantidad: Int,
    esTodaLaPapelera: Boolean,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    if (!abierto) return

    val titulo = if (esTodaLaPapelera) "¿Restaurar toda la papelera?" else "¿Restaurar seleccionadas?"
    val mensaje = if (esTodaLaPapelera) {
        "Se restaurarán todas las entradas ($cantidad) a la bóveda activa."
    } else if (cantidad == 1) {
        "Se restaurará la entrada seleccionada a la bóveda activa."
    } else {
        "Se restaurarán las $cantidad entradas seleccionadas a la bóveda activa."
    }

    DialogoConfirmacionBoveda(
        titulo = titulo,
        mensaje = mensaje,
        textoConfirmar = "Restaurar",
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        iconoHeader = Icons.Filled.RestoreFromTrash,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}
