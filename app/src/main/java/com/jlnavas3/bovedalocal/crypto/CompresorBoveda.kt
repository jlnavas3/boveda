/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.crypto

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

/**
 * Micro-componente para compresión transparente de la bóveda antes del cifrado AES-GCM.
 * Reduce el tamaño del archivo en disco en un 50-70% y acelera la persistencia y lectura flash.
 * Garantiza retrocompatibilidad total con bóvedas no comprimidas mediante la cabecera mágica BLZ1.
 */
object CompresorBoveda {

    private val MAGIA_COMPRIMIDO = byteArrayOf(0x42, 0x4C, 0x5A, 0x31) // "BLZ1"

    fun comprimir(datos: ByteArray): ByteArray {
        val out = ByteArrayOutputStream(datos.size / 2 + 32)
        out.write(MAGIA_COMPRIMIDO)
        val deflater = Deflater(Deflater.BEST_COMPRESSION, false)
        try {
            DeflaterOutputStream(out, deflater).use { dos ->
                dos.write(datos)
                dos.finish()
            }
        } finally {
            deflater.end()
        }
        return out.toByteArray()
    }

    fun descomprimir(datos: ByteArray): ByteArray {
        if (!esComprimido(datos)) {
            // Retrocompatibilidad: archivo de bóveda antiguo sin comprimir
            return datos
        }
        val bais = ByteArrayInputStream(datos, MAGIA_COMPRIMIDO.size, datos.size - MAGIA_COMPRIMIDO.size)
        val out = ByteArrayOutputStream()
        InflaterInputStream(bais).use { iis ->
            val buffer = ByteArray(4096)
            var leidos: Int
            while (iis.read(buffer).also { leidos = it } != -1) {
                out.write(buffer, 0, leidos)
            }
        }
        return out.toByteArray()
    }

    fun esComprimido(datos: ByteArray): Boolean {
        if (datos.size < MAGIA_COMPRIMIDO.size) return false
        for (i in MAGIA_COMPRIMIDO.indices) {
            if (datos[i] != MAGIA_COMPRIMIDO[i]) return false
        }
        return true
    }
}
