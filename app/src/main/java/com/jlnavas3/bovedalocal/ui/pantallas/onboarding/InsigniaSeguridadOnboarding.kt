/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.InsigniaPildora

/**
 * Insignia visual estilo píldora con las garantías criptográficas esenciales.
 */
@Composable
fun InsigniaSeguridadOnboarding(
    modifier: Modifier = Modifier
) {
    InsigniaPildora(
        texto = "ARGON2ID · AES-256 · SIN CONEXIÓN",
        modifier = modifier
    )
}
