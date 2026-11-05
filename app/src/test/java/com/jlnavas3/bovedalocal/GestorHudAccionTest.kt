/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.ui.componentes.hud.GestorHudAccion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GestorHudAccionTest {

    @Test
    fun `mostrar con etiqueta valida proyecta los datos en el estado`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val gestor = GestorHudAccion(scope = TestScope(testDispatcher))

        gestor.mostrar("Compartir")
        testDispatcher.scheduler.runCurrent()

        val estado = gestor.estado.value
        assertNotNull(estado)
        assertEquals("Compartir", estado?.etiqueta)
        assertNull(estado?.icono)
    }

    @Test
    fun `mostrar con etiqueta en blanco no altera el estado`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val gestor = GestorHudAccion(scope = TestScope(testDispatcher))

        gestor.mostrar("   ")
        testDispatcher.scheduler.runCurrent()

        assertNull(gestor.estado.value)
    }

    @Test
    fun `ocultar limpia el estado inmediatamente`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val gestor = GestorHudAccion(scope = TestScope(testDispatcher))

        gestor.mostrar("Eliminar")
        testDispatcher.scheduler.runCurrent()
        assertNotNull(gestor.estado.value)

        gestor.ocultar()
        assertNull(gestor.estado.value)
    }

    @Test
    fun `el estado se auto-descarta tras cumplir la duracion de 2,5 segundos`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val gestor = GestorHudAccion(scope = TestScope(testDispatcher))

        gestor.mostrar("Renombrar", duracionMs = 2500L)
        testDispatcher.scheduler.runCurrent()
        assertNotNull(gestor.estado.value)

        // Avanzamos 2400 ms: sigue visible
        advanceTimeBy(2400L)
        testDispatcher.scheduler.runCurrent()
        assertNotNull(gestor.estado.value)

        // Avanzamos 150 ms adicionales: ya pasaron 2550 ms, debe haberse cerrado
        advanceTimeBy(150L)
        testDispatcher.scheduler.runCurrent()
        assertNull(gestor.estado.value)
    }

    @Test
    fun `segunda pulsacion reemplaza la anterior y reinicia el temporizador`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val gestor = GestorHudAccion(scope = TestScope(testDispatcher))

        gestor.mostrar("Accion A", duracionMs = 2500L)
        testDispatcher.scheduler.runCurrent()
        assertEquals("Accion A", gestor.estado.value?.etiqueta)

        // Avanzamos 2000 ms
        advanceTimeBy(2000L)
        testDispatcher.scheduler.runCurrent()

        // Disparamos nueva acción
        gestor.mostrar("Accion B", duracionMs = 2500L)
        testDispatcher.scheduler.runCurrent()
        assertEquals("Accion B", gestor.estado.value?.etiqueta)

        // Avanzamos 1000 ms (en la anterior habrían pasado 3000 ms y se habría cerrado)
        advanceTimeBy(1000L)
        testDispatcher.scheduler.runCurrent()
        assertEquals("Accion B", gestor.estado.value?.etiqueta)

        // Avanzamos 1600 ms más (2600 ms desde Accion B)
        advanceTimeBy(1600L)
        testDispatcher.scheduler.runCurrent()
        assertNull(gestor.estado.value)
    }
}
