/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Perfil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FusionadorBovedasTest {

    @Test
    fun testFusionBovedasDisjuntas() {
        val entradaA = Entrada(
            id = "550e8400-e29b-41d4-a716-446655440001",
            titulo = "GitHub",
            usuario = "octocat",
            revision = 1L
        )
        val entradaB = Entrada(
            id = "550e8400-e29b-41d4-a716-446655440002",
            titulo = "GitLab",
            usuario = "gitlab-user",
            revision = 1L
        )

        val bovedaA = ContenidoBoveda(entradas = listOf(entradaA))
        val bovedaB = ContenidoBoveda(entradas = listOf(entradaB))

        val resultado = FusionadorBovedas.fusionarBovedas(bovedaA, bovedaB)

        assertEquals(2, resultado.entradas.size)
        assertTrue(resultado.entradas.any { it.id == entradaA.id })
        assertTrue(resultado.entradas.any { it.id == entradaB.id })

        for (e in resultado.entradas) {
            assertNotNull(e.lastSyncHash)
            assertEquals(64, e.lastSyncHash?.length)
        }
    }

    @Test
    fun testFastForwardLocal() {
        val uuid = "550e8400-e29b-41d4-a716-446655440003"
        val baseRemota = Entrada(
            id = uuid,
            titulo = "ProtonMail",
            usuario = "user@pm.me",
            contrasena = "claveAntigua",
            revision = 1L
        )
        val hashBase = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(baseRemota))

        val localModificada = Entrada(
            id = uuid,
            titulo = "ProtonMail",
            usuario = "user@pm.me",
            contrasena = "claveNuevaLocal",
            revision = 2L,
            lastSyncHash = hashBase
        )

        val bovedaLocal = ContenidoBoveda(entradas = listOf(localModificada))
        val bovedaRemota = ContenidoBoveda(entradas = listOf(baseRemota))

        val resultado = FusionadorBovedas.fusionarBovedas(bovedaLocal, bovedaRemota)

        assertEquals(1, resultado.entradas.size)
        val ganadora = resultado.entradas[0]
        assertEquals("claveNuevaLocal", ganadora.contrasena)
        assertEquals(2L, ganadora.revision)
        assertEquals(
            CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(localModificada)),
            ganadora.lastSyncHash
        )
    }

    @Test
    fun testFastForwardRemoto() {
        val uuid = "550e8400-e29b-41d4-a716-446655440004"
        val baseLocal = Entrada(
            id = uuid,
            titulo = "Bitwarden",
            usuario = "user@bitwarden.com",
            contrasena = "claveAntigua",
            revision = 1L
        )
        val hashBase = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(baseLocal))

        val remotaModificada = Entrada(
            id = uuid,
            titulo = "Bitwarden",
            usuario = "user@bitwarden.com",
            contrasena = "claveActualizadaEnDesktop",
            revision = 2L,
            lastSyncHash = hashBase
        )

        val bovedaLocal = ContenidoBoveda(entradas = listOf(baseLocal))
        val bovedaRemota = ContenidoBoveda(entradas = listOf(remotaModificada))

        val resultado = FusionadorBovedas.fusionarBovedas(bovedaLocal, bovedaRemota)

        assertEquals(1, resultado.entradas.size)
        val ganadora = resultado.entradas[0]
        assertEquals("claveActualizadaEnDesktop", ganadora.contrasena)
        assertEquals(2L, ganadora.revision)
        assertEquals(
            CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(remotaModificada)),
            ganadora.lastSyncHash
        )
    }

    @Test
    fun testSplitBrainConcurrentePreservaHistorialYIncrementaRevision() {
        val uuid = "550e8400-e29b-41d4-a716-446655440005"
        val baseComun = Entrada(
            id = uuid,
            titulo = "AWS Console",
            usuario = "admin",
            contrasena = "claveOriginal",
            revision = 1L
        )
        val hashBase = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(baseComun))

        // Edición concurrente en Móvil (Android)
        val local = Entrada(
            id = uuid,
            titulo = "AWS Console",
            usuario = "admin",
            contrasena = "claveAndroid",
            modificadaEn = 2000L,
            revision = 2L,
            lastSyncHash = hashBase
        )

        // Edición concurrente en Escritorio (Desktop)
        val remota = Entrada(
            id = uuid,
            titulo = "AWS Console",
            usuario = "admin",
            contrasena = "claveDesktop",
            modificadaEn = 3000L, // Modificada más recientemente
            revision = 2L,
            lastSyncHash = hashBase
        )

        val resultado = FusionadorBovedas.fusionarBovedas(
            ContenidoBoveda(entradas = listOf(local)),
            ContenidoBoveda(entradas = listOf(remota))
        )

        assertEquals(1, resultado.entradas.size)
        val fusionada = resultado.entradas[0]

        // La más reciente es remota (claveDesktop) y la otra queda en historial sin pérdida
        assertEquals("claveDesktop", fusionada.contrasena)
        assertTrue(fusionada.historialContrasenas.any { it.contrasena == "claveAndroid" })
        assertEquals(3L, fusionada.revision) // max(2, 2) + 1 = 3
        assertEquals(
            CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(fusionada)),
            fusionada.lastSyncHash
        )
    }

    @Test
    fun testPropagacionPapeleraPorFastForward() {
        val uuid = "550e8400-e29b-41d4-a716-446655440006"
        val activaRemota = Entrada(
            id = uuid,
            titulo = "Slack",
            usuario = "user@slack.com",
            revision = 1L
        )
        val hashBase = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(activaRemota))

        // Local lo mandó a la papelera (revision = 2, eliminadaEn > 0)
        val enPapeleraLocal = activaRemota.copy(
            eliminadaEn = 1000L,
            revision = 2L,
            lastSyncHash = hashBase
        )

        val bovedaLocal = ContenidoBoveda(papelera = listOf(enPapeleraLocal))
        val bovedaRemota = ContenidoBoveda(entradas = listOf(activaRemota))

        val resultado = FusionadorBovedas.fusionarBovedas(bovedaLocal, bovedaRemota)

        assertEquals(0, resultado.entradas.size)
        assertEquals(1, resultado.papelera.size)
        assertEquals(uuid, resultado.papelera[0].id)
        assertTrue(resultado.papelera[0].eliminadaEn > 0L)
    }

    @Test
    fun testFusionCategoriasEIdentidades() {
        val cat1 = Categoria(id = "cat-1", nombre = "Finanzas", modificadaEn = 100L)
        val cat2Local = Categoria(id = "cat-2", nombre = "Trabajo Antiguo", modificadaEn = 100L)
        val cat2Remota = Categoria(id = "cat-2", nombre = "Trabajo Actualizado", modificadaEn = 200L)

        val iden1 = Perfil(id = "perf-1", nombre = "Personal", modificadaEn = 150L)

        val bovedaLocal = ContenidoBoveda(
            categorias = listOf(cat1, cat2Local),
            perfiles = listOf(iden1)
        )
        val bovedaRemota = ContenidoBoveda(
            categorias = listOf(cat2Remota)
        )

        val resultado = FusionadorBovedas.fusionarBovedas(bovedaLocal, bovedaRemota)

        assertEquals(2, resultado.categorias.size)
        val cat2 = resultado.categorias.first { it.id == "cat-2" }
        assertEquals("Trabajo Actualizado", cat2.nombre)

        assertEquals(1, resultado.perfiles.size)
        assertEquals("Personal", resultado.perfiles[0].nombre)
    }

    @Test
    fun testPropagacionAliasDispositivoYLimpiezaEnDisco() {
        val bovedaLocal = ContenidoBoveda(
            entradas = emptyList(),
            aliasDispositivo = "HONOR DNP-NX9"
        )
        val bovedaRemota = ContenidoBoveda(
            entradas = emptyList(),
            aliasDispositivo = "Xiaomi 22101320G"
        )

        val resultado = FusionadorBovedas.fusionarBovedasConInforme(bovedaLocal, bovedaRemota)

        // El informe debe capturar el alias del dispositivo remoto
        assertEquals("Xiaomi 22101320G", resultado.informe.dispositivoRemoto)
        assertTrue(resultado.informe.textoResumen().startsWith("con Xiaomi 22101320G: "))

        // La bóveda resultante para guardar en disco debe limpiar el alias
        assertEquals(null, resultado.boveda.aliasDispositivo)
    }
}
