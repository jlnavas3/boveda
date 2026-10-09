package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import java.util.UUID

/**
 * Microcomponente puro para parsear cadenas en formato vCard (2.1, 3.0, 4.0)
 * y convertirlas en una [Entrada] de tipo [TipoEntrada.CONTACTO].
 */
object ParserVCardQr {

    fun esVCard(texto: String): Boolean {
        val t = texto.trim()
        return t.startsWith("BEGIN:VCARD", ignoreCase = true) ||
            t.contains("VERSION:3.0", ignoreCase = true) ||
            t.contains("VERSION:2.1", ignoreCase = true) ||
            t.contains("VERSION:4.0", ignoreCase = true)
    }

    fun parsear(texto: String): Entrada? {
        if (!esVCard(texto)) return null

        var nombreCompleto = ""
        val telefonos = mutableListOf<String>()
        val correos = mutableListOf<String>()
        var organizacion = ""
        var cargo = ""
        var direccion = ""
        val notas = mutableListOf<String>()

        texto.lines().forEach { lineaOriginal ->
            val linea = lineaOriginal.trim()
            val dosPuntosIdx = linea.indexOf(':')
            if (dosPuntosIdx > 0) {
                val clave = linea.substring(0, dosPuntosIdx).uppercase()
                val valor = linea.substring(dosPuntosIdx + 1).trim()
                    .replace("\\,", ",")
                    .replace("\\;", ";")
                    .replace("\\n", "\n")
                    .replace("\\\\", "\\")

                when {
                    clave.startsWith("FN") && nombreCompleto.isBlank() -> {
                        nombreCompleto = valor
                    }
                    clave.startsWith("N") && nombreCompleto.isBlank() -> {
                        val partes = valor.split(';').filter { it.isNotBlank() }
                        nombreCompleto = partes.reversed().joinToString(" ")
                    }
                    clave.startsWith("TEL") -> {
                        if (valor.isNotBlank() && !telefonos.contains(valor)) {
                            telefonos.add(valor)
                        }
                    }
                    clave.startsWith("EMAIL") -> {
                        if (valor.isNotBlank() && !correos.contains(valor)) {
                            correos.add(valor)
                        }
                    }
                    clave.startsWith("ORG") -> {
                        organizacion = valor.replace(";", " - ").trim()
                    }
                    clave.startsWith("TITLE") -> {
                        cargo = valor
                    }
                    clave.startsWith("ADR") -> {
                        val dirLimpia = valor.split(';').filter { it.isNotBlank() }.joinToString(", ")
                        if (dirLimpia.isNotBlank()) direccion = dirLimpia
                    }
                    clave.startsWith("NOTE") -> {
                        if (valor.isNotBlank()) notas.add(valor)
                    }
                }
            }
        }

        if (nombreCompleto.isBlank()) {
            nombreCompleto = telefonos.firstOrNull() ?: correos.firstOrNull() ?: "Contacto QR"
        }

        val usuarioPrincipal = telefonos.firstOrNull() ?: correos.firstOrNull() ?: ""
        val telefonosRestantes = if (usuarioPrincipal.isNotEmpty() && usuarioPrincipal == telefonos.firstOrNull()) {
            telefonos.drop(1)
        } else {
            telefonos
        }

        val correosRestantes = if (usuarioPrincipal.isNotEmpty() && usuarioPrincipal == correos.firstOrNull()) {
            correos.drop(1)
        } else {
            correos
        }

        val campos = mutableListOf<CampoPersonalizado>()

        telefonosRestantes.forEachIndexed { i, tel ->
            campos.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = if (i == 0 && usuarioPrincipal != telefonos.firstOrNull()) "Teléfono" else "Teléfono ${i + 2}",
                    valor = tel,
                    tipo = TipoCampo.TELEFONO,
                    esSensible = false
                )
            )
        }

        correosRestantes.forEachIndexed { i, email ->
            campos.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = if (i == 0) "Correo electrónico" else "Correo secundario",
                    valor = email,
                    tipo = TipoCampo.EMAIL,
                    esSensible = false
                )
            )
        }

        if (organizacion.isNotBlank()) {
            campos.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = "Empresa / Organización",
                    valor = organizacion,
                    tipo = TipoCampo.TEXTO,
                    esSensible = false
                )
            )
        }

        if (cargo.isNotBlank()) {
            campos.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = "Cargo / Puesto",
                    valor = cargo,
                    tipo = TipoCampo.TEXTO,
                    esSensible = false
                )
            )
        }

        if (direccion.isNotBlank()) {
            campos.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = "Dirección",
                    valor = direccion,
                    tipo = TipoCampo.TEXTO,
                    esSensible = false
                )
            )
        }

        val ahora = System.currentTimeMillis()
        return Entrada(
            id = UUID.randomUUID().toString(),
            tipo = TipoEntrada.CONTACTO,
            titulo = nombreCompleto,
            usuario = usuarioPrincipal,
            camposPersonalizados = campos,
            notas = notas.joinToString("\n"),
            creadaEn = ahora,
            modificadaEn = ahora
        )
    }
}
