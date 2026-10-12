/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

fun normalizarCaracterIndice(c: Char, incluirEnie: Boolean = true): Char {
    val mayus = c.uppercaseChar()
    return when (mayus) {
        'Á', 'À', 'Ä', 'Â', 'Ã' -> 'A'
        'É', 'È', 'Ë', 'Ê' -> 'E'
        'Í', 'Ì', 'Ï', 'Î' -> 'I'
        'Ó', 'Ò', 'Ö', 'Ô', 'Õ' -> 'O'
        'Ú', 'Ù', 'Ü', 'Û' -> 'U'
        'Ñ' -> if (incluirEnie) 'Ñ' else 'N'
        in 'A'..'Z' -> mayus
        else -> '#'
    }
}
