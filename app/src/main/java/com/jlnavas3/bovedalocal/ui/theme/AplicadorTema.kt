/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.theme

/** "sistema", "claro" u "oscuro"; "sistema" sigue el tema actual del teléfono. */
fun aplicarTema(claveTema: String, sistemaEnOscuro: Boolean) {
    esOscuroActivo = when (claveTema) {
        "claro" -> false
        "oscuro" -> true
        else -> sistemaEnOscuro
    }
    paletaActiva = if (esOscuroActivo) paletaOscura else paletaClara
}
