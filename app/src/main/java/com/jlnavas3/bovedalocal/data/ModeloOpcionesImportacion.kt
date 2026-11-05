/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Encapsula la selección granular del usuario para la restauración
 * selectiva de entradas y configuraciones desde un archivo '.bvda'.
 */
data class OpcionesImportacionBvda(
    val tiposEntradaSeleccionados: Set<TipoEntrada> = emptySet(),
    val importarCategorias: Boolean = true,
    val importarIdentidades: Boolean = true,
    val importarReglasUrls: Boolean = true,
    val importarReglasHomelab: Boolean = true,
    val importarAutofillDeteccion: Boolean = true,
    val importarMapeosApps: Boolean = true,
    val importarPlantillas: Boolean = true,
    val importarSeguridadTiempos: Boolean = true,
    val importarSeguridadCripto: Boolean = true,
    val importarSeguridadVisual: Boolean = true,
    val importarFormatosLista: Boolean = true,
    val importarGeneradorTotp: Boolean = true,
    val importarRetencionPapelera: Boolean = true
) {
    /** Calcula el número total de bloques/elementos seleccionados */
    fun totalSeleccionados(totalEntradasSeleccionadas: Int): Int {
        var total = totalEntradasSeleccionadas
        if (importarCategorias) total++
        if (importarIdentidades) total++
        if (importarReglasUrls) total++
        if (importarReglasHomelab) total++
        if (importarAutofillDeteccion) total++
        if (importarMapeosApps) total++
        if (importarPlantillas) total++
        if (importarSeguridadTiempos) total++
        if (importarSeguridadCripto) total++
        if (importarSeguridadVisual) total++
        if (importarFormatosLista) total++
        if (importarGeneradorTotp) total++
        if (importarRetencionPapelera) total++
        return total
    }
}
