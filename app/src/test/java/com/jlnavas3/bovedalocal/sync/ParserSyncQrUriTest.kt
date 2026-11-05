/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ParserSyncQrUriTest {

    private val kHexCanon = "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20"
    private val tHexCanon = "a1a2a3a4a5a6a7a8a9aaabacadaeafb0b1b2b3b4b5b6b7b8b9babbbcbdbebfc0"
    private val huellaEsperada = "74:5A:63:02:1A:A5"

    @Test
    fun testVectorCanonicoP2P() {
        val uri = "bvda-sync://192.168.1.50,10.0.0.15:54321?k=$kHexCanon&t=$tHexCanon&v=2"

        val resultado = ParserSyncQrUri.parsear(uri)
        assertTrue(resultado.isSuccess)

        val payload = resultado.getOrThrow()
        assertEquals(listOf("192.168.1.50", "10.0.0.15"), payload.ips)
        assertEquals(54321, payload.puerto)
        assertEquals(2, payload.version)
        assertEquals(kHexCanon, payload.clavePublicaHex)
        assertEquals(tHexCanon, payload.tokenPskHex)

        // Verificación de la Huella Visual Determinista
        assertEquals(huellaEsperada, payload.huellaVisual)

        // Roundtrip exacto
        val serializado = ParserSyncQrUri.serializar(payload)
        assertEquals(uri, serializado)
    }

    @Test
    fun testRechazoIpPublicaEnLista() {
        val uri = "bvda-sync://192.168.1.50,8.8.8.8:54321?k=$kHexCanon&t=$tHexCanon&v=2"
        val resultado = ParserSyncQrUri.parsear(uri)
        assertTrue(resultado.isFailure)
    }

    @Test
    fun testRechazoDominioODns() {
        val uri = "bvda-sync://mi-desktop.local:54321?k=$kHexCanon&t=$tHexCanon&v=2"
        val resultado = ParserSyncQrUri.parsear(uri)
        assertTrue(resultado.isFailure)
    }

    @Test
    fun testRechazoLoopback() {
        val uri = "bvda-sync://127.0.0.1:54321?k=$kHexCanon&t=$tHexCanon&v=2"
        val resultado = ParserSyncQrUri.parsear(uri)
        assertTrue(resultado.isFailure)
    }

    @Test
    fun testRechazoPuertosFueraDeRango() {
        // Puerto privilegiado (< 1024)
        val uriPrivilegiado = "bvda-sync://192.168.1.50:80?k=$kHexCanon&t=$tHexCanon&v=2"
        assertTrue(ParserSyncQrUri.parsear(uriPrivilegiado).isFailure)

        // Puerto inválido (> 65535)
        val uriExcedido = "bvda-sync://192.168.1.50:70000?k=$kHexCanon&t=$tHexCanon&v=2"
        assertTrue(ParserSyncQrUri.parsear(uriExcedido).isFailure)
    }

    @Test
    fun testRechazoParametrosCriptograficosInvalidos() {
        // k con 63 caracteres (longitud inválida)
        val uriCorta = "bvda-sync://192.168.1.50:54321?k=${kHexCanon.drop(1)}&t=$tHexCanon&v=2"
        assertTrue(ParserSyncQrUri.parsear(uriCorta).isFailure)

        // t con caracteres no hexadecimales
        val tInvalida = tHexCanon.drop(2) + "ZZ"
        val uriNoHex = "bvda-sync://192.168.1.50:54321?k=$kHexCanon&t=$tInvalida&v=2"
        assertTrue(ParserSyncQrUri.parsear(uriNoHex).isFailure)
    }

    @Test
    fun testRechazoVersionIncompatible() {
        val uriV0 = "bvda-sync://192.168.1.50:54321?k=$kHexCanon&t=$tHexCanon&v=0"
        assertTrue(ParserSyncQrUri.parsear(uriV0).isFailure)
    }

    @Test
    fun testAceptaVersion1Y2() {
        val uriV1 = "bvda-sync://192.168.1.50:54321?k=$kHexCanon&t=$tHexCanon&v=1"
        assertTrue(ParserSyncQrUri.parsear(uriV1).isSuccess)
        val uriV2 = "bvda-sync://192.168.1.50:54321?k=$kHexCanon&t=$tHexCanon&v=2"
        assertTrue(ParserSyncQrUri.parsear(uriV2).isSuccess)
    }

    @Test
    fun testUriConAliasDispositivo() {
        val alias = "HONOR DNP-NX9"
        val aliasEncoded = java.net.URLEncoder.encode(alias, "UTF-8")
        val uri = "bvda-sync://192.168.1.50:54321?k=$kHexCanon&t=$tHexCanon&v=2&d=$aliasEncoded"

        val resultado = ParserSyncQrUri.parsear(uri)
        assertTrue(resultado.isSuccess)
        val payload = resultado.getOrThrow()
        assertEquals(alias, payload.aliasDispositivo)

        // Roundtrip con alias
        val serializado = ParserSyncQrUri.serializar(payload)
        assertEquals(uri, serializado)
    }
}
