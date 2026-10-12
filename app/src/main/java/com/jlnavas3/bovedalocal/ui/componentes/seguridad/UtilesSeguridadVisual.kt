/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.seguridad

import android.os.Build

/**
 * Utilidades de seguridad visual para enmascarar datos sensibles.
 */
object UtilesSeguridadVisual {

    fun enmascarar(
        texto: String,
        estilo: String,
        longitudFija: Int = 8
    ): String {
        if (texto.isEmpty()) return ""
        return when (estilo) {
            "puntos_reales" -> "•".repeat(texto.length.coerceIn(4, 28))
            else -> "•".repeat(longitudFija)
        }
    }

    fun soportaDesenfoqueHardware(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}
