/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.CambioContrasena
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculadorMascaraDiffTest {

    @Test
    fun testEntradaNuevaEnciendeCamposPresentes() {
        val entrada = Entrada(
            id = "id-1",
            titulo = "GitHub",
            usuario = "octocat",
            contrasena = "secret123",
            urls = listOf("https://github.com", "androidapp://com.github.android"),
            notas = "Token personal guardado",
            secretoTotp = "JBSWY3DPEHPK3PXP"
        )

        val mascara = CalculadorMascaraDiff.deEntradaNueva(entrada)

        assertTrue(mascara.usuario)
        assertTrue(mascara.clave)
        assertTrue(mascara.totp)
        assertTrue(mascara.web)
        assertTrue(mascara.app)
        assertTrue(mascara.notas)
        assertFalse(mascara.passkey)
        assertFalse(mascara.submodelos)
        assertFalse(mascara.camposExtra)
        assertTrue(mascara.hayCambios)
    }

    @Test
    fun testClaveSeIluminaConCambioEnHistorialAunqueContrasenaSeaIgual() {
        val previa = Entrada(
            id = "id-2",
            titulo = "Servidor",
            contrasena = "claveGanadora",
            historialContrasenas = emptyList()
        )
        val nueva = Entrada(
            id = "id-2",
            titulo = "Servidor",
            contrasena = "claveGanadora",
            historialContrasenas = listOf(
                CambioContrasena(contrasena = "claveAntiguaRemota", cambiadaEn = 1000L)
            )
        )

        val mascara = CalculadorMascaraDiff.calcularDiff(previa, nueva)

        assertTrue("La clave debe iluminarse si el historial absorbió una credencial", mascara.clave)
        assertFalse(mascara.usuario)
        assertFalse(mascara.totp)
        assertTrue(mascara.hayCambios)
    }

    @Test
    fun testDiffUrlsDistingueWebDeApp() {
        val previa = Entrada(
            id = "id-3",
            titulo = "Amazon",
            urls = listOf("https://amazon.com")
        )
        val nuevaConApp = Entrada(
            id = "id-3",
            titulo = "Amazon",
            urls = listOf("https://amazon.com", "androidapp://com.amazon.mShop.android.shopping")
        )

        val mascara = CalculadorMascaraDiff.calcularDiff(previa, nuevaConApp)

        assertFalse(mascara.web)
        assertTrue(mascara.app)
        assertFalse(mascara.clave)
    }

    @Test
    fun testDiffSubmodelosNativos() {
        val previa = Entrada(
            id = "id-4",
            titulo = "Tarjeta Visa",
            datosTarjeta = DatosTarjetaBancaria(titular = "Juan Perez", numero = "4111111111111111")
        )
        val nueva = Entrada(
            id = "id-4",
            titulo = "Tarjeta Visa",
            datosTarjeta = DatosTarjetaBancaria(titular = "Juan Perez", numero = "4111111111112222")
        )

        val mascara = CalculadorMascaraDiff.calcularDiff(previa, nueva)

        assertTrue(mascara.submodelos)
        assertFalse(mascara.clave)
        assertFalse(mascara.usuario)
    }

    @Test
    fun testTransicionPapeleraConModificacionDeCamposSimultanea() {
        val enPapeleraLocal = Entrada(
            id = "id-5",
            titulo = "Netflix",
            contrasena = "claveVieja",
            eliminadaEn = 1700000000L
        )
        val restauradaRemota = Entrada(
            id = "id-5",
            titulo = "Netflix",
            contrasena = "claveNueva",
            eliminadaEn = 0L
        )

        val bovedaLocal = ContenidoBoveda(papelera = listOf(enPapeleraLocal))
        val bovedaRemota = ContenidoBoveda(entradas = listOf(restauradaRemota))

        val resultado = FusionadorBovedas.fusionarBovedasConInforme(bovedaLocal, bovedaRemota)
        val informe = resultado.informe

        assertEquals(0, informe.nuevas.size)
        assertEquals(0, informe.actualizadas.size)
        assertEquals(1, informe.papelera.size)

        val item = informe.papelera[0]
        assertEquals(TipoCambioSync.RESTAURADA_DE_PAPELERA, item.tipoCambio)
        assertTrue("La máscara debe iluminar el campo que cambió al restaurarse", item.mascara.clave)
    }

    @Test
    fun testSplitBrainConContenidoIdenticoNoGeneraEntradaConIconosApagados() {
        val entradaBase = Entrada(
            id = "id-6",
            titulo = "Spotify",
            usuario = "user@spotify.com",
            contrasena = "mismaclave",
            revision = 1L,
            modificadaEn = 2000L
        )
        val hashBase = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(entradaBase))

        val local = entradaBase.copy(revision = 2L, lastSyncHash = hashBase, modificadaEn = 3000L)
        val remota = entradaBase.copy(revision = 2L, lastSyncHash = hashBase, modificadaEn = 2500L)

        val bovedaLocal = ContenidoBoveda(entradas = listOf(local))
        val bovedaRemota = ContenidoBoveda(entradas = listOf(remota))

        val resultado = FusionadorBovedas.fusionarBovedasConInforme(bovedaLocal, bovedaRemota)
        val informe = resultado.informe

        assertEquals(0, informe.nuevas.size)
        assertEquals(0, informe.actualizadas.size)
        assertEquals(1, informe.sinCambiosCount)
        assertFalse(informe.hayCambios)
    }
}
