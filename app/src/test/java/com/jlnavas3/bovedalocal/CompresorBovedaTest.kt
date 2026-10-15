/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.crypto.CompresorBoveda
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CompresorBovedaTest {

    @Test
    fun testComprimirYDescomprimirIdentico() {
        val original = """{"version":1,"entradas":[{"id":"123","titulo":"Test Banco","usuario":"admin","contrasena":"Secr3t!@#"}]}""".toByteArray(Charsets.UTF_8)
        val comprimido = CompresorBoveda.comprimir(original)
        assertTrue(CompresorBoveda.esComprimido(comprimido))
        
        val descomprimido = CompresorBoveda.descomprimir(comprimido)
        assertArrayEquals(original, descomprimido)
        assertEquals(String(original, Charsets.UTF_8), String(descomprimido, Charsets.UTF_8))
    }

    @Test
    fun testRetrocompatibilidadBovedaSinComprimir() {
        val planoSinComprimir = """{"version":1,"entradas":[]}""".toByteArray(Charsets.UTF_8)
        assertFalse(CompresorBoveda.esComprimido(planoSinComprimir))

        val resultado = CompresorBoveda.descomprimir(planoSinComprimir)
        assertArrayEquals(planoSinComprimir, resultado)
    }

    @Test
    fun testReduccionTamanoEnPayloadsRepetitivos() {
        val jsonLargo = buildString {
            append("{\"entradas\":[")
            repeat(100) { i ->
                if (i > 0) append(",")
                append("{\"id\":\"id-$i\",\"titulo\":\"Servicio Web $i\",\"usuario\":\"usuario.seguro@dominio.com\",\"contrasena\":\"P@ssw0rdMuyLargaYRepetitiva$i\"}")
            }
            append("]}")
        }.toByteArray(Charsets.UTF_8)

        val comprimido = CompresorBoveda.comprimir(jsonLargo)
        assertTrue(comprimido.size < jsonLargo.size / 2) // Al menos 50% de reducción
    }
}
