/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

/**
 * Modos de visualización para las identidades en el listado principal de credenciales.
 */
@Serializable
enum class ModoVisualizacionIdentidades(val clave: String, val etiqueta: String) {
    CHIPS("chips", "Chips de filtro rápido"),
    SECCIONES("secciones", "Secciones agrupadas plegables"),
    DESACTIVADO("desactivado", "Desactivado");

    companion object {
        fun desde(clave: String): ModoVisualizacionIdentidades =
            values().firstOrNull { it.clave.equals(clave, ignoreCase = true) } ?: CHIPS
    }
}
