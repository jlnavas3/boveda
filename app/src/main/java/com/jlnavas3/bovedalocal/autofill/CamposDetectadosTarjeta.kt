/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.view.autofill.AutofillId

/**
 * Contenedor inmutable de identificadores de vistas correspondientes a campos de pago y tarjeta bancaria.
 */
data class CamposDetectadosTarjeta(
    val numeroTarjeta: AutofillId? = null,
    val titular: AutofillId? = null,
    val vencimiento: AutofillId? = null,
    val vencimientoMes: AutofillId? = null,
    val vencimientoAno: AutofillId? = null,
    val cvv: AutofillId? = null
) {
    val hayAlgo: Boolean
        get() = numeroTarjeta != null || cvv != null || vencimiento != null || vencimientoMes != null || vencimientoAno != null
}
