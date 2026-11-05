/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modificador composable que aplica una elevación sincronizada e instantánea a los botones
 * de acción flotantes (FAB) cuando un aviso emergente aparece en pantalla.
 * Al incorporar navigationBarsPadding(), garantiza que la base del botón y del aviso
 * sean idénticas en cualquier dispositivo, independientemente del sistema de navegación (gestos o 3 botones).
 */
@Composable
fun Modifier.desplazarPorAvisoEmergente(): Modifier {
    val desplazamientoObjetivo: Dp = LocalDesplazamientoFabPorAviso.current.value
    return this
        .navigationBarsPadding()
        .then(
            if (desplazamientoObjetivo > 0.dp) {
                Modifier.offset(y = -desplazamientoObjetivo)
            } else {
                Modifier
            }
        )
}

