/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Modo de disponibilidad para la transferencia directa de credenciales FIDO CXF.
 */
enum class ModoTransferenciaCxf(val clave: String, val etiqueta: String, val descripcion: String) {
    AUTOMATICA(
        clave = "automatica",
        etiqueta = "Automática",
        descripcion = "Mantiene activa la transferencia mientras la bóveda esté desbloqueada"
    ),
    MANUAL(
        clave = "manual",
        etiqueta = "Manual",
        descripcion = "Solo se activa al solicitarlo por un tiempo determinado"
    );

    companion object {
        fun desde(clave: String): ModoTransferenciaCxf =
            entries.firstOrNull { it.clave.equals(clave, ignoreCase = true) } ?: MANUAL
    }
}
