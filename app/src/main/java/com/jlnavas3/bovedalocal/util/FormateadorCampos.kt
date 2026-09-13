package com.jlnavas3.bovedalocal.util

import java.util.Locale

object FormateadorCampos {

    const val FECHA_DD_MM_AAAA = "DD/MM/AAAA"
    const val FECHA_AAAA_MM_DD = "AAAA-MM-DD"
    const val FECHA_MM_DD_AAAA = "MM/DD/AAAA"
    const val FECHA_DD_GUION_MM_AAAA = "DD-MM-AAAA"

    val OPCIONES_FECHA = listOf(
        FECHA_DD_MM_AAAA to "DD/MM/AAAA (ej. 31/12/2026)",
        FECHA_AAAA_MM_DD to "AAAA-MM-DD (ej. 2026-12-31)",
        FECHA_MM_DD_AAAA to "MM/DD/AAAA (ej. 12/31/2026)",
        FECHA_DD_GUION_MM_AAAA to "DD-MM-AAAA (ej. 31-12-2026)"
    )

    const val HORA_24H = "24h"
    const val HORA_12H = "12h"

    val OPCIONES_HORA = listOf(
        HORA_24H to "24 horas (ej. 19:30)",
        HORA_12H to "12 horas (ej. 07:30 PM)"
    )

    const val TEL_ESPACIOS = "### ### ####"
    const val TEL_GUIONES = "###-###-####"
    const val TEL_PARENTESIS = "(###) ###-####"
    const val TEL_INTERNACIONAL = "+## ### ### ####"
    const val TEL_SIN_MASCARA = "Sin máscara"

    val OPCIONES_TELEFONO = listOf(
        TEL_ESPACIOS to "612 345 678",
        TEL_GUIONES to "612-345-678",
        TEL_PARENTESIS to "(612) 345-678",
        TEL_INTERNACIONAL to "+34 612 345 678",
        TEL_SIN_MASCARA to "Sin máscara (libre)"
    )

    val OPCIONES_DECIMAL = listOf(
        "." to "Punto decimal (ej. 1250.50)",
        "," to "Coma decimal (ej. 1250,50)"
    )

    fun formatearFecha(anio: Int, mes: Int, dia: Int, formato: String): String {
        val d = String.format(Locale.ROOT, "%02d", dia)
        val m = String.format(Locale.ROOT, "%02d", mes + 1)
        val a = String.format(Locale.ROOT, "%04d", anio)
        return when (formato) {
            FECHA_AAAA_MM_DD -> "$a-$m-$d"
            FECHA_MM_DD_AAAA -> "$m/$d/$a"
            FECHA_DD_GUION_MM_AAAA -> "$d-$m-$a"
            else -> "$d/$m/$a"
        }
    }

    fun parsearFecha(valor: String, formato: String): Triple<Int, Int, Int>? {
        if (valor.isBlank()) return null
        return try {
            val delimitador = if (valor.contains("-")) "-" else "/"
            val partes = valor.split(delimitador).map { it.trim().toInt() }
            if (partes.size != 3) return null
            when (formato) {
                FECHA_AAAA_MM_DD -> Triple(partes[0], partes[1] - 1, partes[2])
                FECHA_MM_DD_AAAA -> Triple(partes[2], partes[0] - 1, partes[1])
                else -> Triple(partes[2], partes[1] - 1, partes[0]) // DD/MM/AAAA o DD-MM-AAAA
            }
        } catch (_: Exception) {
            null
        }
    }

    fun formatearHora(hora: Int, minuto: Int, formato: String): String {
        val m = String.format(Locale.ROOT, "%02d", minuto)
        return if (formato == HORA_12H) {
            val periodo = if (hora >= 12) "PM" else "AM"
            val h12 = when {
                hora == 0 -> 12
                hora > 12 -> hora - 12
                else -> hora
            }
            val hStr = String.format(Locale.ROOT, "%02d", h12)
            "$hStr:$m $periodo"
        } else {
            val h24 = String.format(Locale.ROOT, "%02d", hora)
            "$h24:$m"
        }
    }

    fun parsearHora(valor: String, formato: String): Pair<Int, Int>? {
        if (valor.isBlank()) return null
        return try {
            val limpio = valor.trim()
            val esPm = limpio.contains("PM", ignoreCase = true)
            val esAm = limpio.contains("AM", ignoreCase = true)
            val partesStr = limpio.replace("AM", "", ignoreCase = true)
                .replace("PM", "", ignoreCase = true)
                .trim()
                .split(":")
            if (partesStr.size != 2) return null
            var h = partesStr[0].trim().toInt()
            val m = partesStr[1].trim().toInt()
            if (esPm && h < 12) h += 12
            if (esAm && h == 12) h = 0
            Pair(h, m)
        } catch (_: Exception) {
            null
        }
    }

    fun sanitizarNumero(valor: String): String = valor.filter { it.isDigit() }

    fun sanitizarDecimal(valor: String, separador: String): String {
        val sepChar = if (separador == ",") ',' else '.'
        val altChar = if (sepChar == '.') ',' else '.'
        val normalizado = valor.replace(altChar, sepChar)
        val soloValidos = normalizado.filter { it.isDigit() || it == sepChar }
        val partes = soloValidos.split(sepChar)
        return if (partes.size <= 2) soloValidos else partes[0] + sepChar + partes.subList(1, partes.size).joinToString("")
    }

    fun aplicarMascaraTelefono(digitosRaw: String, mascara: String): String {
        if (mascara == TEL_SIN_MASCARA) return digitosRaw
        val tienePlus = digitosRaw.startsWith("+")
        val soloDigitos = digitosRaw.filter { it.isDigit() }
        if (soloDigitos.isEmpty()) return if (tienePlus) "+" else ""

        val resultado = StringBuilder()
        var idxDigito = 0

        for (charMascara in mascara) {
            if (idxDigito >= soloDigitos.length) break
            if (charMascara == '#') {
                resultado.append(soloDigitos[idxDigito])
                idxDigito++
            } else if (charMascara == '+' && tienePlus) {
                resultado.append('+')
            } else if (charMascara != '+') {
                resultado.append(charMascara)
            }
        }

        // Si sobran dígitos más allá de la máscara, agregarlos al final
        if (idxDigito < soloDigitos.length) {
            resultado.append(" ").append(soloDigitos.substring(idxDigito))
        }

        return resultado.toString().trim()
    }
}
