/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CampoPersonalizado(
    val id: String = UUID.randomUUID().toString(),
    val etiqueta: String = "",
    val valor: String = "",
    val tipo: TipoCampo = TipoCampo.TEXTO,
    val esSensible: Boolean = false,
    val esObligatorio: Boolean = false,
    val formato: String? = null
) {
    @Suppress("DEPRECATION")
    val esSensibleEfectivo: Boolean
        get() = esSensible || tipo == TipoCampo.PIN || tipo == TipoCampo.OCULTO
}
