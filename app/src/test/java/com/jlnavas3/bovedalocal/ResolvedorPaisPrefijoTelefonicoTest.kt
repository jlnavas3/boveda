/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.contactos.ResolvedorPaisPrefijoTelefonico
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ResolvedorPaisPrefijoTelefonicoTest {

    @Test
    fun `obtenerPaisPorNumero resuelve codigos de 1, 2 y 3 digitos correctamente`() {
        // 1 dígito
        assertEquals("US", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+14155552671"))
        assertEquals("RU", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+79991234567"))

        // 2 dígitos
        assertEquals("ES", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+34612345678"))
        assertEquals("IT", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+393451234567"))
        assertEquals("FR", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+33612345678"))
        assertEquals("GB", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+447911123456"))
        assertEquals("MX", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+525512345678"))
        assertEquals("BR", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+5511987654321"))
        assertEquals("AR", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+5491123456789"))
        assertEquals("CO", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+573001234567"))

        // 3 dígitos
        assertEquals("EC", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+593987654321"))
        assertEquals("PE", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+511234567"))
        assertEquals("CL", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+56912345678"))
        assertEquals("VE", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+584121234567"))
        assertEquals("CR", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("+50688887777"))

        // Con prefijo 00 internacional
        assertEquals("EC", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("00593987654321"))
        assertEquals("IT", ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("00393451234567"))

        // Inválidos o locales
        assertNull(ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero("099123456"))
        assertNull(ResolvedorPaisPrefijoTelefonico.obtenerPaisPorNumero(""))
    }
}
