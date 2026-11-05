/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.autofill.DetectorCamposDireccionAutofill
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DetectorCamposDireccionAutofillTest {

    @Test
    fun `detecta calle por autocomplete html y texto`() {
        assertTrue(DetectorCamposDireccionAutofill.esCalle(emptyList(), emptyList(), "street-address"))
        assertTrue(DetectorCamposDireccionAutofill.esCalle(emptyList(), listOf("address"), null))
        assertTrue(DetectorCamposDireccionAutofill.esCalle(emptyList(), listOf("adr"), null))
        assertTrue(DetectorCamposDireccionAutofill.esCalle(emptyList(), listOf("direccion"), null))
    }

    @Test
    fun `detecta codigo postal`() {
        assertTrue(DetectorCamposDireccionAutofill.esCodigoPostal(emptyList(), emptyList(), "postal-code"))
        assertTrue(DetectorCamposDireccionAutofill.esCodigoPostal(emptyList(), listOf("zip"), null))
        assertTrue(DetectorCamposDireccionAutofill.esCodigoPostal(emptyList(), listOf("zipcode"), null))
        assertTrue(DetectorCamposDireccionAutofill.esCodigoPostal(emptyList(), listOf("codigopostal"), null))
    }

    @Test
    fun `detecta ciudad, provincia y pais`() {
        assertTrue(DetectorCamposDireccionAutofill.esCiudad(emptyList(), emptyList(), "address-level2"))
        assertTrue(DetectorCamposDireccionAutofill.esCiudad(emptyList(), listOf("city"), null))

        assertTrue(DetectorCamposDireccionAutofill.esProvincia(emptyList(), emptyList(), "address-level1"))
        assertTrue(DetectorCamposDireccionAutofill.esProvincia(emptyList(), listOf("state"), null))

        assertTrue(DetectorCamposDireccionAutofill.esPais(emptyList(), emptyList(), "country"))
        assertTrue(DetectorCamposDireccionAutofill.esPais(emptyList(), listOf("country"), null))
    }

    @Test
    fun `detecta telefono y correo de contacto`() {
        assertTrue(DetectorCamposDireccionAutofill.esTelefono(emptyList(), emptyList(), "tel"))
        assertTrue(DetectorCamposDireccionAutofill.esTelefono(emptyList(), listOf("phone"), null))

        assertTrue(DetectorCamposDireccionAutofill.esCorreo(emptyList(), emptyList(), "email"))
        assertTrue(DetectorCamposDireccionAutofill.esCorreo(emptyList(), listOf("correo"), null))
    }
}
