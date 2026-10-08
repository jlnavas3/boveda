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
