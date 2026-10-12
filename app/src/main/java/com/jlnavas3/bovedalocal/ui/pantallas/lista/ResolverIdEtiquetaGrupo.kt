/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

/**
 * Resuelve el identificador de etiqueta de grupo para el menú lateral.
 */
fun resolverIdEtiquetaGrupo(grupo: String): String? {
    return when (grupo) {
        "Herramientas" -> "04-HER"
        "Organización y auditoría" -> "03-LST"
        "Seguridad" -> "01-SEG"
        "Apariencia" -> "02-APA"
        "Copias y datos" -> "05-COP"
        "Sistema" -> "06-SIS"
        else -> null
    }
}
