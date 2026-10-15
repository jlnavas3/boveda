/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Micro-componente desacoplado para filtrar entradas de la bóveda mediante sintaxis avanzada:
 * - t:login, t:tarjeta, t:wifi, t:servidor, t:banco, t:wallet, t:identidad, t:passkey, t:nota, t:contacto
 * - tag:nombre (etiquetas)
 * - cat:id (categorías)
 * - has:totp, has:passkey, has:notas
 * - is:fav, fav:true
 * - Búsqueda de texto plano libre en título, usuario, URLs y notas.
 */
object FiltroSintaxisBusqueda {

    fun filtrar(entradas: List<Entrada>, consulta: String): List<Entrada> {
        val q = consulta.trim()
        if (q.isBlank()) return entradas

        val tokens = q.split(Regex("""\s+""")).filter { it.isNotBlank() }
        var resultado = entradas

        val textoLibre = mutableListOf<String>()

        for (token in tokens) {
            val lower = token.lowercase()
            when {
                lower.startsWith("t:") -> {
                    val tipoStr = lower.removePrefix("t:")
                    val tipoEsperado = when (tipoStr) {
                        "login", "clave", "pass", "password" -> TipoEntrada.LOGIN
                        "nota" -> TipoEntrada.NOTA
                        "tarjeta", "card" -> TipoEntrada.TARJETA
                        "wifi", "red" -> TipoEntrada.WIFI
                        "servidor", "ssh", "server" -> TipoEntrada.SERVIDOR
                        "banco", "cuenta" -> TipoEntrada.CUENTA_BANCARIA
                        "wallet", "crypto", "cripto" -> TipoEntrada.WALLET
                        "identidad", "dni", "doc" -> TipoEntrada.IDENTIDAD
                        "passkey", "llave" -> TipoEntrada.PASSKEY
                        "contacto" -> TipoEntrada.CONTACTO
                        else -> null
                    }
                    if (tipoEsperado != null) {
                        resultado = resultado.filter { it.tipo == tipoEsperado }
                    }
                }
                lower.startsWith("tag:") -> {
                    val tagBuscado = lower.removePrefix("tag:")
                    if (tagBuscado.isNotBlank()) {
                        resultado = resultado.filter { ent ->
                            ent.etiquetas.any { it.contains(tagBuscado, ignoreCase = true) }
                        }
                    }
                }
                lower.startsWith("cat:") -> {
                    val catBuscada = lower.removePrefix("cat:")
                    if (catBuscada.isNotBlank()) {
                        resultado = resultado.filter { ent ->
                            ent.categorias.any { it.contains(catBuscada, ignoreCase = true) }
                        }
                    }
                }
                lower == "has:totp" -> {
                    resultado = resultado.filter { !it.secretoTotp.isNullOrBlank() }
                }
                lower == "has:passkey" -> {
                    resultado = resultado.filter { it.passkey != null }
                }
                lower == "has:notas" || lower == "has:nota" -> {
                    resultado = resultado.filter { it.notas.isNotBlank() }
                }
                lower == "is:fav" || lower == "fav:true" || lower == "is:favorito" -> {
                    resultado = resultado.filter { it.favorito }
                }
                lower == "is:ignorado" || lower == "has:ignorado" -> {
                    resultado = resultado.filter { it.ignoradaEnSalud }
                }
                else -> {
                    textoLibre.add(token)
                }
            }
        }

        if (textoLibre.isNotEmpty()) {
            val palabras = textoLibre.map { it.lowercase() }
            resultado = resultado.filter { entrada ->
                palabras.all { palabra ->
                    entrada.titulo.contains(palabra, ignoreCase = true) ||
                    entrada.usuario.contains(palabra, ignoreCase = true) ||
                    entrada.notas.contains(palabra, ignoreCase = true) ||
                    entrada.urls.any { it.contains(palabra, ignoreCase = true) } ||
                    entrada.camposPersonalizados.any {
                        it.etiqueta.contains(palabra, ignoreCase = true) || it.valor.contains(palabra, ignoreCase = true)
                    }
                }
            }
        }

        return resultado
    }
}
