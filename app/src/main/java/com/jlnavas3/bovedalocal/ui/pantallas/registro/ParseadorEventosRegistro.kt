package com.jlnavas3.bovedalocal.ui.pantallas.registro

/**
 * Parsea las líneas crudas del registro de eventos en objetos estructurados EventoRegistro.
 */
fun parsearEventosRegistro(lineas: List<String>): List<EventoRegistro> {
    return lineas.map { linea ->
        val timestamp = if (linea.length >= 14) linea.take(14) else ""
        val resto = if (linea.length > 15) linea.drop(15) else linea
        val area = if (resto.contains(':')) resto.substringBefore(':').trim() else "app"
        val mensaje = if (resto.contains(':')) resto.substringAfter(':').trim() else resto
        val esError = linea.contains("error", ignoreCase = true) ||
            linea.contains("fallo", ignoreCase = true) ||
            linea.contains("exception", ignoreCase = true) ||
            linea.contains("[NO]", ignoreCase = true)
        EventoRegistro(
            timestamp = timestamp,
            area = area,
            mensaje = mensaje,
            esError = esError,
            textoCompleto = linea
        )
    }
}
