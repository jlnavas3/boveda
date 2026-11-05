/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SesionCriptoP2PTest {

    @Test
    fun testVectorOroParidadConDesktop() {
        // Semillas deterministas del Vector de Oro proporcionado por Desktop
        val skDesktop = ByteArray(32) { 0x11 }
        val skAndroid = ByteArray(32) { 0x22 }
        val pskToken = ByteArray(32) { 0x33 }

        val pkDesktopEsperada = "7b4e909bbe7ffe44c465a220037d608ee35897d31ef972f07f74892cb0f73f13"
        val pkAndroidEsperada = "0faa684ed28867b97f4a6a2dee5df8ce974e76b7018e3f22a1c4cf2678570f20"
        val dhSharedEsperada = "9e004098efc091d4ec2663b4e9f5cfd4d7064571690b4bea97ab146ab9f35056"
        val keyA2DEsperada = "79cc89c48ff1ab338a04c6593eb4845d29311f995ef3ba33f68502897a8c1fda"
        val keyD2AEsperada = "7bcdf48db0200d5754a121e191d72eed81c3d6d35205fe412cfa9b15022c6d2c"
        val handshakeKeyEsperada = "4ac9fe2ecce450269f8bd4fa7d367e02145b8484b2bfeda9911f59fe82d984eb"
        val serverHelloEsperado = "addcc7157c95a2a2654842da84ee65249b2a53ebd3c4a4aba9269acfd5d2a735"

        val pkDesktop = X25519.derivarClavePublica(skDesktop)
        val pkAndroid = X25519.derivarClavePublica(skAndroid)
        val dhShared = X25519.calcularDiffieHellman(skAndroid, pkDesktop)

        assertEquals(pkDesktopEsperada, CalculadorHashEntrada.bytesAHex(pkDesktop))
        assertEquals(pkAndroidEsperada, CalculadorHashEntrada.bytesAHex(pkAndroid))
        assertEquals(dhSharedEsperada, CalculadorHashEntrada.bytesAHex(dhShared))

        val claves = HandshakeP2P.derivarClavesSesion(pkDesktop, pkAndroid, dhShared, pskToken)

        assertEquals(keyA2DEsperada, CalculadorHashEntrada.bytesAHex(claves.keyA2D))
        assertEquals(keyD2AEsperada, CalculadorHashEntrada.bytesAHex(claves.keyD2A))
        assertEquals(handshakeKeyEsperada, CalculadorHashEntrada.bytesAHex(claves.handshakeKey))

        // ClientHello
        val (clientHello, clavesCliente) = HandshakeP2P.construirClientHello(skAndroid, pkDesktop, pskToken)
        assertEquals(64, clientHello.size)
        assertEquals(keyA2DEsperada, CalculadorHashEntrada.bytesAHex(clavesCliente.keyA2D))

        // ServerHello
        val serverResponse = HandshakeP2P.responderServerHello(clientHello, pkDesktop, pskToken, skDesktop)
        assertNotNull(serverResponse)
        val (serverHello, clavesServer) = serverResponse!!

        assertEquals(serverHelloEsperado, CalculadorHashEntrada.bytesAHex(serverHello))

        // Verificación en el cliente
        val serverValido = HandshakeP2P.verificarServerHello(serverHello, pkDesktop, pkAndroid, clavesCliente.handshakeKey)
        assertTrue(serverValido)

        claves.destruir()
        clavesCliente.destruir()
        clavesServer.destruir()
    }

    @Test
    fun testCifradoBidireccionalClienteServidor() {
        val skDesktop = ByteArray(32) { 0x11 }
        val skAndroid = ByteArray(32) { 0x22 }
        val pskToken = ByteArray(32) { 0x33 }

        val pkDesktop = X25519.derivarClavePublica(skDesktop)
        val (clientHello, clavesCliente) = HandshakeP2P.construirClientHello(skAndroid, pkDesktop, pskToken)
        val (serverHello, clavesServer) = HandshakeP2P.responderServerHello(clientHello, pkDesktop, pskToken, skDesktop)!!

        val pkAndroid = X25519.derivarClavePublica(skAndroid)
        assertTrue(HandshakeP2P.verificarServerHello(serverHello, pkDesktop, pkAndroid, clavesCliente.handshakeKey))

        val clienteCipher = CifradorSesionP2P.paraCliente(clavesCliente)
        val servidorCipher = CifradorSesionP2P.paraServidor(clavesServer)

        // 1. Android transmite a Desktop
        val mensajeAndroid = "Sincronizacion Boveda Local P2P V2".toByteArray(Charsets.UTF_8)
        val tramaA2D = clienteCipher.seal(mensajeAndroid)
        val textoDescifradoEnDesktop = servidorCipher.open(tramaA2D)
        assertEquals(String(mensajeAndroid, Charsets.UTF_8), String(textoDescifradoEnDesktop, Charsets.UTF_8))

        // 2. Desktop responde a Android
        val mensajeDesktop = "Confirmacion de recepcion OK".toByteArray(Charsets.UTF_8)
        val tramaD2A = servidorCipher.seal(mensajeDesktop)
        val textoDescifradoEnAndroid = clienteCipher.open(tramaD2A)
        assertEquals(String(mensajeDesktop, Charsets.UTF_8), String(textoDescifradoEnAndroid, Charsets.UTF_8))

        clienteCipher.destruir()
        servidorCipher.destruir()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testRechazoAtaqueReplay() {
        val skDesktop = ByteArray(32) { 0x11 }
        val skAndroid = ByteArray(32) { 0x22 }
        val pskToken = ByteArray(32) { 0x33 }

        val pkDesktop = X25519.derivarClavePublica(skDesktop)
        val (clientHello, clavesCliente) = HandshakeP2P.construirClientHello(skAndroid, pkDesktop, pskToken)
        val (_, clavesServer) = HandshakeP2P.responderServerHello(clientHello, pkDesktop, pskToken, skDesktop)!!

        val clienteCipher = CifradorSesionP2P.paraCliente(clavesCliente)
        val servidorCipher = CifradorSesionP2P.paraServidor(clavesServer)

        val trama = clienteCipher.seal("Mensaje unico".toByteArray(Charsets.UTF_8))
        servidorCipher.open(trama) // Primera vez: éxito

        // Ataque de repetición: enviar la misma trama otra vez
        servidorCipher.open(trama) // Debe lanzar excepción
    }

    @Test
    fun testFalloConPskAlterado() {
        val skDesktop = ByteArray(32) { 0x11 }
        val skAndroid = ByteArray(32) { 0x22 }
        val pskToken = ByteArray(32) { 0x33 }
        val pskAtacante = ByteArray(32) { 0x34 } // 1 byte alterado

        val pkDesktop = X25519.derivarClavePublica(skDesktop)
        val (clientHello, _) = HandshakeP2P.construirClientHello(skAndroid, pkDesktop, pskAtacante)

        // El servidor rechaza la conexión porque el HMAC del ClientHello no coincide con su PSK
        val serverResponse = HandshakeP2P.responderServerHello(clientHello, pkDesktop, pskToken, skDesktop)
        assertTrue(serverResponse == null)
    }
}
