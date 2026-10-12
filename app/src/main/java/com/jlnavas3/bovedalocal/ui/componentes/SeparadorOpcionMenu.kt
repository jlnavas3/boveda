/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes

/**
 * Divisor / separador sutil entre opciones de un menú desplegable, adaptado dinámicamente
 * al tema claro u oscuro para no resaltar excesivamente ni quedar invisible.
 */
@Composable
fun SeparadorOpcionMenu(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(0.8.dp)
            .background(ColorSeparadorAjustes)
    )
}
