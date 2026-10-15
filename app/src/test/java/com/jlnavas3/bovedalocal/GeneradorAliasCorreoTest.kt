/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.util.GeneradorAliasCorreo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GeneradorAliasCorreoTest {

    @Test
    fun testGenerarConEtiqueta() {
        val base = "usuario@gmail.com"
        val alias = GeneradorAliasCorreo.generarConEtiqueta(base, "Netflix")
        assertEquals("usuario+netflix@gmail.com", alias)
    }

    @Test
    fun testGenerarConEtiquetaSobreAliasPrevio() {
        val base = "usuario+viejo@gmail.com"
        val alias = GeneradorAliasCorreo.generarConEtiqueta(base, "Spotify")
        assertEquals("usuario+spotify@gmail.com", alias)
    }

    @Test
    fun testGenerarParaSitioConCaracteresEspeciales() {
        val base = "admin@empresa.es"
        val alias = GeneradorAliasCorreo.generarParaSitio(base, "¡Amazon Prime (2026)!")
        assertEquals("admin+amazonprime2026@empresa.es", alias)
    }

    @Test
    fun testGenerarConAleatorio() {
        val base = "jln@boveda.com"
        val alias = GeneradorAliasCorreo.generarConAleatorio(base, 6)
        assertTrue(alias.startsWith("jln+"))
        assertTrue(alias.endsWith("@boveda.com"))
        assertEquals(base.length + 7, alias.length) // "jln" + "+" + 6 chars + "@boveda.com"
    }
}
