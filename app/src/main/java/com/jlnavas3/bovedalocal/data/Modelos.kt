/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

fun normalizarEtiqueta(valor: String): String = valor
    .trim()
    .removePrefix("#")
    .filterNot { it.isWhitespace() }

/** Typealias para compatibilidad hacia atrás durante la transición */
typealias Coleccion = Categoria
