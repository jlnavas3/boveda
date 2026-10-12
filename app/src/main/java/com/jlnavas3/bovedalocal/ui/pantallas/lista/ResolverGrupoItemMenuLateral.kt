/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes

/**
 * Resuelve el nombre del grupo para un identificador de ítem del menú lateral.
 */
fun resolverGrupoItemMenuLateral(id: String): String {
    return when {
        id in listOf("02-APA-DES", "02-APA-AZX", "02-APA-PLT", "02-APA-FMT", "03-LST-DES", "03-LST-AZX", "03-LST-PLT", "03-LST-FMT") -> "Apariencia"
        id in listOf("03-LST-PAS", "03-LST-2FA", "04-HER-PAS", "04-HER-2FA") -> "Organización y auditoría"
        id.startsWith("04-HER") -> "Herramientas"
        id.startsWith("03-LST") -> "Organización y auditoría"
        id.startsWith("01-SEG") -> "Seguridad"
        id.startsWith("02-APA") -> "Apariencia"
        id.startsWith("05-COP") -> "Copias y datos"
        id == "00-AJU" || id.startsWith("06-SIS") -> "Sistema"
        else -> MapaAjustes.buscarPorId(id)?.grupo ?: "General"
    }
}
