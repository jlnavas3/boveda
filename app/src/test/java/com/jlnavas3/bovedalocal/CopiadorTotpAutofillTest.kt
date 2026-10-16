/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.autofill.AutofillOtpUtiles
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CopiadorTotpAutofillTest {

    @Test
    fun `ajuste de autofillCopiarTotpAutomatico por defecto es true`() {
        val ajustes = AjustesApp()
        assertTrue(ajustes.autofillCopiarTotpAutomatico)
    }

    @Test
    fun `obtenerCodigoTotp retorna null si entrada no tiene secreto`() {
        val entrada = Entrada(
            id = "test-1",
            tipo = TipoEntrada.LOGIN,
            titulo = "Sin 2FA",
            usuario = "usuario",
            contrasena = "clave"
        )
        assertNull(AutofillOtpUtiles.obtenerCodigoTotp(entrada))
    }

    @Test
    fun `obtenerCodigoTotp genera codigo valido para entrada con secreto Base32`() {
        val entrada = Entrada(
            id = "test-2",
            tipo = TipoEntrada.LOGIN,
            titulo = "Con 2FA",
            usuario = "usuario",
            contrasena = "clave",
            secretoTotp = "JBSWY3DPEHPK3PXP" // RFC 4226 test secret
        )
        val codigo = AutofillOtpUtiles.obtenerCodigoTotp(entrada, momentoMs = 59000L)
        assertNotNull(codigo)
        assertEquals(6, codigo?.length)
        assertTrue(codigo?.all { it.isDigit() } == true)
    }

    @Test
    fun `si el ajuste esta desactivado no debe procesarse copia de totp`() {
        val ajustes = AjustesApp(autofillCopiarTotpAutomatico = false)
        assertFalse(ajustes.autofillCopiarTotpAutomatico)
    }
}
