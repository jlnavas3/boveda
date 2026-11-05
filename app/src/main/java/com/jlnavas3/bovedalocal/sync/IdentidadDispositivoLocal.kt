/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import android.os.Build
import java.util.Locale

/**
 * Genera el nombre o alias legible del dispositivo local para identificación mutua
 * dentro del canal de transporte cifrado (P2P y óptico).
 *
 * Micro-diseño: 1 archivo = 1 responsabilidad pura.
 */
object IdentidadDispositivoLocal {

    /**
     * Devuelve el nombre del dispositivo combinando [Build.MANUFACTURER] y [Build.MODEL]
     * sin duplicar la marca si el modelo ya la contiene (ej. "HONOR DNP-NX9" o "Xiaomi 22101320G").
     */
    fun obtenerNombre(): String {
        return try {
            val fabricante = Build.MANUFACTURER?.trim().orEmpty()
            val modelo = Build.MODEL?.trim().orEmpty()

            when {
                fabricante.isBlank() && modelo.isBlank() -> "Dispositivo Android"
                fabricante.isBlank() -> modelo
                modelo.isBlank() -> capitalizar(fabricante)
                modelo.startsWith(fabricante, ignoreCase = true) -> modelo
                else -> "${capitalizar(fabricante)} $modelo"
            }
        } catch (_: Throwable) {
            "Dispositivo Android"
        }
    }

    fun formatearNombre(fabricante: String, modelo: String): String {
        val f = fabricante.trim()
        val m = modelo.trim()
        return when {
            f.isBlank() && m.isBlank() -> "Dispositivo Android"
            f.isBlank() -> m
            m.isBlank() -> capitalizar(f)
            m.startsWith(f, ignoreCase = true) -> m
            else -> "${capitalizar(f)} $m"
        }
    }

    private fun capitalizar(texto: String): String {
        if (texto.isEmpty()) return texto
        if (texto.length <= 5 && texto == texto.uppercase(Locale.ROOT)) {
            return texto
        }
        return texto.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}
