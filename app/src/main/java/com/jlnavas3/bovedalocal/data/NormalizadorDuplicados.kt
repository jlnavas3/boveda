/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import com.jlnavas3.bovedalocal.util.Dominios

object NormalizadorDuplicados {

    fun esAppAndroid(entrada: Entrada): Boolean {
        return entrada.urls.any { it.trim().startsWith("android://", ignoreCase = true) }
    }

    fun extraerPaqueteAndroid(url: String): String {
        val u = url.trim()
        if (!u.startsWith("android://", ignoreCase = true)) return ""
        val sinEsquema = u.substringAfter("://", u)
        val sinRuta = sinEsquema.substringBefore('/').substringBefore('?').substringBefore('#')
        val sinCredenciales = sinRuta.substringAfterLast('@')
        return sinCredenciales.substringBefore(':').trim().lowercase()
    }

    /**
     * Normaliza el servicio / sitio web o app de Android de la entrada:
     * Unifica de forma canónica las apps móviles y sitios web al mismo servicio base
     * para que cuentas de la misma plataforma (web o móvil) se reconozcan como duplicadas.
     */
    fun normalizarServicio(entrada: Entrada): String {
        val url = entrada.urls.firstOrNull { it.isNotBlank() } ?: ""
        val base = com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios.extraerNombreBase(
            urlODominio = url,
            rawTitulo = entrada.titulo
        ).trim().lowercase()

        if (base.isNotBlank() && base != "cuenta") {
            return base
        }

        val tit = entrada.titulo.trim().lowercase()
        if (tit.isNotBlank()) return tit

        return "desconocido"
    }

    fun armarClaveVisual(entrada: Entrada): String {
        val esApp = esAppAndroid(entrada)
        val url = entrada.urls.firstOrNull { it.isNotBlank() } ?: ""
        val nombreServicio = if (esApp) {
            val tit = entrada.titulo.trim()
            if (tit.isNotBlank() && !tit.equals("android", ignoreCase = true)) {
                tit
            } else {
                com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios.extraerNombreBase(
                    urlODominio = url,
                    rawTitulo = entrada.titulo
                ).ifBlank { "App Android" }
            }
        } else {
            com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios.extraerNombreBase(
                urlODominio = url,
                rawTitulo = entrada.titulo
            ).ifBlank { entrada.titulo.ifBlank { "Sin título" } }
        }

        val usuario = entrada.usuario.ifBlank { "Sin usuario" }
        return if (esApp) "$nombreServicio (App Android) · $usuario" else "$nombreServicio · $usuario"
    }

    /**
     * Normaliza el nombre de usuario según el servicio para detectar variantes de la misma cuenta:
     * - En Google / Gmail: "usuario@gmail.com" y "usuario" representan la misma cuenta de Google.
     * - En Yahoo: "usuario@yahoo.com" y "usuario" representan la misma cuenta.
     * - En Microsoft / Outlook: "usuario@outlook.com" y "usuario" representan la misma cuenta.
     * - Si el correo termina en @<dominioServicio> (ej. en spotify.com "juan@spotify.com" y "juan").
     * - Números telefónicos: unifica formato internacional y local (ej. +593991234567 y 0991234567).
     *
     * IMPORTANTE: Cuentas con usuarios completamente distintos (ej. "usuario_uno" vs "usuario_dos")
     * o correos con dominios institucionales distintos (ej. "estudiante@universidad.edu" vs "estudiante@gmail.com")
     * NUNCA son equivalentes y se mantienen estrictamente separadas.
     */
    fun normalizarUsuario(usuario: String, servicio: String): String {
        val u = usuario.trim().lowercase()
        if (u.isBlank()) return ""

        val servLower = servicio.lowercase()

        // 1. Google / Gmail:
        // "@googlemail.com" es el alias histórico idéntico de "@gmail.com"
        if (u.endsWith("@googlemail.com")) {
            return u.removeSuffix("@googlemail.com") + "@gmail.com"
        }
        if ((servLower.contains("google") || servLower.contains("gmail")) && !u.contains('@')) {
            return "$u@gmail.com"
        }
        if (u.endsWith("@gmail.com")) {
            return u
        }

        // 2. Si el servicio es un dominio propio (ej. en spotify.com usuario es 'juan' y otro 'juan@spotify.com'):
        // Solo unificar si una entrada omitió la arroba y la otra puso @<dominioServicio>
        val dominioServ = servLower.substringAfter("web:").substringAfter("app:")
        if (dominioServ.isNotBlank() && dominioServ.contains('.') && !u.contains('@')) {
            return "$u@$dominioServ"
        }

        // Normalización de números telefónicos (quitar espacios, guiones, paréntesis)
        if (u.matches(Regex("""^\+?[\d\s\-()]+$""")) && u.count { it.isDigit() } >= 7) {
            var digitos = u.filter { it.isDigit() }
            if (digitos.startsWith("593") && digitos.length == 12) {
                digitos = "0" + digitos.removePrefix("593")
            }
            return digitos
        }

        return u
    }
}
