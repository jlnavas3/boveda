/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Representa un par de comparación (original vs sugerido) para la vista previa
 * rápida antes de estandarizar títulos en 1 toque.
 */
data class EjemploNormalizacion(
    val id: String,
    val tituloOriginal: String,
    val tituloSugerido: String,
    val subtitulo: String = ""
)
