/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.view.autofill.AutofillId

/**
 * Contenedor inmutable de identificadores de vistas para documentos de identidad (cédula, DNI, pasaporte).
 */
data class CamposDetectadosDocumento(
    val numero: AutofillId? = null,
    val nombreCompleto: AutofillId? = null
) {
    val hayAlgo: Boolean
        get() = numero != null
}
