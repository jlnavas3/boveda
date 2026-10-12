/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion

/**
 * Filtra y ordena las entradas de un tipo específico según la búsqueda y el criterio de ordenación.
 */
fun filtrarYOrdenarEntradasTipo(
    entradas: List<Entrada>,
    textoBusqueda: String,
    soloFavoritos: Boolean,
    criterioOrdenacion: CriterioOrdenacion
): List<Entrada> {
    val q = textoBusqueda.trim().lowercase()
    return entradas
        .filter { entrada ->
            val coincideTexto = if (q.isBlank()) true else {
                entrada.titulo.lowercase().contains(q) ||
                entrada.usuario.lowercase().contains(q) ||
                entrada.notas.lowercase().contains(q) ||
                entrada.urls.any { it.lowercase().contains(q) } ||
                entrada.etiquetas.any { it.lowercase().contains(q) } ||
                entrada.camposPersonalizados.any { it.etiqueta.lowercase().contains(q) || it.valor.lowercase().contains(q) }
            }
            val coincideFavorito = if (soloFavoritos) entrada.favorito else true
            coincideTexto && coincideFavorito
        }
        .sortedWith { a, b ->
            when (criterioOrdenacion) {
                CriterioOrdenacion.NOMBRE_AZ -> a.titulo.compareTo(b.titulo, ignoreCase = true)
                CriterioOrdenacion.NOMBRE_ZA -> b.titulo.compareTo(a.titulo, ignoreCase = true)
                CriterioOrdenacion.MODIFICACION_RECIENTE -> b.modificadaEn.compareTo(a.modificadaEn)
                CriterioOrdenacion.ANTIGUEDAD -> a.creadaEn.compareTo(b.creadaEn)
                CriterioOrdenacion.CREACION_RECIENTE -> b.creadaEn.compareTo(a.creadaEn)
                CriterioOrdenacion.USO_RECIENTE -> b.ultimoUsoEn.compareTo(a.ultimoUsoEn)
                CriterioOrdenacion.IGNORADAS -> b.ignoradaEnSalud.compareTo(a.ignoradaEnSalud)
            }
        }
}
