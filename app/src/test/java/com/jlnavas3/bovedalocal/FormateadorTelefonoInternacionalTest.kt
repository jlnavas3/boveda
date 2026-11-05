/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.contactos.FormateadorTelefonoInternacional
import org.junit.Assert.assertEquals
import org.junit.Test

class FormateadorTelefonoInternacionalTest {

    @Test
    fun `limpiarParaMarcado conserva plus y caracteres de marcado telefónico`() {
        assertEquals("+593987654321", FormateadorTelefonoInternacional.limpiarParaMarcado("+593 98-765-4321"))
        assertEquals("+393451234567", FormateadorTelefonoInternacional.limpiarParaMarcado("+39 (345) 123-4567"))
        assertEquals("099123456", FormateadorTelefonoInternacional.limpiarParaMarcado("099 123 456"))
    }

    @Test
    fun `limpiarParaWhatsApp formatea numero para wa me sin mas ni ceros iniciales`() {
        assertEquals("593987654321", FormateadorTelefonoInternacional.limpiarParaWhatsApp("+593 98-765-4321"))
        assertEquals("393451234567", FormateadorTelefonoInternacional.limpiarParaWhatsApp("+39 345 123 4567"))
        assertEquals("393451234567", FormateadorTelefonoInternacional.limpiarParaWhatsApp("0039 345 123 4567"))
    }

    @Test
    fun `formatearEnBloques divide en bloques legibles de 3 o 4 digitos`() {
        val formateado = FormateadorTelefonoInternacional.formatearEnBloques("+593987654321")
        assertEquals("+593 987 654 321", formateado)
    }

    @Test
    fun `esNumeroTelefonicoValido valida telefonos legitimos y descarta textos o contrasenas`() {
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.esNumeroTelefonicoValido("+593 98 765 4321"))
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.esNumeroTelefonicoValido("099123456"))
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.esNumeroTelefonicoValido("+1 (555) 234-5678"))
        org.junit.Assert.assertFalse(FormateadorTelefonoInternacional.esNumeroTelefonicoValido("12345")) // Demasiado corto
        org.junit.Assert.assertFalse(FormateadorTelefonoInternacional.esNumeroTelefonicoValido("clave1234567")) // Contiene letras
        org.junit.Assert.assertFalse(FormateadorTelefonoInternacional.esNumeroTelefonicoValido("https://google.com"))
        org.junit.Assert.assertFalse(FormateadorTelefonoInternacional.esNumeroTelefonicoValido(""))
    }

    @Test
    fun `sonNumerosEquivalentes detecta numeros identicos con diferente formato de guiones y espacios`() {
        // Caso exacto del usuario: +59399-461-5671 vs +593994615671
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.sonNumerosEquivalentes("+59399-461-5671", "+593994615671"))
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.sonNumerosEquivalentes("+593 99 461 5671", "+593994615671"))
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.sonNumerosEquivalentes("+593 (99) 461-5671", "+593 994615671"))
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.sonNumerosEquivalentes("593994615671", "+593994615671"))
    }

    @Test
    fun `sonNumerosEquivalentes detecta equivalencia entre formato local con cero y formato internacional`() {
        // Ecuador: 0994615671 vs +593994615671
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.sonNumerosEquivalentes("0994615671", "+593994615671"))
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.sonNumerosEquivalentes("099-461-5671", "+593994615671"))
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.sonNumerosEquivalentes("994615671", "+593994615671"))

        // USA: (555) 234-5678 vs +1-555-234-5678
        org.junit.Assert.assertTrue(FormateadorTelefonoInternacional.sonNumerosEquivalentes("(555) 234-5678", "+1-555-234-5678"))
    }

    @Test
    fun `sonNumerosEquivalentes no confunde numeros distintos`() {
        org.junit.Assert.assertFalse(FormateadorTelefonoInternacional.sonNumerosEquivalentes("+593994615671", "+593991234567"))
        org.junit.Assert.assertFalse(FormateadorTelefonoInternacional.sonNumerosEquivalentes("0994615671", "0991234567"))
        org.junit.Assert.assertFalse(FormateadorTelefonoInternacional.sonNumerosEquivalentes("+593994615671", null))
        org.junit.Assert.assertFalse(FormateadorTelefonoInternacional.sonNumerosEquivalentes("", "+593994615671"))
    }

    @Test
    fun `mejorRepresentacion prefiere formato con prefijo internacional`() {
        val mejor1 = FormateadorTelefonoInternacional.mejorRepresentacion("0994615671", "+593994615671")
        assertEquals("+593994615671", mejor1)

        val mejor2 = FormateadorTelefonoInternacional.mejorRepresentacion("+59399-461-5671", "0994615671")
        assertEquals("+59399-461-5671", mejor2)
    }
}
