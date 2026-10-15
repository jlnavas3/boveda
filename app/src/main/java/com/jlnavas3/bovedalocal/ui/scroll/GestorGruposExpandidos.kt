/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.scroll

/**
 * Gestor en memoria que preserva el conjunto de identificadores de grupos expandidos
 * para los listados con agrupación (ej. listado principal por sitio, papelera, duplicados).
 */
class GestorGruposExpandidos {
    private val gruposPorLista = mutableMapOf<String, Set<String>>()

    fun obtenerGrupos(clave: String): Set<String> {
        return gruposPorLista[clave] ?: emptySet()
    }

    fun guardarGrupos(clave: String, grupos: Set<String>) {
        gruposPorLista[clave] = grupos
    }

    fun alternarGrupo(clave: String, idGrupo: String) {
        val actuales = gruposPorLista[clave] ?: emptySet()
        gruposPorLista[clave] = if (actuales.contains(idGrupo)) {
            actuales - idGrupo
        } else {
            actuales + idGrupo
        }
    }

    fun limpiarClave(clave: String) {
        gruposPorLista.remove(clave)
    }

    fun limpiarTodo() {
        gruposPorLista.clear()
    }
}
