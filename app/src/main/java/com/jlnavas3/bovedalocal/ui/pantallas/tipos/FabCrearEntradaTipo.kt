/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Botón Flotante (FAB) minimalista para crear una entrada del tipo correspondiente (solo icono +).
 */
@Composable
fun FabCrearEntradaTipo(
    infoTipo: InfoTipoEntrada,
    alCrear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formaFab = RoundedCornerShape(CurvaturaEsquinas)
    val tieneBorde = GrosorBorde > 0.dp && EstiloBorde != "ninguno"

    FloatingActionButton(
        onClick = alCrear,
        containerColor = ColorAcento,
        contentColor = ColorSobreAcento,
        shape = formaFab,
        modifier = modifier.then(
            if (tieneBorde) Modifier.border(GrosorBorde, ColorBordeActual, formaFab) else Modifier
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = infoTipo.descripcionCrear
        )
    }
}
