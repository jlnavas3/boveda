/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.totp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

/**
 * Microcomposable desacoplado que provee los segundos Unix actuales (reloj TOTP)
 * actualizándose cada segundo únicamente mientras el composable que lo invoca
 * permanezca visible y activo en pantalla, evitando recomposiciones innecesarias
 * en contenedores padres como listados principales o tarjetas estáticas.
 */
@Composable
fun rememberSegundosTotpTicker(): Long {
    var segundos by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
    LaunchedEffect(Unit) {
        while (true) {
            segundos = System.currentTimeMillis() / 1000
            delay(1000L)
        }
    }
    return segundos
}
