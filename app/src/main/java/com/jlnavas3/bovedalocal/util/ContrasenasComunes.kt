/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import android.content.Context
import com.jlnavas3.bovedalocal.data.AuditorContrasenasFiltradas

/**
 * Comprueba si una contraseña está entre las que más se repiten en filtraciones
 * conocidas. La lista vive dentro del APK (assets/contrasenas_comunes.txt); no se
 * consulta ningún servicio, así que no hace falta ni permiso de red ni conexión.
 */
object ContrasenasComunes {

    @Volatile private var lista: Set<String>? = null

    private fun cargar(contexto: Context): Set<String> {
        lista?.let { return it }
        synchronized(this) {
            lista?.let { return it }
            val leidas = try {
                contexto.applicationContext.assets.open("contrasenas_comunes.txt")
                    .bufferedReader(Charsets.UTF_8)
                    .useLines { lineas -> lineas.map { it.trim().lowercase() }.filter { it.isNotEmpty() }.toHashSet() }
            } catch (e: Exception) {
                emptySet()
            }
            lista = leidas
            return leidas
        }
    }

    fun obtenerBase(contexto: Context): Set<String> = cargar(contexto)

    fun obtenerListaCompleta(
        contexto: Context,
        anadidas: Set<String> = emptySet(),
        excluidas: Set<String> = emptySet()
    ): List<String> {
        val base = cargar(contexto)
        val excluidasNorm = excluidas.map { it.trim().lowercase() }.toSet()
        val filtradas = if (excluidasNorm.isEmpty()) base else base.filterNot { it in excluidasNorm }
        val anadidasNorm = anadidas.map { it.trim().lowercase() }.filter { it.isNotBlank() }
        val todas = (filtradas + anadidasNorm).distinct()
        return todas.sorted()
    }

    fun esComun(
        contexto: Context,
        contrasena: String,
        anadidas: Set<String> = emptySet(),
        excluidas: Set<String> = emptySet()
    ): Boolean {
        if (contrasena.isBlank()) return false
        val claveLimpia = contrasena.trim().lowercase()
        if (anadidas.any { it.trim().lowercase() == claveLimpia }) return true
        if (excluidas.any { it.trim().lowercase() == claveLimpia }) return false
        if (cargar(contexto).contains(claveLimpia)) return true
        return AuditorContrasenasFiltradas.esFiltrada(contexto, claveLimpia, anadidas, excluidas)
    }
}
