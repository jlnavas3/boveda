/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RevisionEntradaTest {

    @Test
    fun testNuevaEntradaTieneRevision1PorDefecto() {
        val entrada = Entrada(
            id = "test-uuid-1",
            titulo = "GitHub",
            usuario = "octocat"
        )
        assertEquals(1L, entrada.revision)
    }

    @Test
    fun testDeteccionCambioContenidoParaIncrementoRevision() {
        val original = Entrada(
            id = "test-uuid-2",
            titulo = "Correo",
            usuario = "user@test.com",
            contrasena = "clave1",
            revision = 1L
        )

        // Mismo contenido: el hash es idéntico
        val copiaMismoContenido = original.copy(ultimoUsoEn = 123456789L)
        val hashOrig = CalculadorHashEntrada.calcularContentHash(original)
        val hashMismo = CalculadorHashEntrada.calcularContentHash(copiaMismoContenido)
        assertTrue(hashOrig.contentEquals(hashMismo))

        // Contenido modificado: el hash cambia
        val modificado = original.copy(contrasena = "clave2")
        val hashModificado = CalculadorHashEntrada.calcularContentHash(modificado)
        assertFalse(hashOrig.contentEquals(hashModificado))
    }

    @Test
    fun testEliminadaEnAlteraHashContenido() {
        val activa = Entrada(
            id = "test-uuid-3",
            titulo = "Servicio",
            usuario = "admin",
            eliminadaEn = 0L,
            revision = 1L
        )
        val enPapelera = activa.copy(eliminadaEn = 1700000000L)

        val hashActiva = CalculadorHashEntrada.calcularContentHash(activa)
        val hashPapelera = CalculadorHashEntrada.calcularContentHash(enPapelera)

        assertFalse(hashActiva.contentEquals(hashPapelera))
    }
}
