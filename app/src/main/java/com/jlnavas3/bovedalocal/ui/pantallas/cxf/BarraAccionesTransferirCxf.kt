/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.ItemAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Barra inferior fija de acciones para la pantalla de transferencia interactiva CXF:
 * Acción de transferir credenciales seleccionadas con contador dinámico.
 */
@Composable
fun BarraAccionesTransferirCxf(
    seleccionadasCount: Int,
    alTransferir: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ItemAccionBarraInferior(
            icono = Icons.AutoMirrored.Filled.Send,
            etiqueta = if (seleccionadasCount > 0) "Transferir ($seleccionadasCount)" else "Transferir",
            alPulsar = alTransferir,
            activo = seleccionadasCount > 0,
            colorIcono = ColorAcento,
            descripcion = "Transferir credenciales seleccionadas"
        )
    )

    BarraAccionesInferiorBoveda(
        items = items,
        modifier = modifier
    )
}
