/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data.contactos

import android.telephony.PhoneNumberUtils

/**
 * Microcomponente desacoplado para formatear números de teléfono internacionales y locales
 * apoyándose en PhoneNumberUtils nativo de Android y resolución dinámica de país E.164.
 */
object FormateadorTelefonoInternacional {

    /**
     * Formatea un número telefónico garantizando legibilidad internacional.
     * Preserva códigos de país, limpia separadores rotos y agrupa en bloques canónicos.
     */
    fun formatear(numeroRaw: String, paisPorDefecto: String? = null): String {
        val limpio = numeroRaw.trim()
        if (limpio.isBlank()) return ""

        val isoPais = ResolvedorPaisPrefijoTelefonico.resolverIsoPais(limpio, paisPorDefecto)

        // Intentar formateo oficial de Android según el estándar del país
        val formateadoNativo = try {
            PhoneNumberUtils.formatNumber(limpio, isoPais)
        } catch (_: Exception) {
            null
        }

        if (!formateadoNativo.isNullOrBlank()) {
            return formateadoNativo
        }

        // Fallback elástico canónico si PhoneNumberUtils no aplica máscara
        return formatearBloquesFallback(limpio)
    }

    /**
     * Limpia un número para discado o intents directos (WhatsApp, llamada, SMS),
     * preservando el '+' inicial si existe y dejando únicamente dígitos.
     */
    fun limpiarParaDiscado(numeroRaw: String): String {
        val limpio = numeroRaw.trim()
        val tienePlus = limpio.startsWith("+")
        val soloDigitos = limpio.filter { it.isDigit() }
        return if (tienePlus) "+$soloDigitos" else soloDigitos
    }

    /**
     * Alias de limpiarParaDiscado para consistencia de nomenclatura.
     */
    fun limpiarParaMarcado(numeroRaw: String): String = limpiarParaDiscado(numeroRaw)

    /**
     * Determina si dos cadenas telefónicas representan el mismo número físico,
     * ignorando diferencias de espaciado, guiones, paréntesis, puntos, barras,
     * formato internacional (+593994615671 vs 593994615671) y prefijos de llamada local
     * (ej. 0994615671 vs +593994615671).
     */
    fun sonNumerosEquivalentes(tel1: String?, tel2: String?): Boolean {
        if (tel1.isNullOrBlank() || tel2.isNullOrBlank()) return false
        val t1 = tel1.trim()
        val t2 = tel2.trim()
        if (t1.equals(t2, ignoreCase = true)) return true

        val discado1 = limpiarParaDiscado(t1)
        val discado2 = limpiarParaDiscado(t2)
        if (discado1 == discado2) return true

        val digitos1 = discado1.filter { it.isDigit() }
        val digitos2 = discado2.filter { it.isDigit() }
        if (digitos1.isEmpty() || digitos2.isEmpty()) return false
        if (digitos1 == digitos2) return true

        // Comparación quitando ceros iniciales típicos de discado doméstico (ej. 099... -> 99...)
        val sinCero1 = digitos1.trimStart('0')
        val sinCero2 = digitos2.trimStart('0')
        if (sinCero1 == sinCero2) return true

        // Coincidencia de número de abonado nacional (mínimo 7 dígitos)
        // Ejemplo: Ecuador móvil +593994615671 (12 dig) vs 0994615671 (9 dig sin cero).
        // Diferencia: 3 dígitos ("593").
        val minAbonado = 7
        if (sinCero1.length >= minAbonado && sinCero2.length >= minAbonado) {
            val (largo, corto) = if (sinCero1.length > sinCero2.length) sinCero1 to sinCero2 else sinCero2 to sinCero1
            val dif = largo.length - corto.length
            if (dif in 1..4 && largo.endsWith(corto)) {
                return true
            }
        }

        return false
    }

    /**
     * Selecciona la representación más canónica y completa entre dos números equivalentes
     * (priorizando el que incluye prefijo internacional '+' o mayor cantidad de dígitos).
     */
    fun mejorRepresentacion(tel1: String, tel2: String): String {
        val t1 = tel1.trim()
        val t2 = tel2.trim()
        val tienePlus1 = t1.startsWith("+")
        val tienePlus2 = t2.startsWith("+")
        if (tienePlus1 && !tienePlus2) return t1
        if (tienePlus2 && !tienePlus1) return t2
        val d1 = t1.filter { it.isDigit() }
        val d2 = t2.filter { it.isDigit() }
        return if (d1.length >= d2.length) t1 else t2
    }

    /**
     * Determina si una cadena dada tiene una estructura plausible de número telefónico
     * (entre 7 y 15 dígitos con prefijo internacional o caracteres de formateo).
     */
    fun esNumeroTelefonicoValido(texto: String): Boolean {
        val limpio = texto.trim()
        if (limpio.isBlank()) return false
        val digitos = limpio.filter { it.isDigit() }
        if (digitos.length !in 7..15) return false
        return limpio.all { it.isDigit() || it == '+' || it == ' ' || it == '-' || it == '(' || it == ')' || it == '.' }
    }

    /**
     * Limpia un número específicamente para WhatsApp (número internacional sin signo '+').
     */
    fun limpiarParaWhatsApp(numeroRaw: String, paisPorDefecto: String? = null): String {
        val discado = limpiarParaDiscado(numeroRaw)
        return if (discado.startsWith("+")) {
            discado.removePrefix("+")
        } else {
            // Si no tiene prefijo internacional pero empieza con 00, remover 00
            if (discado.startsWith("00")) {
                discado.removePrefix("00")
            } else {
                // Si es un número local, resolver el prefijo del país por defecto
                val iso = ResolvedorPaisPrefijoTelefonico.resolverIsoPais(discado, paisPorDefecto)
                val prefijoNumerico = obtenerPrefijoPorIso(iso)
                if (prefijoNumerico != null && discado.startsWith("0")) {
                    prefijoNumerico + discado.removePrefix("0")
                } else if (prefijoNumerico != null) {
                    prefijoNumerico + discado
                } else {
                    discado
                }
            }
        }
    }

    /**
     * Divide un número en bloques legibles preservando el prefijo internacional.
     */
    fun formatearEnBloques(numero: String): String = formatearBloquesFallback(numero)

    private fun formatearBloquesFallback(numero: String): String {
        val tienePlus = numero.startsWith("+")
        val digitos = numero.filter { it.isDigit() }
        if (digitos.isEmpty()) return if (tienePlus) "+" else ""

        val sb = StringBuilder()
        if (tienePlus) sb.append("+")

        // Si tiene más de 9 dígitos y es internacional, separar prefijo de 2 o 3 cifras
        if (tienePlus && digitos.length > 7) {
            val prefijoLen = if (digitos.startsWith("1") || digitos.startsWith("7")) 1
            else if (digitos.length >= 11 && (digitos.startsWith("59") || digitos.startsWith("35") || digitos.startsWith("37") || digitos.startsWith("38") || digitos.startsWith("42") || digitos.startsWith("85") || digitos.startsWith("88") || digitos.startsWith("96") || digitos.startsWith("97") || digitos.startsWith("99") || digitos.startsWith("21") || digitos.startsWith("22") || digitos.startsWith("23") || digitos.startsWith("24") || digitos.startsWith("25") || digitos.startsWith("26"))) 3
            else 2

            val prefijo = digitos.take(prefijoLen)
            val resto = digitos.drop(prefijoLen)
            sb.append(prefijo).append(" ")

            val partes = resto.chunked(3)
            sb.append(partes.joinToString(" "))
            return sb.toString().trim()
        }

        // Agrupación estándar de 3 en 3
        val bloques = digitos.chunked(3)
        sb.append(bloques.joinToString(" "))
        return sb.toString().trim()
    }

    private fun obtenerPrefijoPorIso(iso: String): String? {
        return when (iso.uppercase()) {
            "EC" -> "593"
            "IT" -> "39"
            "ES" -> "34"
            "US", "CA" -> "1"
            "MX" -> "52"
            "CO" -> "57"
            "AR" -> "54"
            "PE" -> "51"
            "CL" -> "56"
            "BR" -> "55"
            "UY" -> "598"
            "BO" -> "591"
            "PY" -> "595"
            "VE" -> "58"
            "DE" -> "49"
            "FR" -> "33"
            "GB" -> "44"
            else -> null
        }
    }
}
