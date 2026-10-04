package com.jlnavas3.bovedalocal.ui.pantallas.registro

/**
 * Filtra y ordena los eventos según la categoría seleccionada, la consulta de texto y el orden.
 */
fun filtrarYOrdenarEventos(
    eventos: List<EventoRegistro>,
    filtroTexto: String,
    categoriaSeleccionada: String,
    criterioOrden: CriterioOrdenRegistro
): List<EventoRegistro> {
    val filtrados = eventos.filter { ev ->
        val coincideCategoria = when (categoriaSeleccionada) {
            "Todos" -> true
            "Bóveda" -> ev.area.equals("bóveda", ignoreCase = true) ||
                ev.area.equals("boveda", ignoreCase = true)
            "Papelera" -> ev.area.contains("papelera", ignoreCase = true) ||
                ev.mensaje.contains("papelera", ignoreCase = true)
            "Portapapeles" -> ev.area.contains("portapapeles", ignoreCase = true) ||
                ev.mensaje.contains("copiad", ignoreCase = true) ||
                ev.mensaje.contains("portapapeles", ignoreCase = true)
            "2FA" -> ev.area.contains("2fa", ignoreCase = true) ||
                ev.area.contains("totp", ignoreCase = true) ||
                ev.mensaje.contains("2fa", ignoreCase = true) ||
                ev.mensaje.contains("totp", ignoreCase = true) ||
                ev.mensaje.contains("doble factor", ignoreCase = true)
            "Huella" -> ev.area.contains("huella", ignoreCase = true) ||
                ev.area.contains("keystore", ignoreCase = true) ||
                ev.mensaje.contains("biometr", ignoreCase = true)
            "Cámara" -> ev.area.contains("camara", ignoreCase = true) ||
                ev.area.contains("cámara", ignoreCase = true) ||
                ev.area.contains("qr", ignoreCase = true) ||
                ev.mensaje.contains("motor", ignoreCase = true)
            "Autofill" -> ev.area.contains("autofill", ignoreCase = true) ||
                ev.area.contains("credential", ignoreCase = true) ||
                ev.area.contains("passkey", ignoreCase = true) ||
                ev.mensaje.contains("relleno", ignoreCase = true) ||
                ev.mensaje.contains("passkey", ignoreCase = true)
            "Errores" -> ev.esError
            else -> true
        }
        val coincideTexto = if (filtroTexto.isBlank()) true else {
            ev.textoCompleto.contains(filtroTexto, ignoreCase = true)
        }
        coincideCategoria && coincideTexto
    }

    return when (criterioOrden) {
        CriterioOrdenRegistro.RECIENTES -> filtrados.reversed()
        CriterioOrdenRegistro.ANTIGUOS -> filtrados
        CriterioOrdenRegistro.AREA_AZ -> filtrados.sortedWith(
            compareBy({ it.area.lowercase() }, { it.timestamp })
        )
        CriterioOrdenRegistro.AREA_ZA -> filtrados.sortedWith(
            compareByDescending<EventoRegistro> { it.area.lowercase() }.thenByDescending { it.timestamp }
        )
    }
}
