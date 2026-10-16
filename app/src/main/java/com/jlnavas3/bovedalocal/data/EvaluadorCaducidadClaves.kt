/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Estados de vigencia para contraseñas o credenciales con fecha de caducidad asignada.
 */
enum class EstadoCaducidad {
    EXPIRADA,
    POR_EXPIRAR,
    VIGENTE,
    SIN_CADUCIDAD
}

/**
 * Evaluador desacoplado para calcular el estado, días restantes y representación textual
 * de la caducidad y expiración de contraseñas.
 */
object EvaluadorCaducidadClaves {

    const val UMBRAL_DIAS_AVISO = 15L

    fun evaluar(fechaCaducidad: Long?, ahora: Long = System.currentTimeMillis()): EstadoCaducidad {
        if (fechaCaducidad == null || fechaCaducidad <= 0L) return EstadoCaducidad.SIN_CADUCIDAD
        val restantes = diasRestantes(fechaCaducidad, ahora)
        return when {
            restantes < 0 -> EstadoCaducidad.EXPIRADA
            restantes <= UMBRAL_DIAS_AVISO -> EstadoCaducidad.POR_EXPIRAR
            else -> EstadoCaducidad.VIGENTE
        }
    }

    fun diasRestantes(fechaCaducidad: Long, ahora: Long = System.currentTimeMillis()): Long {
        val diferencia = fechaCaducidad - ahora
        return if (diferencia >= 0) {
            TimeUnit.MILLISECONDS.toDays(diferencia)
        } else {
            -TimeUnit.MILLISECONDS.toDays(-diferencia) - 1
        }
    }

    fun formatearFecha(fechaMs: Long): String {
        return try {
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formato.format(Date(fechaMs))
        } catch (_: Exception) {
            ""
        }
    }

    fun calcularPreset(dias: Int, ahora: Long = System.currentTimeMillis()): Long {
        return ahora + TimeUnit.DAYS.toMillis(dias.toLong())
    }
}
