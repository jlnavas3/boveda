/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.ItemAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Barra inferior fija de acciones para la pantalla de duplicados cuando no hay selección activa.
 * Proporciona acceso a la limpieza rápida masiva de copias idénticas.
 */
@Composable
fun BarraAccionesDuplicadosPrincipal(
    totalSobrantesIdenticas: Int,
    alPedirLimpiezaMasiva: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ItemAccionBarraInferior(
            icono = Icons.Filled.AutoFixHigh,
            etiqueta = "Limpiar ($totalSobrantesIdenticas)",
            alPulsar = alPedirLimpiezaMasiva,
            colorIcono = ColorAcento,
            descripcion = "Limpieza rápida masiva de duplicadas idénticas"
        )
    )

    BarraAccionesInferiorBoveda(
        items = items,
        modifier = modifier
    )
}
