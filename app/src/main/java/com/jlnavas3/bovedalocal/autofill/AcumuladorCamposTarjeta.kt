/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.view.autofill.AutofillId

/**
 * Acumulador mutable para recopilar identificadores de vistas durante el análisis de formularios de pago.
 */
class AcumuladorCamposTarjeta(
    var numeroTarjeta: AutofillId? = null,
    var titular: AutofillId? = null,
    var vencimiento: AutofillId? = null,
    var vencimientoMes: AutofillId? = null,
    var vencimientoAno: AutofillId? = null,
    var cvv: AutofillId? = null
) {
    fun aCamposDetectados(): CamposDetectadosTarjeta = CamposDetectadosTarjeta(
        numeroTarjeta = numeroTarjeta,
        titular = titular,
        vencimiento = vencimiento,
        vencimientoMes = vencimientoMes,
        vencimientoAno = vencimientoAno,
        cvv = cvv
    )
}
