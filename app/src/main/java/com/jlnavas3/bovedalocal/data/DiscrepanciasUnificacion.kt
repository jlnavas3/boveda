/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Representa los resultados del análisis comparativo entre entradas candidatas a unificarse.
 * Identifica discrepancias de servicios, usuarios, contraseñas y factores de autenticación.
 */
data class DiscrepanciasUnificacion(
    val hayServiciosDiferentes: Boolean,
    val servicios: List<String>,
    val hayUsuariosDiferentes: Boolean,
    val usuarios: List<String>,
    val hayContrasenasDiferentes: Boolean,
    val totalContrasenas: Int,
    val hayTotpsMultiples: Boolean,
    val hayPasskeysMultiples: Boolean,
    val hayTiposDiferentes: Boolean = false,
    val tiposDiferentes: List<TipoEntrada> = emptyList(),
    val advertenciasNativas: List<String> = emptyList()
) {
    val tieneAdvertenciasCriticas: Boolean
        get() = hayServiciosDiferentes || hayTiposDiferentes

    val tieneAlgunaAdvertencia: Boolean
        get() = hayServiciosDiferentes || hayUsuariosDiferentes || hayContrasenasDiferentes ||
                hayTotpsMultiples || hayPasskeysMultiples || hayTiposDiferentes || advertenciasNativas.isNotEmpty()
}
