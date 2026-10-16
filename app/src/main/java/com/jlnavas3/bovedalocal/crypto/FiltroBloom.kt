/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.crypto

import java.io.InputStream
import java.util.BitSet

/**
 * Estructura de datos probabilística compacta y de alto rendimiento (Filtro de Bloom)
 * para auditoría offline de contraseñas filtradas con tasa mínima de falsos positivos y cero falsos negativos.
 */
class FiltroBloom(
    val numBits: Int = 524288, // 64 KB por defecto
    val numHashes: Int = 5,
    private val bitSet: BitSet = BitSet(numBits)
) {

    fun insertar(texto: String) {
        val clave = texto.trim().lowercase()
        if (clave.isEmpty()) return
        val h1 = hashFnv1a(clave)
        val h2 = hashMurmurLike(clave)
        for (i in 0 until numHashes) {
            val bit = Math.floorMod(h1 + i.toLong() * h2, numBits.toLong()).toInt()
            bitSet.set(bit)
        }
    }

    fun contiene(texto: String): Boolean {
        val clave = texto.trim().lowercase()
        if (clave.isEmpty()) return false
        val h1 = hashFnv1a(clave)
        val h2 = hashMurmurLike(clave)
        for (i in 0 until numHashes) {
            val bit = Math.floorMod(h1 + i.toLong() * h2, numBits.toLong()).toInt()
            if (!bitSet.get(bit)) return false
        }
        return true
    }

    fun aByteArray(): ByteArray {
        val bytes = bitSet.toByteArray()
        val esperado = (numBits + 7) / 8
        return if (bytes.size == esperado) bytes
        else bytes.copyOf(esperado)
    }

    companion object {
        fun desdeBytes(bytes: ByteArray, numBits: Int = 524288, numHashes: Int = 5): FiltroBloom {
            val bitSet = BitSet.valueOf(bytes)
            return FiltroBloom(numBits, numHashes, bitSet)
        }

        fun desdeStream(input: InputStream, numBits: Int = 524288, numHashes: Int = 5): FiltroBloom {
            val bytes = input.readBytes()
            return desdeBytes(bytes, numBits, numHashes)
        }

        private fun hashFnv1a(s: String): Long {
            var h = -3750763034362895579L
            for (ch in s) {
                h = h xor ch.code.toLong()
                h = h * 1099511628211L
            }
            return h
        }

        private fun hashMurmurLike(s: String): Long {
            var h = 1125899906842597L
            for (ch in s) {
                h = 31L * h + ch.code.toLong()
            }
            return h
        }
    }
}
