/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.view.autofill.AutofillId

/**
 * Acumulador mutable para recopilar identificadores de vistas durante el análisis de formularios de direcciones y envío.
 */
class AcumuladorCamposDireccion(
    var calle: AutofillId? = null,
    var ciudad: AutofillId? = null,
    var provincia: AutofillId? = null,
    var codigoPostal: AutofillId? = null,
    var pais: AutofillId? = null,
    var nombreCompleto: AutofillId? = null,
    var telefono: AutofillId? = null,
    var correo: AutofillId? = null
) {
    fun aCamposDetectados(): CamposDetectadosDireccion = CamposDetectadosDireccion(
        calle = calle,
        ciudad = ciudad,
        provincia = provincia,
        codigoPostal = codigoPostal,
        pais = pais,
        nombreCompleto = nombreCompleto,
        telefono = telefono,
        correo = correo
    )
}
