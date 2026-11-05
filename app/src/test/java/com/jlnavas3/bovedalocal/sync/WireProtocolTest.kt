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
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class WireProtocolTest {

    @Test
    fun testFramingLecturaYEscritura() {
        val payload = "Prueba de framing canónico".toByteArray(Charsets.UTF_8)
        val baos = ByteArrayOutputStream()
        WireProtocol.escribirTrama(baos, payload)

        val bais = ByteArrayInputStream(baos.toByteArray())
        val leido = WireProtocol.leerTrama(bais)

        assertEquals(String(payload, Charsets.UTF_8), String(leido, Charsets.UTF_8))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testLimiteTamanoTrama() {
        val payload = ByteArray(100)
        val baos = ByteArrayOutputStream()
        WireProtocol.escribirTrama(baos, payload)

        val bais = ByteArrayInputStream(baos.toByteArray())
        WireProtocol.leerTrama(bais, maxBytes = 50) // Debe lanzar IllegalArgumentException
    }

    @Test
    fun testSerializacionMensajeSyncPayload() {
        val boveda = ContenidoBoveda(
            entradas = listOf(Entrada(id = "test-1", titulo = "GitHub", usuario = "octocat"))
        )
        val msg = MensajeP2P.SyncPayload(boveda)
        val bytes = WireProtocol.serializarMensaje(msg)

        assertEquals(WireProtocol.TIPO_MSG_SYNC_PAYLOAD, bytes[0])

        val deserializado = WireProtocol.deserializarMensaje(bytes)
        assertTrue(deserializado is MensajeP2P.SyncPayload)
        assertEquals("GitHub", (deserializado as MensajeP2P.SyncPayload).boveda.entradas[0].titulo)
    }

    @Test
    fun testSerializacionMensajeCommitAck() {
        val msg = MensajeP2P.CommitAck("OK")
        val bytes = WireProtocol.serializarMensaje(msg)

        assertEquals(WireProtocol.TIPO_MSG_COMMIT_ACK, bytes[0])

        val deserializado = WireProtocol.deserializarMensaje(bytes)
        assertTrue(deserializado is MensajeP2P.CommitAck)
        assertEquals("OK", (deserializado as MensajeP2P.CommitAck).ack)
    }
}
