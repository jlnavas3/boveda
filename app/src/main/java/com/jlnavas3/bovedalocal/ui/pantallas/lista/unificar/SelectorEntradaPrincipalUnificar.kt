/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.unificar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Lista interactiva de entradas candidatas para permitir al usuario seleccionar cuál queda como principal.
 */
@Composable
fun SelectorEntradaPrincipalUnificar(
    entradas: List<Entrada>,
    entradaPrincipal: Entrada,
    alSeleccionarPrincipal: (Entrada) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        entradas.forEach { entrada ->
            ElementoEntradaUnificar(
                entrada = entrada,
                esPrincipal = entrada.id == entradaPrincipal.id,
                alSeleccionar = { alSeleccionarPrincipal(entrada) }
            )
        }
    }
}
