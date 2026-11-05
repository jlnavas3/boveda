/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import java.math.BigInteger
import java.security.SecureRandom

/**
 * Implementación determinista, pura y autocontenida de X25519 (RFC 7748).
 * Espejo exacto de x25519-dalek en Rust.
 *
 * Utiliza la escalera de Montgomery sobre la curva de Edwards/Montgomery Curve25519:
 * y^2 = x^3 + 486662x^2 + x (mod 2^255 - 19)
 *
 * Compatible de forma universal y sin depender de proveedores JCA variables en Android o JVM.
 */
object X25519 {

    private val P = BigInteger.valueOf(2).pow(255).subtract(BigInteger.valueOf(19))
    private val A24 = BigInteger.valueOf(121665)
    private val BASE_U = BigInteger.valueOf(9)

    fun generarClavePrivada(): ByteArray {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return bytes
    }

    fun derivarClavePublica(sk: ByteArray): ByteArray {
        require(sk.size == 32) { "La clave privada debe tener exactamente 32 bytes." }
        return escalarMult(sk, BASE_U)
    }

    fun calcularDiffieHellman(sk: ByteArray, pk: ByteArray): ByteArray {
        require(sk.size == 32) { "La clave privada debe tener exactamente 32 bytes." }
        require(pk.size == 32) { "La clave pública remota debe tener exactamente 32 bytes." }
        val u = bytesALittleEndianBigInteger(pk)
        return escalarMult(sk, u)
    }

    private fun escalarMult(kBytes: ByteArray, uCoord: BigInteger): ByteArray {
        val clamped = kBytes.copyOf(32)
        clamped[0] = (clamped[0].toInt() and 248).toByte()
        clamped[31] = ((clamped[31].toInt() and 127) or 64).toByte()

        val k = bytesALittleEndianBigInteger(clamped)
        val x1 = uCoord.mod(P)
        var x2 = BigInteger.ONE
        var z2 = BigInteger.ZERO
        var x3 = x1
        var z3 = BigInteger.ONE
        var swap = 0

        for (t in 254 downTo 0) {
            val kt = if (k.testBit(t)) 1 else 0
            swap = swap xor kt
            if (swap != 0) {
                val tmpX = x2; x2 = x3; x3 = tmpX
                val tmpZ = z2; z2 = z3; z3 = tmpZ
            }
            swap = kt

            val a = x2.add(z2).mod(P)
            val aa = a.multiply(a).mod(P)
            val b = x2.subtract(z2).mod(P)
            val bb = b.multiply(b).mod(P)
            val e = aa.subtract(bb).mod(P)
            val c = x3.add(z3).mod(P)
            val d = x3.subtract(z3).mod(P)
            val da = d.multiply(a).mod(P)
            val cb = c.multiply(b).mod(P)
            x3 = da.add(cb).modPow(BigInteger.TWO, P)
            z3 = x1.multiply(da.subtract(cb).modPow(BigInteger.TWO, P)).mod(P)
            x2 = aa.multiply(bb).mod(P)
            z2 = e.multiply(aa.add(A24.multiply(e))).mod(P)
        }

        if (swap != 0) {
            val tmpX = x2; x2 = x3; x3 = tmpX
            val tmpZ = z2; z2 = z3; z3 = tmpZ
        }

        val result = x2.multiply(z2.modInverse(P)).mod(P)
        return littleEndianBigIntegerABytes(result)
    }

    private fun bytesALittleEndianBigInteger(bytes: ByteArray): BigInteger {
        val rev = bytes.reversedArray()
        return BigInteger(1, rev)
    }

    private fun littleEndianBigIntegerABytes(bi: BigInteger): ByteArray {
        val raw = bi.toByteArray()
        val result = ByteArray(32)
        var srcIdx = raw.size - 1
        var dstIdx = 0
        while (srcIdx >= 0 && dstIdx < 32) {
            result[dstIdx] = raw[srcIdx]
            srcIdx--
            dstIdx++
        }
        return result
    }
}
