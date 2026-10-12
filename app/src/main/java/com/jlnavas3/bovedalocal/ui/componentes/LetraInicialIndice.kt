/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

fun letraInicialIndice(texto: String, incluirEnie: Boolean = true): Char {
    val limpia = texto.trim()
    if (limpia.isEmpty()) return '#'
    return normalizarCaracterIndice(limpia.first(), incluirEnie)
}
