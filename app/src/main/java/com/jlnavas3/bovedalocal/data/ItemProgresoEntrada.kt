/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Representa una fila individual de entrada o microtarea visible en el streaming
 * del panel de progreso activo.
 */
data class ItemProgresoEntrada(
    val id: String,
    val titulo: String,
    val subtitulo: String = "",
    val progreso: Float = 0f,
    val completado: Boolean = false,
    val esCripto: Boolean = false
)
