/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.seguridad

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

/**
 * Efecto para volver a ocultar un dato sensible tras el tiempo configurado.
 * Si [tiempoSegundos] es <= 0, el dato no se auto-oculta (modo manual).
 */
@Composable
fun TemporizadorAutoOcultar(
    revelado: Boolean,
    tiempoSegundos: Int,
    alAutoOcultar: () -> Unit
) {
    if (revelado && tiempoSegundos > 0) {
        LaunchedEffect(revelado, tiempoSegundos) {
            delay(tiempoSegundos * 1000L)
            alAutoOcultar()
        }
    }
}
