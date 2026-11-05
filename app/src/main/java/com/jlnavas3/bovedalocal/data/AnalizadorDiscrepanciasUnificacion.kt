/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Analiza una lista de entradas a unificar frente a la entrada principal elegida,
 * extrayendo discrepancias críticas (distintos servicios/plataformas) y de credenciales
 * (usuarios, contraseñas, TOTP, passkeys) para advertir al usuario con claridad.
 */
object AnalizadorDiscrepanciasUnificacion {

    fun analizar(entradas: List<Entrada>, principal: Entrada? = null): DiscrepanciasUnificacion {
        if (entradas.isEmpty()) {
            return DiscrepanciasUnificacion(
                hayServiciosDiferentes = false,
                servicios = emptyList(),
                hayUsuariosDiferentes = false,
                usuarios = emptyList(),
                hayContrasenasDiferentes = false,
                totalContrasenas = 0,
                hayTotpsMultiples = false,
                hayPasskeysMultiples = false
            )
        }

        // 1. Detección de servicios distintos
        val serviciosNormalizados = entradas.map { NormalizadorDuplicados.normalizarServicio(it) }.distinct()
        val hayServiciosDiferentes = serviciosNormalizados.size > 1
        val serviciosVisuales = entradas.map {
            val visual = NormalizadorDuplicados.armarClaveVisual(it)
            if (visual.isNotBlank()) visual else it.titulo.ifBlank { "Sin nombre" }
        }.distinct()

        // 2. Detección de usuarios distintos
        val usuariosDistintos = entradas.map { it.usuario.trim() }
            .filter { it.isNotBlank() }
            .distinct()
        val hayUsuariosDiferentes = usuariosDistintos.size > 1

        // 3. Detección de contraseñas distintas
        val contrasenasDistintas = entradas.map { it.contrasena }
            .filter { it.isNotBlank() }
            .distinct()
        val hayContrasenasDiferentes = contrasenasDistintas.size > 1

        // 4. Múltiples factores TOTP
        val totpsDistintos = entradas.flatMap { e ->
            listOfNotNull(e.secretoTotp) + e.totpsSecundarios.map { it.secreto }
        }.map { it.replace(" ", "").trim().uppercase() }
            .filter { it.isNotBlank() }
            .distinct()
        val hayTotpsMultiples = totpsDistintos.size > 1

        // 5. Múltiples Passkeys
        val passkeysDistintos = entradas.flatMap { e ->
            listOfNotNull(e.passkey) + e.passkeysSecundarias
        }.distinctBy { it.credId.ifBlank { "${it.rpId}:${it.usuario}" } }
        val hayPasskeysMultiples = passkeysDistintos.size > 1

        // 6. Detección de tipos de entrada diferentes
        val tiposDistintos = entradas.map { it.tipo }.distinct()
        val hayTiposDiferentes = tiposDistintos.size > 1

        // 7. Advertencias específicas de submodelos nativos
        val advertenciasNativas = AnalizadorDiscrepanciasNativas.analizar(entradas, principal)

        return DiscrepanciasUnificacion(
            hayServiciosDiferentes = hayServiciosDiferentes,
            servicios = serviciosVisuales,
            hayUsuariosDiferentes = hayUsuariosDiferentes,
            usuarios = usuariosDistintos,
            hayContrasenasDiferentes = hayContrasenasDiferentes,
            totalContrasenas = contrasenasDistintas.size,
            hayTotpsMultiples = hayTotpsMultiples,
            hayPasskeysMultiples = hayPasskeysMultiples,
            hayTiposDiferentes = hayTiposDiferentes,
            tiposDiferentes = tiposDistintos,
            advertenciasNativas = advertenciasNativas
        )
    }
}
