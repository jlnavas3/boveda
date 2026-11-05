/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data.contactos

import com.jlnavas3.bovedalocal.data.Entrada
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.UUID

/**
 * Microcomponente puro para parsear archivos y flujos vCard (.vcf) estándar (versiones 2.1, 3.0 y 4.0)
 * sin requerir permisos de Android (Zero-Permissions SAF).
 *
 * Características:
 * - Despliegue de líneas continuadas (line folding RFC 2425/2426/6350 y Quoted-Printable soft breaks).
 * - Decodificación Quoted-Printable con detección de charset (UTF-8, ISO-8859-1, Windows-1252).
 * - Des-escape de caracteres reservados (\,, \;, \n, \\).
 * - Soporte para archivos con miles de contactos consecutivos (BEGIN:VCARD ... END:VCARD).
 * - Mapeo directo y puro a [ContactoDispositivo] y [Entrada].
 */
object ParserVcardContacto {

    /**
     * Parsea un texto con uno o múltiples bloques vCard y devuelve la lista de [ContactoDispositivo].
     */
    fun parsear(contenido: String): List<ContactoDispositivo> {
        if (contenido.isBlank()) return emptyList()

        val lineasDesplegadas = desplegarLineas(contenido)
        val contactos = mutableListOf<ContactoDispositivo>()

        var enVcard = false
        val lineasActuales = mutableListOf<String>()

        for (linea in lineasDesplegadas) {
            val trimmed = linea.trim()
            if (trimmed.equals("BEGIN:VCARD", ignoreCase = true)) {
                enVcard = true
                lineasActuales.clear()
            } else if (trimmed.equals("END:VCARD", ignoreCase = true)) {
                if (enVcard && lineasActuales.isNotEmpty()) {
                    val contacto = procesarBloqueVcard(lineasActuales)
                    if (contacto != null) {
                        contactos.add(contacto)
                    }
                }
                enVcard = false
                lineasActuales.clear()
            } else if (enVcard) {
                lineasActuales.add(linea)
            }
        }

        // Si el archivo no tenía END:VCARD final pero sí BEGIN:VCARD
        if (enVcard && lineasActuales.isNotEmpty()) {
            val contacto = procesarBloqueVcard(lineasActuales)
            if (contacto != null) {
                contactos.add(contacto)
            }
        }

        return contactos
    }

    /**
     * Parsea el contenido vCard y lo convierte directamente en entradas seguras de la bóveda.
     */
    fun parsearAEntradas(contenido: String): List<Entrada> {
        return parsear(contenido).map { ConversorContactoEntrada.convertir(it) }
    }

    /**
     * Despliega líneas continuadas según las especificaciones RFC (espacio o tab al inicio, o soft-break '=').
     */
    private fun desplegarLineas(contenido: String): List<String> {
        val lineasCrudas = contenido.lines()
        val resultado = mutableListOf<String>()
        var buffer: StringBuilder? = null

        for (linea in lineasCrudas) {
            val sinRetorno = linea.removeSuffix("\r")

            if (sinRetorno.startsWith(" ") || sinRetorno.startsWith("\t")) {
                // Continuación de línea RFC 2426 / 6350
                if (buffer != null) {
                    buffer.append(sinRetorno.substring(1))
                } else {
                    buffer = StringBuilder(sinRetorno.trimStart())
                }
            } else {
                if (buffer != null) {
                    val acum = buffer.toString()
                    if (acum.endsWith("=") && !acum.endsWith("==")) {
                        // Soft break de vCard 2.1 Quoted-Printable
                        buffer.setLength(buffer.length - 1)
                        buffer.append(sinRetorno)
                        continue
                    } else {
                        resultado.add(acum)
                    }
                }
                buffer = StringBuilder(sinRetorno)
            }
        }

        if (buffer != null) {
            resultado.add(buffer.toString())
        }

        return resultado
    }

    /**
     * Procesa todas las propiedades de un bloque individual de vCard.
     */
    private fun procesarBloqueVcard(lineas: List<String>): ContactoDispositivo? {
        var nombreFormateado = ""
        var apellido = ""
        var nombrePila = ""
        var segundoNombre = ""
        val telefonos = mutableListOf<String>()
        val correos = mutableListOf<String>()
        var organizacion: String? = null
        var cargo: String? = null
        val notas = mutableListOf<String>()
        val direcciones = mutableListOf<DireccionPostalDispositivo>()
        val sitiosWeb = mutableListOf<String>()
        val camposExtra = mutableMapOf<String, String>()

        for (linea in lineas) {
            val dosPuntos = linea.indexOf(':')
            if (dosPuntos <= 0) continue

            val cabecera = linea.substring(0, dosPuntos).trim()
            val valorCrudo = linea.substring(dosPuntos + 1)

            val partesCabecera = cabecera.split(';')
            val nombrePropiedad = partesCabecera[0].uppercase().trim()
            val parametros = partesCabecera.drop(1)

            val esQuotedPrintable = parametros.any {
                it.contains("ENCODING=QUOTED-PRINTABLE", ignoreCase = true) ||
                    it.equals("QUOTED-PRINTABLE", ignoreCase = true)
            }

            val charset = extraerCharset(parametros)

            val valor = if (esQuotedPrintable) {
                decodificarQuotedPrintable(valorCrudo, charset)
            } else {
                desescaparVcard(valorCrudo)
            }

            when (nombrePropiedad) {
                "FN" -> {
                    if (nombreFormateado.isBlank() && valor.isNotBlank()) {
                        nombreFormateado = valor.trim()
                    }
                }
                "N" -> {
                    val componentes = valor.split(';').map { it.trim() }
                    apellido = componentes.getOrNull(0) ?: ""
                    nombrePila = componentes.getOrNull(1) ?: ""
                    segundoNombre = componentes.getOrNull(2) ?: ""
                }
                "TEL" -> {
                    val telLimpio = valor.trim()
                    if (telLimpio.isNotBlank()) {
                        val idx = telefonos.indexOfFirst {
                            FormateadorTelefonoInternacional.sonNumerosEquivalentes(it, telLimpio)
                        }
                        if (idx == -1) {
                            telefonos.add(telLimpio)
                        } else {
                            telefonos[idx] = FormateadorTelefonoInternacional.mejorRepresentacion(telefonos[idx], telLimpio)
                        }
                    }
                }
                "EMAIL" -> {
                    val mailLimpio = valor.trim()
                    if (mailLimpio.isNotBlank() && !correos.contains(mailLimpio)) {
                        correos.add(mailLimpio)
                    }
                }
                "ORG" -> {
                    val orgLimpia = valor.split(';').map { it.trim() }.filter { it.isNotBlank() }.joinToString(" - ")
                    if (orgLimpia.isNotBlank()) {
                        organizacion = orgLimpia
                    }
                }
                "TITLE" -> {
                    val cargoLimpio = valor.trim()
                    if (cargoLimpio.isNotBlank()) {
                        cargo = cargoLimpio
                    }
                }
                "NOTE" -> {
                    val notaLimpia = valor.trim()
                    if (notaLimpia.isNotBlank()) {
                        notas.add(notaLimpia)
                    }
                }
                "URL" -> {
                    val urlLimpia = valor.trim()
                    if (urlLimpia.isNotBlank() && !sitiosWeb.contains(urlLimpia)) {
                        sitiosWeb.add(urlLimpia)
                    }
                }
                "ADR" -> {
                    val direccion = parsearDireccion(valor)
                    if (direccion != null) {
                        direcciones.add(direccion)
                    }
                }
                "BDAY" -> {
                    val cumple = valor.trim()
                    if (cumple.isNotBlank()) {
                        camposExtra["Cumpleaños"] = cumple
                    }
                }
                "NICKNAME" -> {
                    val apodo = valor.trim()
                    if (apodo.isNotBlank()) {
                        camposExtra["Apodo"] = apodo
                    }
                }
                "ROLE" -> {
                    val rol = valor.trim()
                    if (rol.isNotBlank()) {
                        camposExtra["Rol"] = rol
                    }
                }
                else -> {
                    if (nombrePropiedad.startsWith("X-") && valor.isNotBlank()) {
                        val propLower = nombrePropiedad.lowercase()
                        if (!propLower.contains("group") && !propLower.contains("membership") && !propLower.contains("sync")) {
                            val etiqueta = nombrePropiedad.removePrefix("X-")
                                .replace('-', ' ')
                                .replace('_', ' ')
                                .trim()
                                .lowercase()
                                .replaceFirstChar { it.uppercase() }
                            if (etiqueta.isNotBlank()) {
                                camposExtra[etiqueta] = valor.trim()
                            }
                        }
                    }
                }
            }
        }

        // Resolución del nombre si FN no estaba presente
        val nombreFinal = when {
            nombreFormateado.isNotBlank() -> nombreFormateado
            nombrePila.isNotBlank() || apellido.isNotBlank() -> {
                listOf(nombrePila, segundoNombre, apellido).filter { it.isNotBlank() }.joinToString(" ")
            }
            organizacion != null -> organizacion
            telefonos.isNotEmpty() -> telefonos.first()
            correos.isNotEmpty() -> correos.first()
            else -> "Contacto sin nombre"
        }

        return ContactoDispositivo(
            id = UUID.randomUUID().toString(),
            nombre = nombreFinal,
            telefonos = telefonos,
            correos = correos,
            organizacion = organizacion,
            cargo = cargo,
            notas = if (notas.isNotEmpty()) notas.joinToString("\n\n") else null,
            direcciones = direcciones,
            sitiosWeb = sitiosWeb,
            camposExtra = camposExtra
        )
    }

    /**
     * Parsea el componente ADR de vCard:
     * post-office-box;extended-address;street-address;locality;region;postal-code;country-name
     */
    private fun parsearDireccion(valor: String): DireccionPostalDispositivo? {
        val comp = valor.split(';').map { it.trim() }
        if (comp.all { it.isBlank() }) return null

        val buzon = comp.getOrNull(0) ?: ""
        val extra = comp.getOrNull(1) ?: ""
        val callePrincipal = comp.getOrNull(2) ?: ""
        val ciudad = comp.getOrNull(3) ?: ""
        val region = comp.getOrNull(4) ?: ""
        val codigoPostal = comp.getOrNull(5) ?: ""
        val pais = comp.getOrNull(6) ?: ""

        val calleCompleta = listOf(callePrincipal, extra, buzon)
            .filter { it.isNotBlank() }
            .joinToString(", ")

        return DireccionPostalDispositivo(
            calle = calleCompleta,
            ciudad = ciudad,
            region = region,
            codigoPostal = codigoPostal,
            pais = pais,
            etiqueta = "Dirección vCard"
        )
    }

    /**
     * Des-escapa secuencias de escape estándar en vCard.
     */
    private fun desescaparVcard(texto: String): String {
        return texto
            .replace("\\,", ",")
            .replace("\\;", ";")
            .replace("\\n", "\n")
            .replace("\\N", "\n")
            .replace("\\\\", "\\")
    }

    /**
     * Extrae el Charset especificado en parámetros vCard (ej. CHARSET=UTF-8).
     */
    private fun extraerCharset(parametros: List<String>): Charset {
        for (param in parametros) {
            val p = param.trim().uppercase()
            if (p.startsWith("CHARSET=")) {
                val nombreCharset = p.substring("CHARSET=".length).trim()
                return try {
                    Charset.forName(nombreCharset)
                } catch (_: Exception) {
                    StandardCharsets.UTF_8
                }
            }
        }
        return StandardCharsets.UTF_8
    }

    /**
     * Decodifica cadenas Quoted-Printable (RFC 2045) utilizadas extensamente en vCard 2.1.
     */
    fun decodificarQuotedPrintable(texto: String, charset: Charset = StandardCharsets.UTF_8): String {
        val out = ByteArrayOutputStream()
        var i = 0
        val len = texto.length

        while (i < len) {
            val c = texto[i]
            if (c == '=') {
                if (i + 2 < len) {
                    val hex = texto.substring(i + 1, i + 3)
                    val byteVal = hex.toIntOrNull(16)
                    if (byteVal != null) {
                        out.write(byteVal)
                        i += 3
                        continue
                    }
                }
                // Si no era un hex válido o era soft-break suelto
                i++
            } else {
                out.write(c.code)
                i++
            }
        }

        return try {
            desescaparVcard(out.toString(charset.name()))
        } catch (_: Exception) {
            desescaparVcard(out.toString(StandardCharsets.UTF_8.name()))
        }
    }
}
