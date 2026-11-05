/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.autofill.DetectorCamposTarjetaAutofill
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DetectorCamposTarjetaAutofillTest {

    @Test
    fun `detecta numero de tarjeta por autocomplete html cc-number`() {
        assertTrue(DetectorCamposTarjetaAutofill.esNumeroTarjeta(emptyList(), emptyList(), "cc-number"))
    }

    @Test
    fun `detecta numero de tarjeta por pista de sistema o texto en atributos`() {
        assertTrue(DetectorCamposTarjetaAutofill.esNumeroTarjeta(listOf("creditcardnumber"), emptyList(), null))
        assertTrue(DetectorCamposTarjetaAutofill.esNumeroTarjeta(emptyList(), listOf("input_cardnumber"), null))
        assertTrue(DetectorCamposTarjetaAutofill.esNumeroTarjeta(emptyList(), listOf("ccnum"), null))
        assertFalse(DetectorCamposTarjetaAutofill.esNumeroTarjeta(emptyList(), listOf("username", "login"), null))
    }

    @Test
    fun `detecta cvv por autocomplete y palabras clave`() {
        assertTrue(DetectorCamposTarjetaAutofill.esCvv(emptyList(), emptyList(), "cc-csc"))
        assertTrue(DetectorCamposTarjetaAutofill.esCvv(listOf("creditcardsecuritycode"), emptyList(), null))
        assertTrue(DetectorCamposTarjetaAutofill.esCvv(emptyList(), listOf("cvv"), null))
        assertTrue(DetectorCamposTarjetaAutofill.esCvv(emptyList(), listOf("securitycode"), null))
        assertFalse(DetectorCamposTarjetaAutofill.esCvv(emptyList(), listOf("password"), null))
    }

    @Test
    fun `detecta mes y ano de expiracion separados`() {
        assertTrue(DetectorCamposTarjetaAutofill.esExpiracionMes(emptyList(), emptyList(), "cc-exp-month"))
        assertTrue(DetectorCamposTarjetaAutofill.esExpiracionMes(emptyList(), listOf("expmonth"), null))

        assertTrue(DetectorCamposTarjetaAutofill.esExpiracionAno(emptyList(), emptyList(), "cc-exp-year"))
        assertTrue(DetectorCamposTarjetaAutofill.esExpiracionAno(emptyList(), listOf("expyear"), null))
    }

    @Test
    fun `detecta expiracion combinada`() {
        assertTrue(DetectorCamposTarjetaAutofill.esExpiracion(emptyList(), emptyList(), "cc-exp"))
        assertTrue(DetectorCamposTarjetaAutofill.esExpiracion(emptyList(), listOf("expdate"), null))
        assertTrue(DetectorCamposTarjetaAutofill.esExpiracion(emptyList(), listOf("vencimiento"), null))
    }

    @Test
    fun `detecta titular de tarjeta`() {
        assertTrue(DetectorCamposTarjetaAutofill.esTitular(emptyList(), "cc-name"))
        assertTrue(DetectorCamposTarjetaAutofill.esTitular(listOf("cardname"), null))
        assertTrue(DetectorCamposTarjetaAutofill.esTitular(listOf("cname"), null))
        assertTrue(DetectorCamposTarjetaAutofill.esTitular(listOf("cardholder"), null))
    }
}
