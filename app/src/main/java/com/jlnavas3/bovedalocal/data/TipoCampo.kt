/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
    BOOLEANO,
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
            BOOLEANO -> "Sí/No (Booleano)"
            @Suppress("DEPRECATION")
            OCULTO -> "Oculto"
        }
}
