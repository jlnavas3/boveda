/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data.contactos

/**
 * Modelo inmutable para representar un contacto obtenido de la agenda del dispositivo.
 */
data class ContactoDispositivo(
    val id: String,
    val nombre: String,
    val telefonos: List<String> = emptyList(),
    val correos: List<String> = emptyList(),
    val organizacion: String? = null,
    val cargo: String? = null,
    val notas: String? = null
)
