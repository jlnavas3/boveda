/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda.ManejadorInsercionFiltro
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ManejadorInsercionFiltroTest {

    @Test
    fun testInsertarEnCadenaVacia() {
        val resultado = ManejadorInsercionFiltro.alternarFiltro("", "t:login")
        assertEquals("t:login ", resultado)
    }

    @Test
    fun testInsertarEnCadenaExistente() {
        val resultado = ManejadorInsercionFiltro.alternarFiltro("google", "has:totp")
        assertEquals("google has:totp ", resultado)
    }

    @Test
    fun testAlternarRemoverComandoExistente() {
        val inicial = "google t:login has:totp"
        val resultado = ManejadorInsercionFiltro.alternarFiltro(inicial, "t:login")
        assertEquals("google has:totp", resultado)
    }

    @Test
    fun testAlternarPrefijoConParametro() {
        val resultado = ManejadorInsercionFiltro.alternarFiltro("google", "tag:")
        assertEquals("google tag:", resultado)

        val resultadoRemover = ManejadorInsercionFiltro.alternarFiltro("google tag:trabajo", "tag:")
        assertEquals("google", resultadoRemover)
    }

    @Test
    fun testEstaActivo() {
        assertTrue(ManejadorInsercionFiltro.estaActivo("google t:login", "t:login"))
        assertFalse(ManejadorInsercionFiltro.estaActivo("google t:login", "t:tarjeta"))
        assertTrue(ManejadorInsercionFiltro.estaActivo("google tag:finanzas", "tag:"))
        assertFalse(ManejadorInsercionFiltro.estaActivo("google", "tag:"))
    }
}
