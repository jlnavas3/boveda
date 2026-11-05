/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Cifrador de sesión simétrica ChaCha20-Poly1305 con protección Anti-Replay estricta.
 * Espejo exacto de `SessionCipher` en `src/sync/p2p/session_crypto.rs` en Rust.
 *
 * Contrato canónico de trama:
 * - Secuencia: entero u64 little-endian monotónico (8 bytes).
 * - Nonce (12 bytes): `[0, 0, 0, 0] || seq_le (8B)`
 * - AAD (8 bytes): `seq_le`
 * - Cifrado: ChaCha20-Poly1305
 * - Formato final: `seq_le (8B) || texto_cifrado_con_tag` (mínimo 24 bytes).
 */
class CifradorSesionP2P(
    private val txKey: ByteArray,
    private val rxKey: ByteArray
) {
    private var txSeq: Long = 0L
    private var rxSeq: Long = 0L

    init {
        require(txKey.size == 32) { "La clave TX debe medir 32 bytes." }
        require(rxKey.size == 32) { "La clave RX debe medir 32 bytes." }
    }

    fun txSeq(): Long = txSeq
    fun rxSeq(): Long = rxSeq

    /**
     * Cifra un mensaje en claro (`plaintext`) produciendo una trama autenticada:
     * `seq_le (8B) || ciphertext_with_tag`.
     * Incrementa monótonamente `txSeq += 1`.
     */
    fun seal(plaintext: ByteArray): ByteArray {
        val currentSeq = txSeq
        val seqBytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(currentSeq).array()

        val nonceBytes = ByteArray(12)
        System.arraycopy(seqBytes, 0, nonceBytes, 4, 8)

        val cipher = Cipher.getInstance("ChaCha20-Poly1305")
        val keySpec = SecretKeySpec(txKey, "ChaCha20")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(nonceBytes))
        cipher.updateAAD(seqBytes)

        val ciphertextWithTag = cipher.doFinal(plaintext)
        txSeq++

        val frame = ByteArray(8 + ciphertextWithTag.size)
        System.arraycopy(seqBytes, 0, frame, 0, 8)
        System.arraycopy(ciphertextWithTag, 0, frame, 8, ciphertextWithTag.size)
        return frame
    }

    /**
     * Descifra y valida la autenticidad e integridad de una trama recibida:
     * Exige `seq == rxSeq` (neutraliza ataques Replay y tramas fuera de secuencia).
     * Incrementa monótonamente `rxSeq += 1`.
     */
    fun open(frame: ByteArray): ByteArray {
        require(frame.size >= 24) {
            "Trama P2P demasiado corta: longitud ${frame.size} bytes (mínimo 24)."
        }

        val seq = ByteBuffer.wrap(frame, 0, 8).order(ByteOrder.LITTLE_ENDIAN).long
        require(seq == rxSeq) {
            "Ataque Replay o trama fuera de secuencia detectada: esperado seq=$rxSeq, recibido seq=$seq."
        }

        val seqBytes = frame.copyOfRange(0, 8)
        val nonceBytes = ByteArray(12)
        System.arraycopy(seqBytes, 0, nonceBytes, 4, 8)

        val cipher = Cipher.getInstance("ChaCha20-Poly1305")
        val keySpec = SecretKeySpec(rxKey, "ChaCha20")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(nonceBytes))
        cipher.updateAAD(seqBytes)

        val plaintext = cipher.doFinal(frame, 8, frame.size - 8)
        rxSeq++
        return plaintext
    }

    /** Limpia las claves simétricas de la memoria RAM. */
    fun destruir() {
        txKey.fill(0)
        rxKey.fill(0)
    }

    companion object {
        /**
         * Inicializa la sesión para el rol Cliente (Android):
         * - Transmite (TX) usando `keyA2D` (Android -> Desktop).
         * - Recibe (RX) usando `keyD2A` (Desktop -> Android).
         */
        fun paraCliente(keys: ResultadoHandshake): CifradorSesionP2P {
            return CifradorSesionP2P(keys.keyA2D.clone(), keys.keyD2A.clone())
        }

        /**
         * Inicializa la sesión para el rol Servidor (Desktop):
         * - Transmite (TX) usando `keyD2A` (Desktop -> Android).
         * - Recibe (RX) usando `keyA2D` (Android -> Desktop).
         */
        fun paraServidor(keys: ResultadoHandshake): CifradorSesionP2P {
            return CifradorSesionP2P(keys.keyD2A.clone(), keys.keyA2D.clone())
        }
    }
}
