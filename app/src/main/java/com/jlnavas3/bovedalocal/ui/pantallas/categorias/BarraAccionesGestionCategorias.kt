/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.barra.BarraAccionesInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.barra.ItemAccionBarraInferior
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Barra inferior fija de acciones para la pantalla de Gestión de Categorías:
 * Acción para crear una nueva categoría.
 */
@Composable
fun BarraAccionesGestionCategorias(
    alCrearCategoria: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ItemAccionBarraInferior(
            icono = Icons.Filled.AddCircle,
            etiqueta = "Nueva",
            alPulsar = alCrearCategoria,
            colorIcono = ColorAcento,
            descripcion = "Nueva categoría"
        )
    )

    BarraAccionesInferiorBoveda(
        items = items,
        modifier = modifier
    )
}
