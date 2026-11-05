/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class P2pSyncIntegrationTest {

    @Test
    fun testSincronizacionP2PEndToEndExitosa() {
        val skDesktop = ByteArray(32) { 0x11 }
        val pkDesktop = X25519.derivarClavePublica(skDesktop)
        val pskToken = ByteArray(32) { 0x33 }

        val bovedaDesktop = ContenidoBoveda(
            entradas = listOf(
                Entrada(
                    id = "550e8400-e29b-41d4-a716-446655440001",
                    titulo = "Desktop Entry",
                    usuario = "admin",
                    revision = 1L
                )
            )
        )

        val bovedaAndroid = ContenidoBoveda(
            entradas = listOf(
                Entrada(
                    id = "550e8400-e29b-41d4-a716-446655440002",
                    titulo = "Android Entry",
                    usuario = "mobile",
                    revision = 1L
                )
            )
        )

        val servidor = ServidorSyncP2P.bind(skDesktop, pskToken, puerto = 0)
        val puertoAsignado = servidor.puertoLocal
        val commitEjecutado = AtomicBoolean(false)

        val hiloServidor = Thread {
            servidor.atenderSincronizacion(bovedaDesktop) { bovedaFusionada ->
                commitEjecutado.set(true)
                assertEquals(2, bovedaFusionada.entradas.size)
            }
        }
        hiloServidor.start()

        val payloadQr = SyncQrPayload(
            ips = listOf("127.0.0.1"),
            puerto = puertoAsignado,
            clavePublicaDesktop = pkDesktop,
            tokenPsk = pskToken,
            version = 2
        )

        val bovedaResultadoCliente = ClienteSyncP2P.sincronizarConServidor(payloadQr, bovedaAndroid)
        hiloServidor.join(5000)

        assertTrue(commitEjecutado.get())
        assertEquals(2, bovedaResultadoCliente.entradas.size)
        assertTrue(bovedaResultadoCliente.entradas.any { it.titulo == "Desktop Entry" })
        assertTrue(bovedaResultadoCliente.entradas.any { it.titulo == "Android Entry" })
    }

    @Test(expected = Exception::class)
    fun testRechazoHandshakeConPskInvalida() {
        val skDesktop = ByteArray(32) { 0x11 }
        val pkDesktop = X25519.derivarClavePublica(skDesktop)
        val pskTokenReal = ByteArray(32) { 0x33 }
        val pskTokenInvalido = ByteArray(32) { 0x34 }

        val servidor = ServidorSyncP2P.bind(skDesktop, pskTokenReal, puerto = 0)
        val puertoAsignado = servidor.puertoLocal

        val hiloServidor = Thread {
            try {
                servidor.atenderSincronizacion(ContenidoBoveda()) {}
            } catch (_: Exception) {}
        }
        hiloServidor.start()

        val payloadQrInvalido = SyncQrPayload(
            ips = listOf("127.0.0.1"),
            puerto = puertoAsignado,
            clavePublicaDesktop = pkDesktop,
            tokenPsk = pskTokenInvalido,
            version = 2
        )

        try {
            ClienteSyncP2P.sincronizarConServidor(payloadQrInvalido, ContenidoBoveda(), timeoutSegundos = 3L)
        } finally {
            hiloServidor.join(4000)
        }
    }
}
