/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun BotonPeligro(
    texto: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    alPulsar: () -> Unit
) {
    BotonBorde(
        texto = texto,
        modifier = modifier,
        color = Peligro,
        icono = icono,
        alPulsar = alPulsar
    )
}
