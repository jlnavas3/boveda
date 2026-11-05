/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.view.autofill.AutofillId

/**
 * Contenedor inmutable de identificadores de vistas correspondientes a direcciones físicas y datos de envío.
 */
data class CamposDetectadosDireccion(
    val calle: AutofillId? = null,
    val ciudad: AutofillId? = null,
    val provincia: AutofillId? = null,
    val codigoPostal: AutofillId? = null,
    val pais: AutofillId? = null,
    val nombreCompleto: AutofillId? = null,
    val telefono: AutofillId? = null,
    val correo: AutofillId? = null
) {
    val hayAlgo: Boolean
        get() = calle != null || codigoPostal != null || ciudad != null || provincia != null || pais != null
}
