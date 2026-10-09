package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Generador desacoplado y puro para exportar entradas de tipo Contacto
 * al estándar universal vCard 3.0 (RFC 2426), compatible nativamente con
 * cámaras y agendas de Android e iOS.
 */
object GeneradorVCard {

    /**
     * Escapa caracteres reservados en vCard 3.0: backslash, punto y coma, coma.
     */
    fun escaparVCard(texto: String): String {
        return buildString {
            for (c in texto) {
                when (c) {
                    '\\' -> append("\\\\")
                    ';' -> append("\\;")
                    ',' -> append("\\,")
                    '\n' -> append("\\n")
                    '\r' -> {}
                    else -> append(c)
                }
            }
        }
    }

    /**
     * Convierte una [Entrada] a formato vCard 3.0 estándar.
     */
    fun generarVCard(entrada: Entrada): String = buildString {
        appendLine("BEGIN:VCARD")
        appendLine("VERSION:3.0")

        val nombreCompleto = entrada.titulo.trim()
        if (nombreCompleto.isNotBlank()) {
            appendLine("FN:${escaparVCard(nombreCompleto)}")
            val partes = nombreCompleto.split("\\s+".toRegex())
            if (partes.size > 1) {
                val apellido = partes.last()
                val nombres = partes.dropLast(1).joinToString(" ")
                appendLine("N:${escaparVCard(apellido)};${escaparVCard(nombres)};;;")
            } else {
                appendLine("N:${escaparVCard(nombreCompleto)};;;;")
            }
        }

        // Usuario como teléfono o correo
        val usuario = entrada.usuario.trim()
        if (usuario.isNotBlank()) {
            if (usuario.contains("@")) {
                appendLine("EMAIL;TYPE=INTERNET:${escaparVCard(usuario)}")
            } else if (usuario.any { it.isDigit() }) {
                appendLine("TEL;TYPE=CELL:${escaparVCard(usuario)}")
            }
        }

        var empresa: String? = null
        var cargo: String? = null
        val notasExtra = mutableListOf<String>()

        entrada.camposPersonalizados.forEach { campo ->
            val etq = campo.etiqueta.trim()
            val valCampo = campo.valor.trim()
            if (valCampo.isNotBlank()) {
                val etqLower = etq.lowercase()
                when {
                    etqLower.contains("teléfono") || etqLower.contains("telefono") ||
                    etqLower.contains("celular") || etqLower.contains("móvil") || etqLower.contains("movil") -> {
                        val tipoTel = if (etqLower.contains("trabajo") || etqLower.contains("laboral")) "WORK" else "CELL"
                        appendLine("TEL;TYPE=$tipoTel:${escaparVCard(valCampo)}")
                    }
                    etqLower.contains("correo") || etqLower.contains("email") -> {
                        appendLine("EMAIL;TYPE=INTERNET:${escaparVCard(valCampo)}")
                    }
                    etqLower.contains("empresa") || etqLower.contains("organización") || etqLower.contains("organizacion") -> {
                        empresa = valCampo
                    }
                    etqLower.contains("cargo") || etqLower.contains("puesto") || etqLower.contains("título") -> {
                        cargo = valCampo
                    }
                    etqLower.contains("dirección") || etqLower.contains("direccion") || etqLower.contains("domicilio") -> {
                        appendLine("ADR;TYPE=HOME:;;${escaparVCard(valCampo)};;;;")
                    }
                    etqLower.contains("url") || etqLower.contains("web") || etqLower.contains("sitio") -> {
                        appendLine("URL:${escaparVCard(valCampo)}")
                    }
                    else -> {
                        notasExtra.add("$etq: $valCampo")
                    }
                }
            }
        }

        if (empresa != null) appendLine("ORG:${escaparVCard(empresa!!)}")
        if (cargo != null) appendLine("TITLE:${escaparVCard(cargo!!)}")

        // URLs asociadas a la entrada
        entrada.urls.forEach { url ->
            if (url.isNotBlank()) {
                appendLine("URL:${escaparVCard(url)}")
            }
        }

        // Notas consolidadas
        val todasNotas = buildList {
            if (entrada.notas.isNotBlank()) add(entrada.notas.trim())
            addAll(notasExtra)
        }
        if (todasNotas.isNotEmpty()) {
            appendLine("NOTE:${escaparVCard(todasNotas.joinToString("\n"))}")
        }

        append("END:VCARD")
    }
}
