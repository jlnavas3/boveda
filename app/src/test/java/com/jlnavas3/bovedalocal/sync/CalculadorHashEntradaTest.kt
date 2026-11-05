/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CalculadorHashEntradaTest {

    @Test
    fun testVectorCanónicoCruzadoConRust() {
        val entrada = Entrada(
            id = "550e8400-e29b-41d4-a716-446655440000",
            titulo = "GitHub",
            usuario = "octocat",
            contrasena = "CorrectHorseBatteryStaple",
            notas = "Cuenta corporativa",
            secretoTotp = "JBSWY3DPEHPK3PXP",
            urls = listOf("https://github.com", "https://gist.github.com"),
            revision = 1L,
            lastSyncHash = null
        )

        val hash = CalculadorHashEntrada.calcularContentHash(entrada)
        val hashHex = CalculadorHashEntrada.bytesAHex(hash)

        // Vector idéntico al calculado por Rust (boveda-local-desktop)
        assertEquals("55e07da013a6666ff8571008ebd5c62abd5f80f516197021187d3e2decd0bed9", hashHex)
    }

    @Test
    fun testHasherDeterministaExcluyeMetadatosSincronizacion() {
        val original = Entrada(
            id = "550e8400-e29b-41d4-a716-446655440000",
            titulo = "GitHub",
            usuario = "octocat",
            contrasena = "CorrectHorseBatteryStaple",
            revision = 1L,
            lastSyncHash = null
        )
        val hashOriginal = CalculadorHashEntrada.calcularContentHash(original)

        // Cambiar revision o lastSyncHash NO altera el hash de contenido
        val conMetadatosModificados = original.copy(
            revision = 42L,
            lastSyncHash = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef"
        )
        val hashModificado = CalculadorHashEntrada.calcularContentHash(conMetadatosModificados)
        assertEquals(CalculadorHashEntrada.bytesAHex(hashOriginal), CalculadorHashEntrada.bytesAHex(hashModificado))

        // Cambiar un campo nuclear sí altera el hash
        val conClaveModificada = original.copy(contrasena = "OtraClave!")
        val hashClaveModificada = CalculadorHashEntrada.calcularContentHash(conClaveModificada)
        assertNotEquals(CalculadorHashEntrada.bytesAHex(hashOriginal), CalculadorHashEntrada.bytesAHex(hashClaveModificada))
    }

    @Test
    fun testConversionHexadecimalBidireccional() {
        val hexOriginal = "55e07da013a6666ff8571008ebd5c62abd5f80f516197021187d3e2decd0bed9"
        val bytes = CalculadorHashEntrada.hexABytes(hexOriginal)
        assertEquals(32, bytes.size)
        val hexReconstruido = CalculadorHashEntrada.bytesAHex(bytes)
        assertEquals(hexOriginal, hexReconstruido)
    }
}
