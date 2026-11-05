/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

/**
 * Acciones de formato Markdown estándar disponibles para el editor de notas.
 */
enum class FormatoMarkdownAccion {
    NEGRITA,
    CURSIVA,
    TACHADO,
    TITULO_1,
    TITULO_2,
    LISTA_PUNTOS,
    LISTA_NUMEROS,
    CHECKBOX,
    CODIGO_LINEA,
    BLOQUE_CODIGO,
    CITA,
    SEPARADOR,
    TABLA,
    CENTRAR,
    ALINEAR_DERECHA
}

/**
 * Función utilitaria pura desacoplada que calcula y aplica transformaciones de formato Markdown
 * sobre un [TextFieldValue], preservando o ajustando inteligentemente la posición del cursor y la selección.
 */
object AplicadorFormatoMarkdown {

    fun aplicar(actual: TextFieldValue, accion: FormatoMarkdownAccion): TextFieldValue {
        val texto = actual.text
        val seleccion = actual.selection
        val min = seleccion.min
        val max = seleccion.max
        val haySeleccion = min != max

        return when (accion) {
            FormatoMarkdownAccion.NEGRITA -> envolver(actual, "**", "**")
            FormatoMarkdownAccion.CURSIVA -> envolver(actual, "*", "*")
            FormatoMarkdownAccion.TACHADO -> envolver(actual, "~~", "~~")
            FormatoMarkdownAccion.CODIGO_LINEA -> envolver(actual, "`", "`")
            FormatoMarkdownAccion.BLOQUE_CODIGO -> {
                if (haySeleccion) {
                    envolver(actual, "```\n", "\n```")
                } else {
                    val prefijo = if (min > 0 && texto.getOrNull(min - 1) != '\n') "\n```\n" else "```\n"
                    val sufijo = "\n```\n"
                    val nuevoTexto = texto.substring(0, min) + prefijo + sufijo + texto.substring(max)
                    val posCursor = min + prefijo.length
                    TextFieldValue(nuevoTexto, TextRange(posCursor))
                }
            }
            FormatoMarkdownAccion.TITULO_1 -> aplicarPrefijoLinea(actual, "# ")
            FormatoMarkdownAccion.TITULO_2 -> aplicarPrefijoLinea(actual, "## ")
            FormatoMarkdownAccion.LISTA_PUNTOS -> aplicarPrefijoLinea(actual, "- ")
            FormatoMarkdownAccion.LISTA_NUMEROS -> aplicarPrefijoLinea(actual, "1. ")
            FormatoMarkdownAccion.CHECKBOX -> aplicarPrefijoLinea(actual, "- [ ] ")
            FormatoMarkdownAccion.CITA -> aplicarPrefijoLinea(actual, "> ")
            FormatoMarkdownAccion.SEPARADOR -> {
                val antes = if (min > 0 && texto.getOrNull(min - 1) != '\n') "\n\n---\n\n" else "\n---\n\n"
                val nuevoTexto = texto.substring(0, min) + antes + texto.substring(max)
                val nuevaPos = min + antes.length
                TextFieldValue(nuevoTexto, TextRange(nuevaPos))
            }
            FormatoMarkdownAccion.TABLA -> {
                val plantilla = if (min > 0 && texto.getOrNull(min - 1) != '\n') {
                    "\n\n| Columna 1 | Columna 2 |\n| :--- | ---: |\n| Texto normal | Texto a la derecha |\n\n"
                } else {
                    "| Columna 1 | Columna 2 |\n| :--- | ---: |\n| Texto normal | Texto a la derecha |\n\n"
                }
                val nuevoTexto = texto.substring(0, min) + plantilla + texto.substring(max)
                val nuevaPos = min + plantilla.length
                TextFieldValue(nuevoTexto, TextRange(nuevaPos))
            }
            FormatoMarkdownAccion.CENTRAR -> {
                if (haySeleccion) {
                    envolver(actual, "-> ", " <-")
                } else {
                    val marcador = "->  <-"
                    val nuevoTexto = texto.substring(0, min) + marcador + texto.substring(max)
                    TextFieldValue(nuevoTexto, TextRange(min + 3))
                }
            }
            FormatoMarkdownAccion.ALINEAR_DERECHA -> {
                if (haySeleccion) {
                    envolver(actual, "-> ", "")
                } else {
                    aplicarPrefijoLinea(actual, "-> ")
                }
            }
        }
    }

    private fun envolver(actual: TextFieldValue, prefijo: String, sufijo: String): TextFieldValue {
        val texto = actual.text
        val min = actual.selection.min
        val max = actual.selection.max

        return if (min != max) {
            val seleccionado = texto.substring(min, max)
            val nuevoTexto = texto.substring(0, min) + prefijo + seleccionado + sufijo + texto.substring(max)
            val nuevaSeleccion = TextRange(min + prefijo.length, max + prefijo.length)
            TextFieldValue(nuevoTexto, nuevaSeleccion)
        } else {
            val nuevoTexto = texto.substring(0, min) + prefijo + sufijo + texto.substring(max)
            val posCursor = min + prefijo.length
            TextFieldValue(nuevoTexto, TextRange(posCursor))
        }
    }

    private fun aplicarPrefijoLinea(actual: TextFieldValue, prefijo: String): TextFieldValue {
        val texto = actual.text
        val cursor = actual.selection.min

        val inicioLinea = if (cursor == 0) 0 else {
            val idx = texto.lastIndexOf('\n', cursor - 1)
            if (idx == -1) 0 else idx + 1
        }

        val lineaExistente = texto.substring(inicioLinea)
        if (lineaExistente.startsWith(prefijo)) {
            val nuevoTexto = texto.substring(0, inicioLinea) + lineaExistente.substring(prefijo.length)
            val nuevoCursor = (cursor - prefijo.length).coerceAtLeast(inicioLinea)
            return TextFieldValue(nuevoTexto, TextRange(nuevoCursor))
        }

        val nuevoTexto = texto.substring(0, inicioLinea) + prefijo + texto.substring(inicioLinea)
        val nuevoCursor = cursor + prefijo.length
        return TextFieldValue(nuevoTexto, TextRange(nuevoCursor))
    }
}
