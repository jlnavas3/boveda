/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

/**
 * Claves simétricas derivadas del Handshake 1-RTT P2P.
 * Espejo exacto de `HandshakeKeys` en Rust.
 *
 * Incluye función [destruir] para sobreescribir con ceros las claves en memoria RAM.
 */
data class ResultadoHandshake(
    /** Clave de cifrado AEAD para tramas Android -> Desktop (32 bytes). */
    val keyA2D: ByteArray,
    /** Clave de cifrado AEAD para tramas Desktop -> Android (32 bytes). */
    val keyD2A: ByteArray,
    /** Clave HMAC para autenticación mutua de los mensajes de Handshake (32 bytes). */
    val handshakeKey: ByteArray
) {
    init {
        require(keyA2D.size == 32 && keyD2A.size == 32 && handshakeKey.size == 32) {
            "Todas las claves del handshake deben tener exactamente 32 bytes."
        }
    }

    /** Limpia inmediatamente de la memoria RAM todas las claves secretas. */
    fun destruir() {
        keyA2D.fill(0)
        keyD2A.fill(0)
        handshakeKey.fill(0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ResultadoHandshake) return false
        if (!keyA2D.contentEquals(other.keyA2D)) return false
        if (!keyD2A.contentEquals(other.keyD2A)) return false
        if (!handshakeKey.contentEquals(other.handshakeKey)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = keyA2D.contentHashCode()
        result = 31 * result + keyD2A.contentHashCode()
        result = 31 * result + handshakeKey.contentHashCode()
        return result
    }
}
