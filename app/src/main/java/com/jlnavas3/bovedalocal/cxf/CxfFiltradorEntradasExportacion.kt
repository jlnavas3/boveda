/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.cxf

import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Filtra la lista de entradas disponibles según si la transferencia activa
 * corresponde a una selección personalizada o a la bóveda completa.
 */
object CxfFiltradorEntradasExportacion {

    fun filtrar(
        entradasDisponibles: List<Entrada>,
        idsPreseleccionadas: Set<String>,
        esSeleccionPersonalizada: Boolean
    ): List<Entrada> {
        if (!esSeleccionPersonalizada || idsPreseleccionadas.isEmpty()) {
            return entradasDisponibles
        }
        val filtradas = entradasDisponibles.filter { idsPreseleccionadas.contains(it.id) }
        return if (filtradas.isNotEmpty()) filtradas else entradasDisponibles
    }
}
