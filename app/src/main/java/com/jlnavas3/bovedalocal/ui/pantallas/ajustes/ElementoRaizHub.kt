/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

/**
 * Representa un elemento en el nivel raíz del Hub de Ajustes:
 * puede ser un grupo colapsable con subhijos o un ajuste directo individual promovido a la raíz.
 */
sealed interface ElementoRaizHub {
    val id: String

    data class GrupoColapsable(
        val grupo: NodoAjuste,
        val elementos: List<ElementoMenuAjustes>
    ) : ElementoRaizHub {
        override val id: String get() = grupo.id
    }

    data class AjusteDirecto(
        val nodo: NodoAjuste,
        val alPulsar: () -> Unit
    ) : ElementoRaizHub {
        override val id: String get() = nodo.id
    }
}
