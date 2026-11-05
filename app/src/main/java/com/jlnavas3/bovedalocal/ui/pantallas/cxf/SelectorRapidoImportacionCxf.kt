/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo

/**
 * Microcomponente desacoplado para los selectores rápidos de importación ("Solo nuevas", "Todas", "Ninguna"),
 * diseñado para operar como stickyHeader en el listado de credenciales CXF.
 */
@Composable
fun SelectorRapidoImportacionCxf(
    totalItems: Int,
    soloNuevasSeleccionadas: Boolean,
    todasSeleccionadas: Boolean,
    ningunaSeleccionada: Boolean,
    alSeleccionarSoloNuevas: () -> Unit,
    alSeleccionarTodas: () -> Unit,
    alSeleccionarNinguna: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorAjustesFondo)
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BotonSeleccionRapida(
            texto = "Solo nuevas",
            activo = soloNuevasSeleccionadas,
            alPulsar = alSeleccionarSoloNuevas
        )
        BotonSeleccionRapida(
            texto = "Todas ($totalItems)",
            activo = todasSeleccionadas,
            alPulsar = alSeleccionarTodas
        )
        BotonSeleccionRapida(
            texto = "Ninguna",
            activo = ningunaSeleccionada,
            alPulsar = alSeleccionarNinguna
        )
    }
}
