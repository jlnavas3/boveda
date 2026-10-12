/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

enum class CriterioOrdenacion(val etiqueta: String) {
    NOMBRE_AZ("Nombre (A-Z)"),
    NOMBRE_ZA("Nombre (Z-A)"),
    MODIFICACION_RECIENTE("Modificado recientemente"),
    CREACION_RECIENTE("Añadido recientemente"),
    ANTIGUEDAD("Más antiguos primero"),
    USO_RECIENTE("Último usado"),
    IGNORADAS("Ignoradas primero")
}
