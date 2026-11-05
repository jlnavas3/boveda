/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Protocolo de Apretón de Manos (Handshake 1-RTT) con autenticación mutua basada en PSK y X25519.
 * Espejo exacto de `src/sync/p2p/handshake.rs` en Rust.
 *
 * Contrato criptográfico canónico:
 * - Intercambio de claves: X25519 (RFC 7748)
 * - Derivación de claves: HKDF-SHA256 (RFC 5869)
 * - Autenticación mutua: HMAC-SHA256
 */
object HandshakeP2P {

    private val INFO_PREFIX = "BVDA-P2P-V2".toByteArray(Charsets.UTF_8)
    private val CLIENT_AUTH_LABEL = "CLIENT-AUTH".toByteArray(Charsets.UTF_8)
    private val SERVER_AUTH_LABEL = "SERVER-AUTH".toByteArray(Charsets.UTF_8)

    /**
     * Deriva las 3 claves de sesión usando HKDF-SHA256:
     * - Salt: `pskToken` (32 bytes)
     * - IKM: `dhShared` (32 bytes)
     * - Info: `"BVDA-P2P-V2" || pkDesktop || pkAndroid` (75 bytes)
     *
     * Retorna [ResultadoHandshake] con `keyA2D`, `keyD2A` y `handshakeKey` (96 bytes totales).
     */
    fun derivarClavesSesion(
        pkDesktop: ByteArray,
        pkAndroid: ByteArray,
        dhShared: ByteArray,
        pskToken: ByteArray
    ): ResultadoHandshake {
        require(pkDesktop.size == 32 && pkAndroid.size == 32 && dhShared.size == 32 && pskToken.size == 32) {
            "Todos los bloques criptográficos deben ser de exactamente 32 bytes."
        }

        val info = INFO_PREFIX + pkDesktop + pkAndroid
        require(info.size == 75) { "El info del HKDF debe medir exactamente 75 bytes." }

        // HKDF-Extract: PRK = HMAC-SHA256(key = pskToken, data = dhShared)
        val prk = hmacSha256(pskToken, dhShared)

        // HKDF-Expand: OKM de 96 bytes con info de 75 bytes
        val okm = hkdfExpandSha256(prk, info, 96)

        val resultado = ResultadoHandshake(
            keyA2D = okm.copyOfRange(0, 32),
            keyD2A = okm.copyOfRange(32, 64),
            handshakeKey = okm.copyOfRange(64, 96)
        )
        okm.fill(0)
        prk.fill(0)
        return resultado
    }

    /**
     * Construye la trama `ClientHello` (64 bytes, Android -> Desktop):
     * `pkAndroid (32B) || HMAC-SHA256(handshakeKey, "CLIENT-AUTH" || pkAndroid || pkDesktop) (32B)`.
     */
    fun construirClientHello(
        skAndroid: ByteArray,
        pkDesktop: ByteArray,
        pskToken: ByteArray
    ): Pair<ByteArray, ResultadoHandshake> {
        val pkAndroid = X25519.derivarClavePublica(skAndroid)
        val dhShared = X25519.calcularDiffieHellman(skAndroid, pkDesktop)
        val claves = derivarClavesSesion(pkDesktop, pkAndroid, dhShared, pskToken)
        dhShared.fill(0)

        val macData = CLIENT_AUTH_LABEL + pkAndroid + pkDesktop
        val authTag = hmacSha256(claves.handshakeKey, macData)

        val clientHello = ByteArray(64)
        System.arraycopy(pkAndroid, 0, clientHello, 0, 32)
        System.arraycopy(authTag, 0, clientHello, 32, 32)

        return Pair(clientHello, claves)
    }

    /**
     * Procesa la trama `ClientHello` en el rol Servidor (Desktop) y genera la respuesta `ServerHello`:
     * Retorna `(serverHello, claves)` o `null` si la autenticación falla.
     */
    fun responderServerHello(
        clientHello: ByteArray,
        pkDesktop: ByteArray,
        pskToken: ByteArray,
        skDesktop: ByteArray
    ): Pair<ByteArray, ResultadoHandshake>? {
        if (clientHello.size != 64) return null
        val pkAndroid = clientHello.copyOfRange(0, 32)
        val clientMac = clientHello.copyOfRange(32, 64)

        val dhShared = X25519.calcularDiffieHellman(skDesktop, pkAndroid)
        val claves = derivarClavesSesion(pkDesktop, pkAndroid, dhShared, pskToken)
        dhShared.fill(0)

        val macData = CLIENT_AUTH_LABEL + pkAndroid + pkDesktop
        val expectedMac = hmacSha256(claves.handshakeKey, macData)

        if (!MessageDigest.isEqual(clientMac, expectedMac)) {
            claves.destruir()
            return null
        }

        val serverData = SERVER_AUTH_LABEL + pkDesktop + pkAndroid
        val serverMac = hmacSha256(claves.handshakeKey, serverData)

        return Pair(serverMac, claves)
    }

    /**
     * Valida la trama `ServerHello` (32 bytes) en el cliente (Android):
     * Verifica `serverHello == HMAC-SHA256(handshakeKey, "SERVER-AUTH" || pkDesktop || pkAndroid)`.
     */
    fun verificarServerHello(
        serverHello: ByteArray,
        pkDesktop: ByteArray,
        pkAndroid: ByteArray,
        handshakeKey: ByteArray
    ): Boolean {
        if (serverHello.size != 32) return false
        val serverData = SERVER_AUTH_LABEL + pkDesktop + pkAndroid
        val expectedServerMac = hmacSha256(handshakeKey, serverData)
        return MessageDigest.isEqual(serverHello, expectedServerMac)
    }

    fun hmacSha256(key: ByteArray, data: ByteArray): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key, "HmacSHA256"))
        return mac.doFinal(data)
    }

    private fun hkdfExpandSha256(prk: ByteArray, info: ByteArray, length: Int): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(prk, "HmacSHA256"))
        val okm = ByteArray(length)
        var t = ByteArray(0)
        var offset = 0
        var i = 1
        while (offset < length) {
            mac.update(t)
            mac.update(info)
            mac.update(i.toByte())
            t = mac.doFinal()
            val copyLen = minOf(t.size, length - offset)
            System.arraycopy(t, 0, okm, offset, copyLen)
            offset += copyLen
            i++
        }
        return okm
    }
}
