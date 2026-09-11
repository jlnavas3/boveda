package com.jlnavas3.bovedalocal.data

import java.util.UUID

/**
 * Lee CSV planos (Google Password Manager, Chrome, Bitwarden, LastPass...) y los
 * convierte en [Entrada]. Todo ocurre en el dispositivo: el archivo ya lo eligió
 * el usuario con el selector del sistema, aquí solo se interpreta su contenido.
 */
object ImportadorCsv {

    private val ALIAS_TITULO = listOf("name", "title", "nombre", "item name", "label")
    private val ALIAS_URL = listOf("url", "urls", "login_uri", "uri", "website", "site", "sitio")
    private val ALIAS_USUARIO = listOf("username", "login_username", "usuario", "user", "email")
    private val ALIAS_CONTRASENA = listOf("password", "login_password", "contraseña", "contrasena", "clave")
    private val ALIAS_NOTAS = listOf("notes", "note", "extra", "notas", "comentario", "comentarios")

    fun parsear(datos: ByteArray): List<Entrada> {
        val texto = String(datos, Charsets.UTF_8).removePrefix("\uFEFF")
        val filas = tokenizar(texto)
        if (filas.size < 2) throw IllegalArgumentException("El CSV no tiene filas de datos")

        val cabecera = filas.first().map { it.trim().lowercase() }
        val idxTitulo = indiceDe(cabecera, ALIAS_TITULO)
        val idxUrl = indiceDe(cabecera, ALIAS_URL)
        val idxUsuario = indiceDe(cabecera, ALIAS_USUARIO)
        val idxContrasena = indiceDe(cabecera, ALIAS_CONTRASENA)
        val idxNotas = indiceDe(cabecera, ALIAS_NOTAS)
        if (idxUsuario == -1 && idxContrasena == -1) {
            throw IllegalArgumentException("No reconozco las columnas de este CSV")
        }

        val ahora = System.currentTimeMillis()
        return filas.drop(1).mapNotNull { fila ->
            fun campo(idx: Int) = if (idx in fila.indices) fila[idx].trim() else ""
            val usuario = campo(idxUsuario)
            val contrasena = campo(idxContrasena)
            val url = campo(idxUrl)
            val titulo = campo(idxTitulo).ifBlank { url.ifBlank { usuario } }
            if (usuario.isBlank() && contrasena.isBlank() && url.isBlank()) return@mapNotNull null
            Entrada(
                id = UUID.randomUUID().toString(),
                titulo = titulo,
                usuario = usuario,
                contrasena = contrasena,
                urls = if (url.isNotBlank()) listOf(url) else emptyList(),
                notas = campo(idxNotas),
                creadaEn = ahora,
                modificadaEn = ahora
            )
        }
    }

    private fun indiceDe(cabecera: List<String>, alias: List<String>): Int =
        alias.firstNotNullOfOrNull { a -> cabecera.indexOf(a).takeIf { it >= 0 } } ?: -1

    /** Tokenizador CSV manual: comillas dobles, comas y saltos de línea dentro de campos. */
    private fun tokenizar(texto: String): List<List<String>> {
        val filas = mutableListOf<List<String>>()
        var fila = mutableListOf<String>()
        val campo = StringBuilder()
        var dentroComillas = false
        var i = 0
        val n = texto.length
        while (i < n) {
            val c = texto[i]
            if (dentroComillas) {
                when {
                    c == '"' && i + 1 < n && texto[i + 1] == '"' -> { campo.append('"'); i++ }
                    c == '"' -> dentroComillas = false
                    else -> campo.append(c)
                }
            } else {
                when (c) {
                    '"' -> dentroComillas = true
                    ',' -> { fila.add(campo.toString()); campo.clear() }
                    '\r' -> {}
                    '\n' -> {
                        fila.add(campo.toString())
                        campo.clear()
                        filas.add(fila)
                        fila = mutableListOf()
                    }
                    else -> campo.append(c)
                }
            }
            i++
        }
        if (campo.isNotEmpty() || fila.isNotEmpty()) {
            fila.add(campo.toString())
            filas.add(fila)
        }
        return filas.filter { f -> f.any { it.isNotBlank() } }
    }
}
