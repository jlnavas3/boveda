/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

/**
 * Alineación horizontal de una columna dentro de una tabla Markdown.
 */
enum class AlineacionColumnaTabla {
    IZQUIERDA,
    CENTRO,
    DERECHA
}

/**
 * Metadatos y encabezado de una columna en la tabla.
 */
data class ColumnaTablaMarkdown(
    val encabezado: String,
    val alineacion: AlineacionColumnaTabla = AlineacionColumnaTabla.IZQUIERDA
)

/**
 * Representación en memoria de una tabla Markdown estructurada.
 */
data class ModeloTablaMarkdown(
    val columnas: List<ColumnaTablaMarkdown>,
    val filas: List<List<String>>
)

/**
 * Analizador puro y desacoplado para detectar y extraer tablas en formato GitHub Flavored Markdown (GFM).
 */
object ParserTablaMarkdown {

    /**
     * Determina si la línea dada corresponde a una fila delimitadora de columnas (ej. | :--- | :---: | ---: |).
     */
    fun esFilaDelimitadora(linea: String): Boolean {
        val trim = linea.trim()
        if (!trim.contains("-")) return false
        val celdas = extraerCeldas(trim)
        if (celdas.isEmpty()) return false
        return celdas.all { celda ->
            val c = celda.trim()
            c.isNotEmpty() && c.matches(Regex("^:?-+:?$"))
        }
    }

    /**
     * Extrae las celdas de una línea de tabla, ignorando las barras laterales exteriores si existen.
     */
    fun extraerCeldas(linea: String): List<String> {
        var texto = linea.trim()
        if (texto.startsWith("|")) {
            texto = texto.substring(1)
        }
        if (texto.endsWith("|")) {
            texto = texto.substring(0, texto.length - 1)
        }
        return texto.split("|").map { it.trim() }
    }

    /**
     * Detecta la alineación configurada para la columna a partir de los dos puntos (:---, :---:, ---:).
     */
    fun parsearAlineacion(delimitador: String): AlineacionColumnaTabla {
        val clean = delimitador.trim()
        val empiezaDosPuntos = clean.startsWith(":")
        val terminaDosPuntos = clean.endsWith(":")
        return when {
            empiezaDosPuntos && terminaDosPuntos -> AlineacionColumnaTabla.CENTRO
            terminaDosPuntos -> AlineacionColumnaTabla.DERECHA
            else -> AlineacionColumnaTabla.IZQUIERDA
        }
    }

    /**
     * Analiza una lista de líneas contiguas y genera el [ModeloTablaMarkdown] correspondiente si es válida.
     */
    fun parsear(lineasTabla: List<String>): ModeloTablaMarkdown? {
        if (lineasTabla.size < 2) return null

        val encabezadosStr = extraerCeldas(lineasTabla[0])
        if (encabezadosStr.isEmpty()) return null

        val delimitadoresStr = extraerCeldas(lineasTabla[1])
        if (!esFilaDelimitadora(lineasTabla[1]) || delimitadoresStr.size != encabezadosStr.size) {
            return null
        }

        val columnas = encabezadosStr.mapIndexed { idx, encabezado ->
            val delim = delimitadoresStr.getOrNull(idx) ?: "---"
            ColumnaTablaMarkdown(
                encabezado = encabezado,
                alineacion = parsearAlineacion(delim)
            )
        }

        val filas = mutableListOf<List<String>>()
        for (i in 2 until lineasTabla.size) {
            val filaLine = lineasTabla[i].trim()
            if (filaLine.isBlank() || !filaLine.contains("|")) continue
            val celdas = extraerCeldas(filaLine)
            filas.add(celdas)
        }

        return ModeloTablaMarkdown(columnas = columnas, filas = filas)
    }
}
