package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad

/**
 * Resuelve la identidad asociada a una credencial.
 * Prioridad 1: Asignación explícita mediante [Entrada.identidadId].
 * Prioridad 2: Vinculación inteligente automática comparando el nombre de usuario
 * con el correo principal o alias secundarios de las identidades configuradas.
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
        identidad.correoPrincipal.trim().lowercase() == usuarioNormalizado ||
            identidad.correosSecundarios.any { it.trim().lowercase() == usuarioNormalizado }
    }
}
