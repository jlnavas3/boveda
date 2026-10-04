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
