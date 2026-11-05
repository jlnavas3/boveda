/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.ItemAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Barra inferior fija de acciones para la pantalla de confirmación de importación CXF:
 * - Acción de Importar credenciales seleccionadas con contador dinámico.
 * - Acción de Cancelar la importación para volver atrás de manera ergonómica.
 */
@Composable
fun BarraAccionesConfirmarImportacionCxf(
    cuantasSeleccionadas: Int,
    alImportar: () -> Unit,
    alCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ItemAccionBarraInferior(
            icono = Icons.Filled.Close,
            etiqueta = "Cancelar",
            alPulsar = alCancelar,
            activo = true,
            colorIcono = ColorAjusteGris,
            descripcion = "Cancelar importación de credenciales"
        ),
        ItemAccionBarraInferior(
            icono = Icons.Filled.FileDownload,
            etiqueta = if (cuantasSeleccionadas > 0) "Importar ($cuantasSeleccionadas)" else "Importar",
            alPulsar = alImportar,
            activo = cuantasSeleccionadas > 0,
            colorIcono = ColorAcento,
            descripcion = "Incorporar credenciales seleccionadas a la bóveda"
        )
    )

    BarraAccionesInferiorBoveda(
        items = items,
        modifier = modifier
    )
}
