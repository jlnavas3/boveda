/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Constantes para definir el identificador preferido por defecto al autocompletar
 * o asociar un perfil con una credencial.
 */
object PreferenciaIdentificadorPerfil {
    const val CORREO = "correo"
    const val DOCUMENTO = "documento"
}

typealias PreferenciaIdentificadorIdentidad = PreferenciaIdentificadorPerfil
