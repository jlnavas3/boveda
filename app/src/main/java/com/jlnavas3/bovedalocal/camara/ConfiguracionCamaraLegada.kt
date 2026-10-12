/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.camara

/**
 * Parámetros resueltos de configuración de previsualización y enfoque para la cámara legada.
 */
data class ConfiguracionCamaraLegada(
    val anchoPrevia: Int = 0,
    val altoPrevia: Int = 0,
    val rotacionPrevia: Int = 0,
    val enfoqueManual: Boolean = false
)
