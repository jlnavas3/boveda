/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.ItemAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Barra inferior fija de acciones para la pantalla de exportación (.bvda):
 * Acción principal de exportar credenciales seleccionadas con contador dinámico.
 */
@Composable
fun BarraAccionesExportarSelectivo(
    idsSeleccionadosCount: Int,
    exportarHabilitado: Boolean,
    alExportar: () -> Unit,
    modifier: Modifier = Modifier,
    todoMarcado: Boolean = false,
    alDeseleccionarTodo: (() -> Unit)? = null,
    alSeleccionarTodo: (() -> Unit)? = null
) {
    val items = listOf(
        ItemAccionBarraInferior(
            icono = Icons.Filled.Backup,
            etiqueta = if (idsSeleccionadosCount > 0) "Exportar ($idsSeleccionadosCount)" else "Exportar",
            alPulsar = alExportar,
            activo = exportarHabilitado,
            colorIcono = ColorAcento,
            descripcion = "Exportar credenciales seleccionadas"
        )
    )

    BarraAccionesInferiorBoveda(
        items = items,
        modifier = modifier
    )
}
