/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda

/**
 * Función pura encargada de alternar o insertar un comando de filtro en la cadena de búsqueda.
 * Si el comando ya existe, lo remueve (toggle). Si no existe, lo agrega al final con formato limpio.
 */
object ManejadorInsercionFiltro {

    fun alternarFiltro(textoActual: String, comando: String): String {
        val tokens = textoActual.trim().split(Regex("\\s+")).filter { it.isNotBlank() }

        // Si el comando requiere parámetro (ej. tag: o cat:), verificamos si ya existe un prefijo similar
        val esPrefijo = comando.endsWith(":")
        val yaExiste = if (esPrefijo) {
            tokens.any { it.startsWith(comando, ignoreCase = true) }
        } else {
            tokens.any { it.equals(comando, ignoreCase = true) }
        }

        return if (yaExiste) {
            // Lo quitamos
            val filtrados = if (esPrefijo) {
                tokens.filterNot { it.startsWith(comando, ignoreCase = true) }
            } else {
                tokens.filterNot { it.equals(comando, ignoreCase = true) }
            }
            filtrados.joinToString(" ")
        } else {
            // Lo agregamos
            if (textoActual.isBlank()) {
                comando + if (esPrefijo) "" else " "
            } else {
                "${textoActual.trim()} $comando" + if (esPrefijo) "" else " "
            }
        }
    }

    fun estaActivo(textoActual: String, comando: String): Boolean {
        if (textoActual.isBlank()) return false
        val tokens = textoActual.trim().split(Regex("\\s+"))
        return if (comando.endsWith(":")) {
            tokens.any { it.startsWith(comando, ignoreCase = true) }
        } else {
            tokens.any { it.equals(comando, ignoreCase = true) }
        }
    }
}
