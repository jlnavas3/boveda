package com.jlnavas3.bovedalocal.data

enum class ModoFormatoTitulos(val etiqueta: String) {
    MINIMALISTA("Solo servicio (Minimalista)"),
    EXPLICITO_PARENTESIS("Servicio (usuario)"),
    EXPLICITO_SEPARADOR("Servicio · usuario");

    companion object {
        fun desde(nombre: String?): ModoFormatoTitulos =
            values().find { it.name.equals(nombre, ignoreCase = true) } ?: EXPLICITO_PARENTESIS
    }
}
