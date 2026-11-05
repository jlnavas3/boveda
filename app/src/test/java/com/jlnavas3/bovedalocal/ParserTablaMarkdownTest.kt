/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.util.AlineacionColumnaTabla
import com.jlnavas3.bovedalocal.util.ParserTablaMarkdown
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ParserTablaMarkdownTest {

    @Test
    fun testParsearTablaUsuarioConAlineacionIzquierdaYDerecha() {
        val lineas = listOf(
            "| Col Izquierda | Col Derecha |",
            "| :--- | ---: |",
            "| Texto normal | Texto a la derecha |"
        )

        val tabla = ParserTablaMarkdown.parsear(lineas)
        assertNotNull(tabla)
        assertEquals(2, tabla!!.columnas.size)

        assertEquals("Col Izquierda", tabla.columnas[0].encabezado)
        assertEquals(AlineacionColumnaTabla.IZQUIERDA, tabla.columnas[0].alineacion)

        assertEquals("Col Derecha", tabla.columnas[1].encabezado)
        assertEquals(AlineacionColumnaTabla.DERECHA, tabla.columnas[1].alineacion)

        assertEquals(1, tabla.filas.size)
        assertEquals(listOf("Texto normal", "Texto a la derecha"), tabla.filas[0])
    }

    @Test
    fun testParsearTablaTresColumnasConCentro() {
        val lineas = listOf(
            "| Nombre | Estado | Valor |",
            "| :--- | :---: | ---: |",
            "| Servidor A | Activo | 100 |",
            "| Servidor B | Inactivo | 0 |"
        )

        val tabla = ParserTablaMarkdown.parsear(lineas)
        assertNotNull(tabla)
        assertEquals(3, tabla!!.columnas.size)

        assertEquals(AlineacionColumnaTabla.IZQUIERDA, tabla.columnas[0].alineacion)
        assertEquals(AlineacionColumnaTabla.CENTRO, tabla.columnas[1].alineacion)
        assertEquals(AlineacionColumnaTabla.DERECHA, tabla.columnas[2].alineacion)

        assertEquals(2, tabla.filas.size)
        assertEquals(listOf("Servidor A", "Activo", "100"), tabla.filas[0])
        assertEquals(listOf("Servidor B", "Inactivo", "0"), tabla.filas[1])
    }

    @Test
    fun testNoTablaRetornaNull() {
        val lineasSinDelimitador = listOf(
            "| Col 1 | Col 2 |",
            "| Val 1 | Val 2 |"
        )
        assertNull(ParserTablaMarkdown.parsear(lineasSinDelimitador))

        val unaSolaLinea = listOf("| Col 1 | Col 2 |")
        assertNull(ParserTablaMarkdown.parsear(unaSolaLinea))

        val textoPlano = listOf("Hola mundo", "Segunda linea")
        assertNull(ParserTablaMarkdown.parsear(textoPlano))
    }
}
