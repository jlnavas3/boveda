/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

/**
 * Orquestador microgranular de diálogos modales para la pantalla de duplicados:
 * limpieza masiva en 1 toque y borrado de selección múltiple.
 */
@Composable
fun DialogosDuplicados(
    confirmarLimpiezaMasiva: Boolean,
    totalSobrantesIdenticas: Int,
    alConfirmarLimpiezaMasiva: () -> Unit,
    alDescartarLimpiezaMasiva: () -> Unit,
    confirmarBorradoSeleccion: Boolean,
    cantidadSeleccionados: Int,
    alConfirmarBorradoSeleccion: () -> Unit,
    alDescartarBorradoSeleccion: () -> Unit
) {
    if (confirmarLimpiezaMasiva) {
        DialogoConfirmacionBoveda(
            titulo = "Unificar $totalSobrantesIdenticas copias idénticas",
            mensaje = "Se consolidarán en una sola entrada sin perder datos.",
            textoConfirmar = "Unificar todo",
            tipoConfirmacion = TipoBotonTexto.PRIMARIO,
            iconoHeader = Icons.Filled.AutoFixHigh,
            alConfirmar = alConfirmarLimpiezaMasiva,
            alDescartar = alDescartarLimpiezaMasiva
        )
    }

    if (confirmarBorradoSeleccion) {
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar $cantidadSeleccionados entrada${if (cantidadSeleccionados > 1) "s" else ""}?",
            mensaje = "Las entradas seleccionadas se enviarán a la papelera. Podrás recuperarlas en los próximos 30 días si lo necesitas.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = alConfirmarBorradoSeleccion,
            alDescartar = alDescartarBorradoSeleccion
        )
    }
}
