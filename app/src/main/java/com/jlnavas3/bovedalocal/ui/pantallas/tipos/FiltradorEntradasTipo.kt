/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.util.FiltroSintaxisBusqueda

/**
 * Filtra y ordena las entradas de un tipo específico según la búsqueda y el criterio de ordenación.
 */
fun filtrarYOrdenarEntradasTipo(
    entradas: List<Entrada>,
    textoBusqueda: String,
    soloFavoritos: Boolean,
    criterioOrdenacion: CriterioOrdenacion
): List<Entrada> {
    val filtradasPorBase = if (soloFavoritos) entradas.filter { it.favorito } else entradas
    val filtradas = if (textoBusqueda.isBlank()) {
        filtradasPorBase
    } else {
        FiltroSintaxisBusqueda.filtrar(filtradasPorBase, textoBusqueda)
    }
    return filtradas
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
