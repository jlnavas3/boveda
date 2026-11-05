/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad

/**
 * Resuelve la identidad asociada a una credencial.
 * Prioridad 1: Asignación explícita mediante [Entrada.identidadId].
 * Prioridad 2: Vinculación inteligente automática comparando el nombre de usuario
 * con el correo principal, alias secundarios o número de documento nacional de identidad.
 */
fun resolverIdentidadParaEntrada(entrada: Entrada, identidades: List<Identidad>): Identidad? {
    if (identidades.isEmpty()) return null

    if (!entrada.identidadId.isNullOrBlank()) {
        val explicita = identidades.firstOrNull { it.id == entrada.identidadId }
        if (explicita != null) return explicita
    }

    val usuarioNormalizado = entrada.usuario.trim().lowercase()
    if (usuarioNormalizado.isBlank()) return null

    return identidades.firstOrNull { identidad ->
        (identidad.correoPrincipal.isNotBlank() && identidad.correoPrincipal.trim().lowercase() == usuarioNormalizado) ||
            identidad.correosSecundarios.any { it.trim().lowercase() == usuarioNormalizado } ||
            (!identidad.numeroDocumento.isNullOrBlank() && esMismoDocumento(identidad.numeroDocumento, usuarioNormalizado))
    }
}

fun esMismoDocumento(docIdentidad: String, docUsuario: String): Boolean {
    val norm1 = docIdentidad.trim().lowercase()
    val norm2 = docUsuario.trim().lowercase()
    if (norm1 == norm2) return true
    val limpio1 = norm1.filter { it.isLetterOrDigit() }
    val limpio2 = norm2.filter { it.isLetterOrDigit() }
    return limpio1.isNotBlank() && limpio1 == limpio2
}
