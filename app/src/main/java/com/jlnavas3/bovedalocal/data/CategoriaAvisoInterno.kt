/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Categoría a la que pertenece un aviso interno.
 */
enum class CategoriaAvisoInterno(val etiqueta: String) {
    ORGANIZACION("Organización"),
    SEGURIDAD("Seguridad"),
    MANTENIMIENTO("Mantenimiento")
}
