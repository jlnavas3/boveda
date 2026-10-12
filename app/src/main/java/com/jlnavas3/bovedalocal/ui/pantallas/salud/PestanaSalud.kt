/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.salud

import java.util.concurrent.TimeUnit

const val DIAS_AVISO_ANTIGUEDAD = 180L

enum class PestanaSalud(val titulo: String) {
    REPETIDAS("Repetidas"),
    COMUNES("Filtradas"),
    DEBILES("Débiles"),
    ANTIGUAS("Antiguas"),
    IGNORADAS("Ignoradas")
}

fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))
