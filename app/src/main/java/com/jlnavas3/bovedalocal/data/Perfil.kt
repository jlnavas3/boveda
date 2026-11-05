/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Representa un Perfil organizativo de usuario (ej. Personal, Trabajo, Finanzas)
 * vinculado a una dirección de correo principal y alias/correos secundarios.
 */
@Immutable
@Serializable
data class Perfil(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val correoPrincipal: String = "",
    val correosSecundarios: List<String> = emptyList(),
    val tipoDocumento: String? = null,
    val numeroDocumento: String? = null,
    val preferenciaIdentificador: String = PreferenciaIdentificadorPerfil.CORREO,
    val colorHex: String? = null,
    val icono: String = "person",
    val tarjetasIds: List<String> = emptyList(),
    val tarjetaPredeterminadaId: String? = null,
    val cuentasIds: List<String> = emptyList(),
    val cuentaPredeterminadaId: String? = null,
    val contactosIds: List<String> = emptyList(),
    val contactoPredeterminadoId: String? = null,
    val documentosIds: List<String> = emptyList(),
    val documentoPredeterminadoId: String? = null,
    val creadaEn: Long = System.currentTimeMillis(),
    val modificadaEn: Long = System.currentTimeMillis()
) {
    /**
     * Devuelve el identificador que debe usarse por defecto al asociar el perfil
     * a una credencial (Correo o Documento, según la preferencia configurada).
     */
    val identificadorPreferido: String
        get() = if (preferenciaIdentificador == PreferenciaIdentificadorPerfil.DOCUMENTO && !numeroDocumento.isNullOrBlank()) {
            numeroDocumento
        } else if (correoPrincipal.isNotBlank()) {
            correoPrincipal
        } else {
            numeroDocumento.orEmpty()
        }

    val tieneDocumento: Boolean get() = !numeroDocumento.isNullOrBlank()

    val etiquetaDocumentoCompleta: String
        get() = if (tieneDocumento) "${tipoDocumento ?: "ID"}: $numeroDocumento" else ""
}

/**
 * Alias de compatibilidad hacia atrás para facilitar la migración paulatina
 * sin romper referencias heredadas.
 */
typealias Identidad = Perfil
