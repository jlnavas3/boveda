/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.scroll

/**
 * Gestor en memoria que preserva la posición de desplazamiento (índice del primer elemento
 * visible y offset de scroll) de los diferentes listados de la aplicación a lo largo del ciclo
 * de navegación entre pantallas mientras la bóveda permanezca desbloqueada.
 */
class GestorPosicionScroll {
    private val posiciones = mutableMapOf<String, Pair<Int, Int>>()

    fun obtenerPosicion(clave: String): Pair<Int, Int> {
        return posiciones[clave] ?: Pair(0, 0)
    }

    fun guardarPosicion(clave: String, indice: Int, offset: Int) {
        posiciones[clave] = Pair(indice, offset)
    }

    fun limpiarClave(clave: String) {
        posiciones.remove(clave)
    }

    fun limpiarTodo() {
        posiciones.clear()
    }
}
