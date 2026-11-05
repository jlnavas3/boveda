/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.view.autofill.AutofillId

/**
 * Acumulador mutable para recopilar identificadores de vistas durante el análisis de documentos de identidad.
 */
class AcumuladorCamposDocumento(
    var numero: AutofillId? = null,
    var nombreCompleto: AutofillId? = null
) {
    fun aCamposDetectados(): CamposDetectadosDocumento = CamposDetectadosDocumento(
        numero = numero,
        nombreCompleto = nombreCompleto
    )
}
