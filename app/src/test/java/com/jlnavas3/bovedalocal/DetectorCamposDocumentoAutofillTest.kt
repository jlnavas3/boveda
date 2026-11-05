/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.autofill.DetectorCamposDocumentoAutofill
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DetectorCamposDocumentoAutofillTest {

    @Test
    fun `detecta documento de identidad por cedula, dni y pasaporte`() {
        assertTrue(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(listOf("cedula"), null))
        assertTrue(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(listOf("cédula"), null))
        assertTrue(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(listOf("dni"), null))
        assertTrue(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(listOf("numero_documento"), null))
        assertTrue(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(listOf("passport"), null))
        assertTrue(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(emptyList(), "national-id"))
        assertFalse(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(listOf("password", "codigo"), null))
    }

    @Test
    fun `detecta documento con pistas personalizadas`() {
        val pistasCustom = listOf("mi_documento_custom", "id_empleado")
        assertTrue(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(listOf("mi_documento_custom"), null, pistasCustom))
        assertTrue(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(listOf("id_empleado_interno"), null, pistasCustom))
        assertFalse(DetectorCamposDocumentoAutofill.esDocumentoIdentidad(listOf("dni"), null, pistasCustom))
    }
}

