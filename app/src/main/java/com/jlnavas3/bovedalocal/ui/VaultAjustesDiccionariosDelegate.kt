/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico

/**
 * Sub-delegado especializado en la personalización de diccionarios Diceware (palabras añadidas,
 * excluidas y favoritas) y catálogo de contraseñas comunes prohibidas.
 */
interface VaultAjustesDiccionariosDelegate {
    val repositorio: VaultRepository

    fun agregarPalabraDiccionario(idioma: String, palabra: String) {
        val limpia = palabra.trim().lowercase()
        if (limpia.isBlank()) return
        repositorio.ajustes.actualizar { actual ->
            if (idioma.equals("en", ignoreCase = true)) {
                actual.copy(
                    diccionarioAnadidasEn = actual.diccionarioAnadidasEn + limpia,
                    diccionarioExcluidasEn = actual.diccionarioExcluidasEn - limpia
                )
            } else {
                actual.copy(
                    diccionarioAnadidasEs = actual.diccionarioAnadidasEs + limpia,
                    diccionarioExcluidasEs = actual.diccionarioExcluidasEs - limpia
                )
            }
        }
        Diagnostico.apuntar("diccionario", "Palabra añadida al diccionario ($idioma): $limpia")
    }

    fun editarPalabraDiccionario(idioma: String, palabraAnterior: String, palabraNueva: String) {
        val limpiaAnterior = palabraAnterior.trim().lowercase()
        val limpiaNueva = palabraNueva.trim().lowercase()
        if (limpiaNueva.isBlank() || limpiaAnterior == limpiaNueva) return
        repositorio.ajustes.actualizar { actual ->
            if (idioma.equals("en", ignoreCase = true)) {
                val nuevasAnadidas = (actual.diccionarioAnadidasEn - limpiaAnterior) + limpiaNueva
                val nuevasExcluidas = actual.diccionarioExcluidasEn + limpiaAnterior - limpiaNueva
                val nuevasFavoritas = if (actual.diccionarioFavoritasEn.contains(limpiaAnterior)) {
                    actual.diccionarioFavoritasEn - limpiaAnterior + limpiaNueva
                } else actual.diccionarioFavoritasEn
                actual.copy(
                    diccionarioAnadidasEn = nuevasAnadidas,
                    diccionarioExcluidasEn = nuevasExcluidas,
                    diccionarioFavoritasEn = nuevasFavoritas
                )
            } else {
                val nuevasAnadidas = (actual.diccionarioAnadidasEs - limpiaAnterior) + limpiaNueva
                val nuevasExcluidas = actual.diccionarioExcluidasEs + limpiaAnterior - limpiaNueva
                val nuevasFavoritas = if (actual.diccionarioFavoritasEs.contains(limpiaAnterior)) {
                    actual.diccionarioFavoritasEs - limpiaAnterior + limpiaNueva
                } else actual.diccionarioFavoritasEs
                actual.copy(
                    diccionarioAnadidasEs = nuevasAnadidas,
                    diccionarioExcluidasEs = nuevasExcluidas,
                    diccionarioFavoritasEs = nuevasFavoritas
                )
            }
        }
        Diagnostico.apuntar("diccionario", "Palabra modificada ($idioma): $limpiaAnterior -> $limpiaNueva")
    }

    fun eliminarPalabraDiccionario(idioma: String, palabra: String) {
        val limpia = palabra.trim().lowercase()
        if (limpia.isBlank()) return
        repositorio.ajustes.actualizar { actual ->
            if (idioma.equals("en", ignoreCase = true)) {
                actual.copy(
                    diccionarioAnadidasEn = actual.diccionarioAnadidasEn - limpia,
                    diccionarioExcluidasEn = actual.diccionarioExcluidasEn + limpia,
                    diccionarioFavoritasEn = actual.diccionarioFavoritasEn - limpia
                )
            } else {
                actual.copy(
                    diccionarioAnadidasEs = actual.diccionarioAnadidasEs - limpia,
                    diccionarioExcluidasEs = actual.diccionarioExcluidasEs + limpia,
                    diccionarioFavoritasEs = actual.diccionarioFavoritasEs - limpia
                )
            }
        }
        Diagnostico.apuntar("diccionario", "Palabra excluida/eliminada del diccionario ($idioma): $limpia")
    }

    fun alternarFavoritaDiccionario(idioma: String, palabra: String) {
        val limpia = palabra.trim().lowercase()
        if (limpia.isBlank()) return
        repositorio.ajustes.actualizar { actual ->
            if (idioma.equals("en", ignoreCase = true)) {
                val favs = if (actual.diccionarioFavoritasEn.contains(limpia)) {
                    actual.diccionarioFavoritasEn - limpia
                } else {
                    actual.diccionarioFavoritasEn + limpia
                }
                actual.copy(diccionarioFavoritasEn = favs)
            } else {
                val favs = if (actual.diccionarioFavoritasEs.contains(limpia)) {
                    actual.diccionarioFavoritasEs - limpia
                } else {
                    actual.diccionarioFavoritasEs + limpia
                }
                actual.copy(diccionarioFavoritasEs = favs)
            }
        }
    }

    fun restablecerDiccionario(idioma: String) {
        repositorio.ajustes.actualizar { actual ->
            if (idioma.equals("en", ignoreCase = true)) {
                actual.copy(
                    diccionarioAnadidasEn = emptySet(),
                    diccionarioExcluidasEn = emptySet(),
                    diccionarioFavoritasEn = emptySet()
                )
            } else {
                actual.copy(
                    diccionarioAnadidasEs = emptySet(),
                    diccionarioExcluidasEs = emptySet(),
                    diccionarioFavoritasEs = emptySet()
                )
            }
        }
        Diagnostico.apuntar("diccionario", "Diccionario ($idioma) restablecido a los valores de fábrica")
    }

    fun agregarContrasenaComun(contrasena: String) {
        val limpia = contrasena.trim()
        if (limpia.isBlank()) return
        repositorio.ajustes.actualizar { actual ->
            actual.copy(
                contrasenasComunesAnadidas = actual.contrasenasComunesAnadidas + limpia,
                contrasenasComunesExcluidas = actual.contrasenasComunesExcluidas - limpia
            )
        }
        Diagnostico.apuntar("seguridad", "Contraseña común añadida manualmente: $limpia")
    }

    fun eliminarContrasenaComun(contrasena: String) {
        val limpia = contrasena.trim()
        if (limpia.isBlank()) return
        repositorio.ajustes.actualizar { actual ->
            actual.copy(
                contrasenasComunesAnadidas = actual.contrasenasComunesAnadidas - limpia,
                contrasenasComunesExcluidas = actual.contrasenasComunesExcluidas + limpia
            )
        }
        Diagnostico.apuntar("seguridad", "Contraseña excluida de lista común: $limpia")
    }

    fun restablecerContrasenasComunes() {
        repositorio.ajustes.actualizar { actual ->
            actual.copy(
                contrasenasComunesAnadidas = emptySet(),
                contrasenasComunesExcluidas = emptySet()
            )
        }
        Diagnostico.apuntar("seguridad", "Lista de contraseñas comunes restablecida")
    }
}
