/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.crypto.FiltroBloom
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class FiltroBloomTest {

    @Test
    fun `elementos insertados siempre dan positivo en el filtro`() {
        val filtro = FiltroBloom(numBits = 65536, numHashes = 5)
        val contrasenas = listOf("123456", "password", "admin123", "dragon", "qwerty", "master2026")

        contrasenas.forEach { filtro.insertar(it) }

        contrasenas.forEach { clave ->
            assertTrue("Debe contener '$clave'", filtro.contiene(clave))
            assertTrue("Debe ser insensible a mayusculas '$clave'", filtro.contiene(clave.uppercase()))
        }
    }

    @Test
    fun `elementos aleatorios no insertados dan negativo`() {
        val filtro = FiltroBloom(numBits = 65536, numHashes = 5)
        filtro.insertar("password123")
        filtro.insertar("qwerty890")

        assertFalse(filtro.contiene("X9#vL2!kP7_zQ1wR_ultra_segura_2026"))
        assertFalse(filtro.contiene("mi_clave_super_secreta_9999"))
    }

    @Test
    fun `serializacion y deserializacion de bytes preserva los bits del filtro`() {
        val filtroOriginal = FiltroBloom(numBits = 8192, numHashes = 4)
        filtroOriginal.insertar("boveda_secreta")
        filtroOriginal.insertar("prueba123")

        val bytes = filtroOriginal.aByteArray()
        val filtroRestaurado = FiltroBloom.desdeBytes(bytes, numBits = 8192, numHashes = 4)

        assertTrue(filtroRestaurado.contiene("boveda_secreta"))
        assertTrue(filtroRestaurado.contiene("prueba123"))
        assertFalse(filtroRestaurado.contiene("otra_desconocida"))
    }

    @Test
    fun `generar archivo de filtro binario para assets si no existe`() {
        val assetFile = File("src/main/assets/filtro_bloom_filtradas.bin")
        val baseTxt = File("src/main/assets/contrasenas_comunes.txt")

        val filtro = FiltroBloom(numBits = 524288, numHashes = 5) // 64 KB
        if (baseTxt.exists()) {
            baseTxt.readLines().forEach { linea ->
                val limpia = linea.trim()
                if (limpia.isNotBlank()) filtro.insertar(limpia)
            }
        }
        // Incluir variaciones populares
        listOf(
            "123456", "password", "12345678", "qwerty", "123456789", "12345", "1234", "111111",
            "admin", "welcome", "login", "iloveyou", "princess", "football", "monkey", "charlie",
            "donald", "sunshine", "shadow", "master", "superman", "batman", "trustno1", "starwars"
        ).forEach { filtro.insertar(it) }

        val bytes = filtro.aByteArray()
        assetFile.parentFile?.mkdirs()
        assetFile.writeBytes(bytes)

        assertTrue(assetFile.exists())
        assertTrue(assetFile.length() > 0)
    }
}
