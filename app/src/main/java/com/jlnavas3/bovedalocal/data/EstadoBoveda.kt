/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

sealed interface EstadoBoveda {
    object SinCrear : EstadoBoveda
    object Bloqueada : EstadoBoveda
    data class Desbloqueada(
        val entradas: List<Entrada>,
        val papelera: List<Entrada> = emptyList(),
        val categorias: List<Categoria> = emptyList(),
        val identidades: List<Identidad> = emptyList()
    ) : EstadoBoveda {
        /** Alias de compatibilidad hacia atrás */
        val colecciones: List<Categoria> get() = categorias
    }
}
