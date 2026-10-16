/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import android.content.Context
import com.jlnavas3.bovedalocal.crypto.FiltroBloom

/**
 * Auditor desacoplado para verificación offline de contraseñas expuestas en filtraciones públicas
 * usando un Filtro de Bloom optimizado y de cero consumo de red.
 */
object AuditorContrasenasFiltradas {

    private const val ASSET_BLOOM = "filtro_bloom_filtradas.bin"
    private const val BITS_DEFAULT = 524288 // 64 KB
    private const val HASHES_DEFAULT = 5

    @Volatile
    private var filtro: FiltroBloom? = null

    fun obtenerFiltro(contexto: Context): FiltroBloom {
        filtro?.let { return it }
        synchronized(this) {
            filtro?.let { return it }
            val cargado = try {
                contexto.applicationContext.assets.open(ASSET_BLOOM).use { input ->
                    FiltroBloom.desdeStream(input, BITS_DEFAULT, HASHES_DEFAULT)
                }
            } catch (_: Exception) {
                // Si el binario precompilado no está presente, construye el filtro en memoria
                // a partir de las listas de texto locales
                val nuevoFiltro = FiltroBloom(BITS_DEFAULT, HASHES_DEFAULT)
                try {
                    contexto.applicationContext.assets.open("contrasenas_comunes.txt")
                        .bufferedReader(Charsets.UTF_8)
                        .useLines { lineas ->
                            lineas.forEach { linea ->
                                val limpia = linea.trim()
                                if (limpia.isNotEmpty()) {
                                    nuevoFiltro.insertar(limpia)
                                }
                            }
                        }
                } catch (_: Exception) {}
                nuevoFiltro
            }
            filtro = cargado
            return cargado
        }
    }

    fun esFiltrada(
        contexto: Context,
        contrasena: String,
        anadidas: Set<String> = emptySet(),
        excluidas: Set<String> = emptySet()
    ): Boolean {
        if (contrasena.isBlank()) return false
        val claveLimpia = contrasena.trim().lowercase()
        if (anadidas.any { it.trim().lowercase() == claveLimpia }) return true
        if (excluidas.any { it.trim().lowercase() == claveLimpia }) return false

        return obtenerFiltro(contexto).contiene(claveLimpia)
    }

    /** Permite inyectar o reiniciar el filtro (útil para pruebas unitarias). */
    fun reiniciarParaPruebas(nuevoFiltro: FiltroBloom? = null) {
        filtro = nuevoFiltro
    }
}
