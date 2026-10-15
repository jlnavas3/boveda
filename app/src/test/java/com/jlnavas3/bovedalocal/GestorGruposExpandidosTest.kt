/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.ui.scroll.GestorGruposExpandidos
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GestorGruposExpandidosTest {

    @Test
    fun `obtenerGrupos retorna conjunto vacio por defecto`() {
        val gestor = GestorGruposExpandidos()
        val grupos = gestor.obtenerGrupos("lista_principal")
        assertTrue(grupos.isEmpty())
    }

    @Test
    fun `guardarGrupos almacena y recupera grupos expandidos por clave`() {
        val gestor = GestorGruposExpandidos()
        val gruposIniciales = setOf("Rerware", "Google", "GitHub")
        gestor.guardarGrupos("lista_principal", gruposIniciales)

        val recuperados = gestor.obtenerGrupos("lista_principal")
        assertEquals(gruposIniciales, recuperados)
    }

    @Test
    fun `guardarGrupos aísla datos entre distintas pantallas`() {
        val gestor = GestorGruposExpandidos()
        gestor.guardarGrupos("lista_principal", setOf("A", "B"))
        gestor.guardarGrupos("papelera", setOf("X", "Y", "Z"))

        assertEquals(setOf("A", "B"), gestor.obtenerGrupos("lista_principal"))
        assertEquals(setOf("X", "Y", "Z"), gestor.obtenerGrupos("papelera"))
    }

    @Test
    fun `alternarGrupo anade y remueve idGrupo correctamente`() {
        val gestor = GestorGruposExpandidos()
        gestor.alternarGrupo("lista_principal", "Rerware")
        assertEquals(setOf("Rerware"), gestor.obtenerGrupos("lista_principal"))

        gestor.alternarGrupo("lista_principal", "Rerware")
        assertTrue(gestor.obtenerGrupos("lista_principal").isEmpty())
    }

    @Test
    fun `limpiarClave elimina grupos de una lista especifica`() {
        val gestor = GestorGruposExpandidos()
        gestor.guardarGrupos("lista_principal", setOf("G1"))
        gestor.guardarGrupos("duplicados", setOf("G2"))

        gestor.limpiarClave("lista_principal")

        assertTrue(gestor.obtenerGrupos("lista_principal").isEmpty())
        assertEquals(setOf("G2"), gestor.obtenerGrupos("duplicados"))
    }

    @Test
    fun `limpiarTodo elimina todos los grupos recordados`() {
        val gestor = GestorGruposExpandidos()
        gestor.guardarGrupos("lista_principal", setOf("Grupo1"))
        gestor.guardarGrupos("duplicados", setOf("Grupo2"))

        gestor.limpiarTodo()

        assertTrue(gestor.obtenerGrupos("lista_principal").isEmpty())
        assertTrue(gestor.obtenerGrupos("duplicados").isEmpty())
    }
}
