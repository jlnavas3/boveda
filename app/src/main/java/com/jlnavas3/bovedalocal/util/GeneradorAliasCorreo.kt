/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import java.security.SecureRandom
import java.util.Locale

/**
 * Micro-componente desacoplado para generación de alias de correo electrónico (Email Masking),
 * compatible con el estándar RFC 5233 (Subdireccionamiento / Plus-Addressing) para privacidad y mitigación de spam.
 */
object GeneradorAliasCorreo {

    private val CARACTERES_ALEATORIOS = "abcdefghjkmnpqrstuvwxyz23456789".toCharArray()
    private val random = SecureRandom()

    fun limpiarEtiqueta(texto: String): String {
        return texto.lowercase(Locale.ROOT)
            .replace(Regex("""[^a-z0-9._-]"""), "")
            .trim('.', '-', '_')
    }

    fun generarConEtiqueta(correoBase: String, etiqueta: String, delimitador: String = "+"): String {
        val partes = correoBase.trim().split("@")
        if (partes.size != 2 || partes[0].isBlank() || partes[1].isBlank()) return correoBase

        val usuarioLimpio = when {
            delimitador != "+" && partes[0].contains(delimitador) -> partes[0].substringBefore(delimitador).trim()
            partes[0].contains("+") -> partes[0].substringBefore("+").trim()
            else -> partes[0].trim()
        }
        val etiquetaLimpia = limpiarEtiqueta(etiqueta)
        val delimEfectivo = if (delimitador in listOf("+", "-", ".")) delimitador else "+"

        return if (etiquetaLimpia.isNotBlank()) {
            "$usuarioLimpio$delimEfectivo$etiquetaLimpia@${partes[1].trim()}"
        } else {
            correoBase
        }
    }

    fun generarConAleatorio(correoBase: String, longitud: Int = 6, delimitador: String = "+"): String {
        val sufijo = buildString(longitud) {
            repeat(longitud) {
                append(CARACTERES_ALEATORIOS[random.nextInt(CARACTERES_ALEATORIOS.size)])
            }
        }
        return generarConEtiqueta(correoBase, sufijo, delimitador)
    }

    fun generarParaSitio(
        correoBase: String,
        tituloOSitio: String,
        delimitador: String = "+",
        longitudAleatorio: Int = 6
    ): String {
        val etiqueta = limpiarEtiqueta(tituloOSitio)
        return if (etiqueta.isNotBlank()) {
            generarConEtiqueta(correoBase, etiqueta, delimitador)
        } else {
            generarConAleatorio(correoBase, longitudAleatorio, delimitador)
        }
    }
}
