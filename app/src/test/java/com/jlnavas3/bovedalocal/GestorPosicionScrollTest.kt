/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.ui.scroll.GestorPosicionScroll
import org.junit.Assert.assertEquals
import org.junit.Test

class GestorPosicionScrollTest {

    @Test
    fun `obtenerPosicion retorna cero por defecto cuando no existe registro`() {
        val gestor = GestorPosicionScroll()
        val (indice, offset) = gestor.obtenerPosicion("lista_principal")
        assertEquals(0, indice)
        assertEquals(0, offset)
    }

    @Test
    fun `guardarPosicion almacena y recupera posicion exacta por clave`() {
        val gestor = GestorPosicionScroll()
        gestor.guardarPosicion("lista_principal", 14, 85)
        gestor.guardarPosicion("papelera", 3, 20)

        val (indiceLista, offsetLista) = gestor.obtenerPosicion("lista_principal")
        assertEquals(14, indiceLista)
        assertEquals(85, offsetLista)

        val (indicePapelera, offsetPapelera) = gestor.obtenerPosicion("papelera")
        assertEquals(3, indicePapelera)
        assertEquals(20, offsetPapelera)
    }

    @Test
    fun `guardarPosicion sobrescribe valores previos para la misma clave`() {
        val gestor = GestorPosicionScroll()
        gestor.guardarPosicion("lista_principal", 5, 10)
        gestor.guardarPosicion("lista_principal", 22, 140)

        val (indice, offset) = gestor.obtenerPosicion("lista_principal")
        assertEquals(22, indice)
        assertEquals(140, offset)
    }

    @Test
    fun `limpiarClave elimina posicion especifica`() {
        val gestor = GestorPosicionScroll()
        gestor.guardarPosicion("lista_principal", 10, 50)
        gestor.guardarPosicion("papelera", 5, 20)

        gestor.limpiarClave("lista_principal")

        val (indiceLista, offsetLista) = gestor.obtenerPosicion("lista_principal")
        assertEquals(0, indiceLista)
        assertEquals(0, offsetLista)

        val (indicePapelera, offsetPapelera) = gestor.obtenerPosicion("papelera")
        assertEquals(5, indicePapelera)
        assertEquals(20, offsetPapelera)
    }

    @Test
    fun `limpiarTodo resetea todas las posiciones almacenadas`() {
        val gestor = GestorPosicionScroll()
        gestor.guardarPosicion("lista_principal", 10, 50)
        gestor.guardarPosicion("tipo_CONTRASENA", 4, 12)

        gestor.limpiarTodo()

        val (indice1, offset1) = gestor.obtenerPosicion("lista_principal")
        assertEquals(0, indice1)
        assertEquals(0, offset1)

        val (indice2, offset2) = gestor.obtenerPosicion("tipo_CONTRASENA")
        assertEquals(0, indice2)
        assertEquals(0, offset2)
    }
}
