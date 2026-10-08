package com.jlnavas3.bovedalocal.ui.pantallas.lista

import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes

/**
 * Resuelve el nombre del grupo para un identificador de ítem del menú lateral.
 */
fun resolverGrupoItemMenuLateral(id: String): String {
    return when {
        id.startsWith("04-HER") -> "Herramientas"
        id.startsWith("03-LST") -> "Organización y auditoría"
        id.startsWith("01-SEG") -> "Seguridad"
        id.startsWith("02-APA") -> "Apariencia"
        id.startsWith("05-COP") -> "Copias y datos"
        id == "00-AJU" || id.startsWith("06-SIS") -> "Sistema"
        else -> MapaAjustes.buscarPorId(id)?.grupo ?: "General"
    }
}
