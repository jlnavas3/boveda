/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Selector modal del tiempo de disponibilidad para la transferencia manual de credenciales.
 * Si el usuario escoge un tiempo mayor al de bloqueo por inactividad, notifica mediante
 * [alDetectarConflicto] para desplegar el modal de confirmación.
 */
@Composable
fun SelectorTiempoTransferenciaCxf(
    segundosSeleccionados: Int,
    autoBloqueoActualSegundos: Int,
    mostrarId: Boolean,
    alSeleccionar: (Int) -> Unit,
    alDetectarConflicto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val opciones = AlmacenAjustes.OPCIONES_VENTANA_TRANSFERENCIA.map { (segundos, etiqueta) ->
        val descripcion = if (segundos == -1) {
            "Mantiene disponible la exportación hasta que la bóveda sea bloqueada manual o automáticamente"
        } else {
            "La exportación expirará automáticamente tras $etiqueta"
        }
        OpcionSelectorModal(
            valor = segundos,
            etiquetaFila = etiqueta,
            etiquetaModal = etiqueta,
            descripcionModal = descripcion
        )
    }

    ComponenteSelectorModal(
        titulo = "Tiempo de disponibilidad",
        valorSeleccionado = segundosSeleccionados,
        opciones = opciones,
        icono = Icons.Filled.Timer,
        colorIcono = ColorAcento,
        idFila = "05-COP-CXF-TPO",
        mostrarId = mostrarId,
        alSeleccionar = { nuevoTiempo ->
            if (nuevoTiempo > 0 && autoBloqueoActualSegundos > 0 && nuevoTiempo > autoBloqueoActualSegundos) {
                alDetectarConflicto(nuevoTiempo)
            } else {
                alSeleccionar(nuevoTiempo)
            }
        },
        modifier = modifier
    )
}
