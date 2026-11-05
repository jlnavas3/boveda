/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data.contactos

import java.util.UUID

/**
 * Microcomponente puro para parsear archivos de contactos en formato CSV (RFC 4180).
 *
 * Soporta de forma nativa e inteligente:
 * - Google Contacts CSV (formato oficial de exportación de contactos de Google).
 * - Outlook Contacts CSV (formato de exportación de Microsoft Outlook).
 * - CSV genéricos en español e inglés con cabeceras estándar (Nombre, Teléfono, Correo, Dirección...).
 * - Detección automática de delimitador (coma o punto y coma).
 * - Manejo de comillas, saltos de línea dentro de campos y comillas escapadas.
 */
object ParserCsvContacto {

    /**
     * Parsea un texto CSV y retorna una lista de [ContactoDispositivo].
     */
    fun parsear(contenido: String): List<ContactoDispositivo> {
        if (contenido.isBlank()) return emptyList()

        val delimitador = detectarDelimitador(contenido)
        val filas = parsearFilasCsv(contenido, delimitador)
        if (filas.size < 2) return emptyList()

        val cabecerasOriginales = filas[0].map { it.trim() }
        val cabeceras = cabecerasOriginales.map { normalizarCabecera(it) }
        val contactos = mutableListOf<ContactoDispositivo>()

        for (i in 1 until filas.size) {
            val fila = filas[i]
            if (fila.all { it.isBlank() }) continue

            val mapaValores = mutableMapOf<String, String>()
            val mapaOriginales = mutableMapOf<String, String>()
            for (j in fila.indices) {
                if (j < cabeceras.size) {
                    val claveNorm = cabeceras[j]
                    val claveOrig = cabecerasOriginales[j]
                    val valor = fila[j].trim()
                    if (claveNorm.isNotBlank() && valor.isNotBlank()) {
                        mapaValores[claveNorm] = valor
                        mapaOriginales[claveNorm] = claveOrig
                    }
                }
            }

            val contacto = construirContacto(mapaValores, mapaOriginales)
            if (contacto != null) {
                contactos.add(contacto)
            }
        }

        return contactos
    }

    /**
     * Detecta el delimitador más probable evaluando la primera fila fuera de comillas.
     */
    private fun detectarDelimitador(contenido: String): Char {
        val primeraLinea = contenido.lineSequence().firstOrNull { it.isNotBlank() } ?: return ','
        var comas = 0
        var puntoComas = 0
        var dentroComillas = false

        for (c in primeraLinea) {
            if (c == '"') {
                dentroComillas = !dentroComillas
            } else if (!dentroComillas) {
                if (c == ',') comas++
                else if (c == ';') puntoComas++
            }
        }
        return if (puntoComas > comas) ';' else ','
    }

    /**
     * Parsea el CSV en filas y celdas respetando el estándar RFC 4180.
     */
    fun parsearFilasCsv(texto: String, delimitador: Char): List<List<String>> {
        val filas = mutableListOf<List<String>>()
        val celdaActual = StringBuilder()
        val filaActual = mutableListOf<String>()

        var dentroComillas = false
        var i = 0
        val len = texto.length

        while (i < len) {
            val c = texto[i]

            if (dentroComillas) {
                if (c == '"') {
                    if (i + 1 < len && texto[i + 1] == '"') {
                        // Comilla escapada ""
                        celdaActual.append('"')
                        i += 2
                        continue
                    } else {
                        dentroComillas = false
                    }
                } else {
                    celdaActual.append(c)
                }
            } else {
                when (c) {
                    '"' -> dentroComillas = true
                    delimitador -> {
                        filaActual.add(celdaActual.toString())
                        celdaActual.setLength(0)
                    }
                    '\r' -> {
                        if (i + 1 < len && texto[i + 1] == '\n') {
                            i++
                        }
                        filaActual.add(celdaActual.toString())
                        celdaActual.setLength(0)
                        filas.add(filaActual.toList())
                        filaActual.clear()
                    }
                    '\n' -> {
                        filaActual.add(celdaActual.toString())
                        celdaActual.setLength(0)
                        filas.add(filaActual.toList())
                        filaActual.clear()
                    }
                    else -> celdaActual.append(c)
                }
            }
            i++
        }

        if (celdaActual.isNotEmpty() || filaActual.isNotEmpty()) {
            filaActual.add(celdaActual.toString())
            filas.add(filaActual.toList())
        }

        return filas
    }

    private fun normalizarCabecera(cabecera: String): String {
        return cabecera
            .trim()
            .lowercase()
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ñ", "n")
    }

    /**
     * Extrae y mapea todos los datos disponibles en un [ContactoDispositivo].
     * Aplica estrategia Zero-Loss capturando cualquier columna no estándar en [camposExtra].
     */
    private fun construirContacto(
        valores: Map<String, String>,
        mapaOriginales: Map<String, String> = emptyMap()
    ): ContactoDispositivo? {
        val clavesConsumidas = mutableSetOf<String>()

        // 1. Nombre
        val nombreDirecto = valores["name"] ?: valores["nombre"] ?: valores["full name"] ?: valores["nombre completo"]
        val primerNombre = valores["given name"] ?: valores["first name"] ?: valores["nombre de pila"] ?: ""
        val segundoNombre = valores["additional name"] ?: valores["middle name"] ?: valores["segundo nombre"] ?: ""
        val apellido = valores["family name"] ?: valores["last name"] ?: valores["apellido"] ?: valores["apellidos"] ?: ""
        val apodo = valores["nickname"] ?: valores["apodo"] ?: ""

        clavesConsumidas.addAll(listOf(
            "name", "nombre", "full name", "nombre completo",
            "given name", "first name", "nombre de pila",
            "additional name", "middle name", "segundo nombre",
            "family name", "last name", "apellido", "apellidos",
            "nickname", "apodo", "name prefix", "name suffix", "initials",
            "short name", "maiden name", "yomi name", "given name yomi", "additional name yomi", "family name yomi"
        ))

        val nombreCompuesto = listOf(primerNombre, segundoNombre, apellido).filter { it.isNotBlank() }.joinToString(" ")
        val nombreFinal = when {
            !nombreDirecto.isNullOrBlank() -> nombreDirecto.trim()
            nombreCompuesto.isNotBlank() -> nombreCompuesto.trim()
            apodo.isNotBlank() -> apodo.trim()
            else -> ""
        }

        // 2. Teléfonos
        val telefonos = mutableListOf<String>()
        // Google / Outlook / Genéricos
        for ((clave, valor) in valores) {
            val v = valor.trim()
            if (v.isBlank()) continue

            val esColumnaTelefono = (clave.contains("phone") || clave.contains("telefono") || clave.contains("celular") || clave.contains("movil")) &&
                !clave.contains("type") && !clave.contains("tipo")

            if (esColumnaTelefono) {
                val idx = telefonos.indexOfFirst {
                    FormateadorTelefonoInternacional.sonNumerosEquivalentes(it, v)
                }
                if (idx == -1) {
                    telefonos.add(v)
                } else {
                    telefonos[idx] = FormateadorTelefonoInternacional.mejorRepresentacion(telefonos[idx], v)
                }
            }
            if (clave.contains("phone") || clave.contains("telefono") || clave.contains("celular") || clave.contains("movil")) {
                clavesConsumidas.add(clave)
            }
        }

        // 3. Correos
        val correos = mutableListOf<String>()
        for ((clave, valor) in valores) {
            val v = valor.trim()
            if (v.isBlank()) continue

            val esColumnaEmail = (clave.contains("e-mail") || clave.contains("email") || clave.contains("correo") || clave.contains("mail")) &&
                !clave.contains("type") && !clave.contains("tipo")

            if (esColumnaEmail && !correos.contains(v)) {
                correos.add(v)
            }
            if (clave.contains("e-mail") || clave.contains("email") || clave.contains("correo") || clave.contains("mail")) {
                clavesConsumidas.add(clave)
            }
        }

        // 4. Empresa / Organización y Cargo
        val organizacion = valores["organization 1 - name"]
            ?: valores["company"]
            ?: valores["empresa"]
            ?: valores["organizacion"]
            ?: valores["organization"]

        val cargo = valores["organization 1 - title"]
            ?: valores["job title"]
            ?: valores["cargo"]
            ?: valores["puesto"]
            ?: valores["occupation"]
            ?: valores["title"]

        clavesConsumidas.addAll(listOf(
            "organization 1 - name", "company", "empresa", "organizacion", "organization",
            "organization 1 - title", "job title", "cargo", "puesto", "occupation", "title",
            "organization 1 - type", "organization 1 - yomi name", "organization 1 - symbol", "organization 1 - location", "organization 1 - job description"
        ))

        // 5. Sitios Web
        val sitiosWeb = mutableListOf<String>()
        for ((clave, valor) in valores) {
            val v = valor.trim()
            if (v.isBlank()) continue

            val esColumnaWeb = (clave.contains("website") || clave.contains("web page") || clave.contains("sitio web") || clave.contains("url") || clave == "web") &&
                !clave.contains("type") && !clave.contains("tipo")

            if (esColumnaWeb && !sitiosWeb.contains(v)) {
                sitiosWeb.add(v)
            }
            if (clave.contains("website") || clave.contains("web page") || clave.contains("sitio web") || clave.contains("url") || clave == "web") {
                clavesConsumidas.add(clave)
            }
        }

        // 6. Direcciones
        val direcciones = mutableListOf<DireccionPostalDispositivo>()
        // a) Formato Google Address 1
        val calleG = valores["address 1 - street"] ?: valores["address 1 - formatted"] ?: ""
        val ciudadG = valores["address 1 - city"] ?: ""
        val regionG = valores["address 1 - region"] ?: ""
        val cpG = valores["address 1 - postal code"] ?: ""
        val paisG = valores["address 1 - country"] ?: ""

        // b) Formato Outlook Home Address / Genérico
        val calleO = valores["home street"] ?: valores["direccion"] ?: valores["calle"] ?: valores["street"] ?: valores["domicilio"] ?: ""
        val ciudadO = valores["home city"] ?: valores["ciudad"] ?: valores["city"] ?: valores["localidad"] ?: ""
        val regionO = valores["home state"] ?: valores["provincia"] ?: valores["estado"] ?: valores["region"] ?: valores["state"] ?: ""
        val cpO = valores["home postal code"] ?: valores["codigo postal"] ?: valores["zip"] ?: valores["postal code"] ?: valores["cp"] ?: ""
        val paisO = valores["home country/region"] ?: valores["home country"] ?: valores["pais"] ?: valores["country"] ?: ""

        val calleFinal = calleG.ifBlank { calleO }
        val ciudadFinal = ciudadG.ifBlank { ciudadO }
        val regionFinal = regionG.ifBlank { regionO }
        val cpFinal = cpG.ifBlank { cpO }
        val paisFinal = paisG.ifBlank { paisO }

        if (calleFinal.isNotBlank() || ciudadFinal.isNotBlank() || regionFinal.isNotBlank() || cpFinal.isNotBlank() || paisFinal.isNotBlank()) {
            direcciones.add(
                DireccionPostalDispositivo(
                    calle = calleFinal,
                    ciudad = ciudadFinal,
                    region = regionFinal,
                    codigoPostal = cpFinal,
                    pais = paisFinal,
                    etiqueta = "Principal"
                )
            )
        }

        // Dirección secundaria (ej. Business Address en Outlook / Address 2 en Google)
        val calle2 = valores["address 2 - street"] ?: valores["business street"] ?: ""
        val ciudad2 = valores["address 2 - city"] ?: valores["business city"] ?: ""
        val region2 = valores["address 2 - region"] ?: valores["business state"] ?: ""
        val cp2 = valores["address 2 - postal code"] ?: valores["business postal code"] ?: ""
        val pais2 = valores["address 2 - country"] ?: valores["business country/region"] ?: ""

        if (calle2.isNotBlank() || ciudad2.isNotBlank() || region2.isNotBlank() || cp2.isNotBlank() || pais2.isNotBlank()) {
            direcciones.add(
                DireccionPostalDispositivo(
                    calle = calle2,
                    ciudad = ciudad2,
                    region = region2,
                    codigoPostal = cp2,
                    pais = pais2,
                    etiqueta = "Trabajo"
                )
            )
        }

        clavesConsumidas.addAll(listOf(
            "address 1 - street", "address 1 - formatted", "address 1 - city", "address 1 - region", "address 1 - postal code", "address 1 - country", "address 1 - type", "address 1 - po box", "address 1 - extended address",
            "address 2 - street", "address 2 - formatted", "address 2 - city", "address 2 - region", "address 2 - postal code", "address 2 - country", "address 2 - type", "address 2 - po box", "address 2 - extended address",
            "home street", "home city", "home state", "home postal code", "home country/region", "home country", "home address",
            "business street", "business city", "business state", "business postal code", "business country/region", "business country", "business address",
            "other street", "other city", "other state", "other postal code", "other country/region", "other country", "other address",
            "direccion", "calle", "street", "domicilio", "ciudad", "city", "localidad", "provincia", "estado", "region", "state", "codigo postal", "zip", "postal code", "cp", "pais", "country"
        ))

        // 7. Notas y cumpleaños
        val notasDirectas = valores["notes"] ?: valores["notas"] ?: valores["nota"] ?: valores["comentarios"] ?: ""
        val cumpleanos = valores["birthday"] ?: valores["cumpleanos"] ?: ""
        val notasTotales = mutableListOf<String>()
        if (notasDirectas.isNotBlank()) notasTotales.add(notasDirectas)
        if (cumpleanos.isNotBlank()) notasTotales.add("Cumpleaños: $cumpleanos")

        val metadatosSyncIgnorados = setOf(
            "notes", "notas", "nota", "comentarios", "birthday", "cumpleanos", "cumpleaños",
            "group membership", "group", "groups", "grupo", "grupos",
            "priority", "prioridad", "sensitivity", "sensibilidad", "subject", "asunto",
            "photo", "foto", "mileage", "billing information", "directory server",
            "user 1", "user 2", "user 3", "user 4", "categories", "categoria", "categorias"
        )
        clavesConsumidas.addAll(metadatosSyncIgnorados)

        val notasFinales = if (notasTotales.isNotEmpty()) notasTotales.joinToString("\n\n") else null

        // 8. Ingesta Zero-Loss: Cualquier columna restante legítima se captura en camposExtra
        val camposExtra = mutableMapOf<String, String>()
        for ((clave, valor) in valores) {
            val v = valor.trim()
            val esMetadatoSync = metadatosSyncIgnorados.any { clave == it || clave.startsWith("$it ") || clave.endsWith(" $it") }
            if (v.isNotBlank() && clave !in clavesConsumidas && !esMetadatoSync) {
                val etiqueta = mapaOriginales[clave]?.trim()?.ifBlank { null } ?: clave
                camposExtra[etiqueta] = v
            }
        }

        // Si la fila no contiene ningún dato identificable de contacto, descartarla
        if (nombreFinal.isBlank() && telefonos.isEmpty() && correos.isEmpty() && organizacion.isNullOrBlank() && camposExtra.isEmpty()) {
            return null
        }

        val nombreDefinitivo = when {
            nombreFinal.isNotBlank() -> nombreFinal
            !organizacion.isNullOrBlank() -> organizacion
            telefonos.isNotEmpty() -> telefonos.first()
            correos.isNotEmpty() -> correos.first()
            else -> "Contacto sin nombre"
        }

        return ContactoDispositivo(
            id = UUID.randomUUID().toString(),
            nombre = nombreDefinitivo,
            telefonos = telefonos,
            correos = correos,
            organizacion = organizacion?.ifBlank { null },
            cargo = cargo?.ifBlank { null },
            notas = notasFinales,
            direcciones = direcciones,
            sitiosWeb = sitiosWeb,
            camposExtra = camposExtra
        )
    }
}
