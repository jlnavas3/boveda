/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Estado inmutable de una operación de importación o procesamiento intensivo en segundo plano.
 */
data class EstadoProgresoOperacion(
    val idOperacion: String,
    val titulo: String,
    val fase: String,
    val progresoGlobal: Float = 0f,
    val itemsActivos: List<ItemProgresoEntrada> = emptyList(),
    val cuentasProcesadas: Int = 0,
    val totalCuentas: Int = 0,
    val enEjecucion: Boolean = true,
    val completado: Boolean = false,
    val error: String? = null,
    val textoAccionResultado: String? = null,
    val textoAccionSecundaria: String? = null,
    val accionResultado: (() -> Unit)? = null,
    val alCerrar: (() -> Unit)? = null
)
