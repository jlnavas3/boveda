/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

/**
 * Representa un dominio desglosado con su host original,
 * su dominio base registrable (eTLD+1) y la lista de hosts candidatos
 * ordenados por prioridad para resolución de App Links.
 */
data class DominioNormalizado(
    val hostOriginal: String,
    val dominioRaiz: String?,
    val hostsCandidatos: List<String>
)
