/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.salud

import com.jlnavas3.bovedalocal.data.Entrada
import java.text.Normalizer

private fun normalizarTexto(texto: String): String {
    return Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .lowercase()
}

/**
 * Comprueba si una credencial coincide con la consulta de búsqueda en la sección de salud,
 * comparando título, usuario, URLs, etiquetas y notas sin distinguir tildes ni mayúsculas.
 */
fun coincideBusquedaSalud(entrada: Entrada, consulta: String): Boolean {
    if (consulta.isBlank()) return true
    val q = normalizarTexto(consulta.trim())
    return normalizarTexto(entrada.titulo).contains(q) ||
           normalizarTexto(entrada.usuario).contains(q) ||
           entrada.urls.any { normalizarTexto(it).contains(q) } ||
           entrada.etiquetas.any { normalizarTexto(it).contains(q) } ||
           normalizarTexto(entrada.notas).contains(q)
}
