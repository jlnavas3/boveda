/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.CatalogoArticulosAyuda
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogoArticulosAyudaTest {

    @Test
    fun testArticulosTienenIdsUnicos() {
        val ids = CatalogoArticulosAyuda.TODOS.map { it.id }
        assertEquals("Todos los artículos deben tener IDs únicos", ids.distinct().size, ids.size)
        assertTrue("Debe haber al menos 20 artículos documentados", ids.size >= 20)
    }

    @Test
    fun testBuscarPorPalabrasClave() {
        val resultadosTotp = CatalogoArticulosAyuda.buscar("totp")
        assertTrue("La búsqueda de totp debe arrojar resultados", resultadosTotp.isNotEmpty())
        assertTrue(resultadosTotp.any { it.id == "autenticador" || it.id == "escaner" })

        val resultadosCopia = CatalogoArticulosAyuda.buscar("cxf")
        assertTrue("La búsqueda de cxf debe encontrar copias", resultadosCopia.any { it.id == "copias" })

        val resultadosVacio = CatalogoArticulosAyuda.buscar("zzzzzzinvalido")
        assertTrue("Búsqueda inválida debe devolver lista vacía", resultadosVacio.isEmpty())
    }

    @Test
    fun testAgruparPorCategoria() {
        val grupos = CatalogoArticulosAyuda.agruparPorCategoria()
        assertTrue(grupos.containsKey("Búsqueda y organización"))
        assertTrue(grupos.containsKey("Herramientas integradas"))
        assertTrue(grupos.containsKey("Salud, auditoría y privacidad"))
        assertTrue(grupos.containsKey("Sistema y ajustes"))
    }
}
