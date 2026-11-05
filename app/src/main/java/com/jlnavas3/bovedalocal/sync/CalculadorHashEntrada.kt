/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.Entrada
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.MessageDigest
import java.util.UUID

/**
 * Calculador canónico binario del content_hash de una entrada mediante SHA-256.
 * Espejo exacto de `src/sync/merge/entry_hasher.rs` en Rust.
 *
 * Aplica Length-Prefixed Canonical Encoding para garantizar determinismo absoluto bit a bit
 * entre plataformas y arquitecturas distintas (x86_64 Desktop vs ARM64 Android).
 */
object CalculadorHashEntrada {

    /**
     * Calcula el hash canónico de 32 bytes (SHA-256) del contenido de una entrada.
     * Excluye estrictamente los metadatos de sincronización (`revision` y `lastSyncHash`).
     */
    fun calcularContentHash(e: Entrada): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")

        // 1. UUID binario (16 bytes)
        val uuidBytes = try {
            val u = UUID.fromString(e.id)
            ByteBuffer.allocate(16)
                .putLong(u.mostSignificantBits)
                .putLong(u.leastSignificantBits)
                .array()
        } catch (_: Exception) {
            ByteArray(16)
        }
        md.update(uuidBytes)

        // Helper para strings con prefijo de longitud u32 little-endian
        fun escribirCampo(texto: String) {
            val bytes = texto.toByteArray(Charsets.UTF_8)
            val lenBytes = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(bytes.size)
                .array()
            md.update(lenBytes)
            md.update(bytes)
        }

        // 2. Campos nucleares en orden canónico
        escribirCampo(e.titulo)
        escribirCampo(e.usuario)
        escribirCampo(e.contrasena)
        escribirCampo(e.notas)
        escribirCampo(e.secretoTotp ?: "")

        // 3. URLs ordenadas
        val lenUrls = ByteBuffer.allocate(4)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(e.urls.size)
            .array()
        md.update(lenUrls)
        for (url in e.urls) {
            escribirCampo(url)
        }

        // 4. Estado de eliminación
        val eliminado = if (e.eliminadaEn > 0L) 1.toByte() else 0.toByte()
        md.update(byteArrayOf(eliminado))

        return md.digest()
    }

    /**
     * Convierte un arreglo de 32 bytes a su representación hexadecimal en minúsculas (64 caracteres).
     */
    fun bytesAHex(bytes: ByteArray): String {
        val hexChars = "0123456789abcdef"
        val result = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            val i = b.toInt() and 0xFF
            result.append(hexChars[i shr 4])
            result.append(hexChars[i and 0x0F])
        }
        return result.toString()
    }

    /**
     * Convierte una cadena hexadecimal de 64 caracteres a un arreglo de 32 bytes.
     */
    fun hexABytes(hex: String): ByteArray {
        require(hex.length == 64) { "La cadena hexadecimal debe tener exactamente 64 caracteres." }
        val bytes = ByteArray(32)
        for (i in 0 until 32) {
            val index = i * 2
            bytes[i] = hex.substring(index, index + 2).toInt(16).toByte()
        }
        return bytes
    }
}
