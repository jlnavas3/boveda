/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda

/**
 * Representa un artículo del centro de ayuda interactivo.
 */
data class ArticuloAyuda(
    val id: String,
    val titulo: String,
    val subtitulo: String,
    val grupo: String,
    val palabrasClave: List<String> = emptyList()
)
