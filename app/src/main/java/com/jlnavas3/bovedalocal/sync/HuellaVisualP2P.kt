/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import java.security.MessageDigest

/**
 * Calculador de la Huella Visual Determinista (Visual Fingerprint) para emparejamiento P2P.
 * Espejo exacto de `src/sync/p2p/fingerprint.rs` en Rust.
 *
 * Toma la clave pública efímera (32 bytes) y el token PSK efímero (32 bytes),
 * calcula SHA-256(clavePublica || tokenPsk) y formatea los primeros 6 bytes
 * en hexadecimal mayúsculas separados por dos puntos (ej. "74:5A:63:02:1A:A5").
 *
 * Permite que el usuario verifique visualmente la autenticidad del canal sin revelar el secreto.
 */
object HuellaVisualP2P {

    /**
     * Calcula la huella visual de 6 bytes en formato "XX:XX:XX:XX:XX:XX".
     */
    fun calcular(clavePublica: ByteArray, tokenPsk: ByteArray): String {
        require(clavePublica.size == 32) { "La clave pública efímera debe tener exactamente 32 bytes." }
        require(tokenPsk.size == 32) { "El token PSK efímero debe tener exactamente 32 bytes." }

        val md = MessageDigest.getInstance("SHA-256")
        md.update(clavePublica)
        md.update(tokenPsk)
        val digest = md.digest()

        val sb = StringBuilder(17) // 6 * 2 chars + 5 colons = 17 chars
        val hexChars = "0123456789ABCDEF"
        for (i in 0 until 6) {
            val b = digest[i].toInt() and 0xFF
            sb.append(hexChars[b shr 4])
            sb.append(hexChars[b and 0x0F])
            if (i < 5) sb.append(':')
        }
        return sb.toString()
    }
}
