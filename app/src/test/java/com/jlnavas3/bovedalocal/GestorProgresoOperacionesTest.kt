/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.ItemProgresoEntrada
import com.jlnavas3.bovedalocal.ui.progreso.GestorProgresoOperaciones
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GestorProgresoOperacionesTest {

    @Before
    fun setUp() {
        GestorProgresoOperaciones.descartar()
    }

    @Test
    fun `iniciar configura el estado inicial correctamente`() {
        GestorProgresoOperaciones.iniciar(
            id = "test_op",
            titulo = "Importando archivo.csv",
            faseInicial = "Leyendo...",
            totalEstimado = 100
        )

        val estado = GestorProgresoOperaciones.estado.value
        assertNotNull(estado)
        assertEquals("test_op", estado?.idOperacion)
        assertEquals("Importando archivo.csv", estado?.titulo)
        assertEquals("Leyendo...", estado?.fase)
        assertTrue(estado?.enEjecucion == true)
        assertFalse(estado?.completado == true)
        assertFalse(GestorProgresoOperaciones.minimizado.value)
    }

    @Test
    fun `reportarItem preserva todos los items procesados en el historial`() {
        GestorProgresoOperaciones.iniciar("test", "Test")

        for (i in 1..6) {
            GestorProgresoOperaciones.reportarItem(
                item = ItemProgresoEntrada(
                    id = "item_$i",
                    titulo = "Cuenta $i",
                    progreso = 1f,
                    completado = true
                ),
                progresoGlobal = i / 6f,
                procesadas = i
            )
        }

        val estado = GestorProgresoOperaciones.estado.value
        assertNotNull(estado)
        assertEquals(6, estado?.itemsActivos?.size)
        assertEquals("Cuenta 1", estado?.itemsActivos?.get(0)?.titulo)
        assertEquals("Cuenta 6", estado?.itemsActivos?.get(5)?.titulo)
    }

    @Test
    fun `completar finaliza todos los items al 100 por ciento`() {
        GestorProgresoOperaciones.iniciar("test", "Test")
        GestorProgresoOperaciones.reportarItem(
            item = ItemProgresoEntrada("1", "Cuenta 1", progreso = 0.5f, completado = false),
            progresoGlobal = 0.5f
        )
        GestorProgresoOperaciones.completar("Exito", 1)
        val estado = GestorProgresoOperaciones.estado.value
        assertTrue(estado?.itemsActivos?.all { it.completado && it.progreso == 1f } == true)
    }

    @Test
    fun `completar actualiza el estado a terminado y no en ejecucion`() {
        GestorProgresoOperaciones.iniciar("test", "Test")
        var accionLlamada = false

        GestorProgresoOperaciones.completar(
            mensajeExito = "Importadas 50 cuentas",
            totalCuentas = 50,
            textoAccionResultado = "Estandarizar",
            accionResultado = { accionLlamada = true }
        )

        val estado = GestorProgresoOperaciones.estado.value
        assertNotNull(estado)
        assertTrue(estado?.completado == true)
        assertFalse(estado?.enEjecucion == true)
        assertEquals(1f, estado?.progresoGlobal)
        assertEquals("Estandarizar", estado?.textoAccionResultado)

        estado?.accionResultado?.invoke()
        assertTrue(accionLlamada)
    }

    @Test
    fun `descartar limpia el estado completamente`() {
        GestorProgresoOperaciones.iniciar("test", "Test")
        GestorProgresoOperaciones.descartar()
        assertNull(GestorProgresoOperaciones.estado.value)
        assertFalse(GestorProgresoOperaciones.minimizado.value)
    }
}
