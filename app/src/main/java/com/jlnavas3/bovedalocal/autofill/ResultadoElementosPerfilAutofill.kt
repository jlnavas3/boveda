/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Perfil

/**
 * Agrupación de elementos resueltos para el perfil activo durante la sesión de autocompletado.
 */
data class ResultadoElementosPerfilAutofill(
    val perfil: Perfil?,
    val tarjetas: List<Entrada> = emptyList(),
    val contactos: List<Entrada> = emptyList(),
    val documentos: List<Entrada> = emptyList()
)
