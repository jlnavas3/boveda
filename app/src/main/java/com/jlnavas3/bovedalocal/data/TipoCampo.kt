package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

@Serializable
enum class TipoCampo {
    TEXTO,
    NUMERO,
    DECIMAL,
    PIN,
    EMAIL,
    URL,
    TELEFONO,
    FECHA,
    HORA,
    LISTA,
    NOTAS,
    @Deprecated("Usar TEXTO con esSensible = true")
    OCULTO;

    val etiqueta: String
        get() = when (this) {
            TEXTO -> "Texto"
            NUMERO -> "Número"
            DECIMAL -> "Decimal"
            PIN -> "PIN"
            EMAIL -> "Email"
            URL -> "URL"
            TELEFONO -> "Teléfono"
            FECHA -> "Fecha"
            HORA -> "Hora"
            LISTA -> "Lista"
            NOTAS -> "Notas"
            @Suppress("DEPRECATION")
            OCULTO -> "Oculto"
        }
}
