/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import org.junit.Assert.assertEquals
import org.junit.Test

class DeduplicadorUrlsTest {

    @Test
    fun `deduplica url con y sin protocolo https conservando la version completa`() {
        val urls = listOf(
            "disneyplus.disney.com",
            "com.disney.disneyplus",
            "starplus.disney.com",
            "com.disney.starplus",
            "https://www.disneyplus.com/",
            "https://starplus.disney.com",
            "https://disneyplus.disney.com"
        )

        val resultado = DeduplicadorUrls.deduplicar(urls)

        assertEquals(5, resultado.size)
        assertEquals("https://disneyplus.disney.com", resultado[0])
        assertEquals("com.disney.disneyplus", resultado[1])
        assertEquals("https://starplus.disney.com", resultado[2])
        assertEquals("com.disney.starplus", resultado[3])
        assertEquals("https://www.disneyplus.com/", resultado[4])
    }

    @Test
    fun `deduplica paquetes de android con y sin esquema`() {
        val urls = listOf(
            "com.whatsapp",
            "android://k2RotK_2DKoTYT4KW2ocyHl7sgK5FEQ3q7U1hLZVJpt72L_GaODrjKuyDeerPI0bHDKSQK-5vs5HyDO3ul7LDw==@com.whatsapp/"
        )

        val resultado = DeduplicadorUrls.deduplicar(urls)

        assertEquals(1, resultado.size)
        assertEquals("android://k2RotK_2DKoTYT4KW2ocyHl7sgK5FEQ3q7U1hLZVJpt72L_GaODrjKuyDeerPI0bHDKSQK-5vs5HyDO3ul7LDw==@com.whatsapp/", resultado[0])
    }

    @Test
    fun `deduplica url con y sin barra inclinada final`() {
        val urls = listOf(
            "https://ejemplo.com/",
            "https://ejemplo.com"
        )

        val resultado = DeduplicadorUrls.deduplicar(urls)
        assertEquals(1, resultado.size)
    }

    @Test
    fun `conserva rutas diferentes en el mismo dominio`() {
        val urls = listOf(
            "https://google.com/finance",
            "https://google.com/maps"
        )

        val resultado = DeduplicadorUrls.deduplicar(urls)
        assertEquals(2, resultado.size)
    }

    @Test
    fun `normaliza correctamente dominios con tld modernos como infinitestudio punto art`() {
        val r1 = NormalizadorTitulosSitios.extraerNombreBase(
            urlODominio = "https://www.infinitestudio.art/",
            rawTitulo = "www.infinitestudio.art"
        )
        val r2 = NormalizadorTitulosSitios.extraerNombreBase(
            urlODominio = "https://infinitestudio.art",
            rawTitulo = "infinitestudio.art"
        )
        val r3 = NormalizadorTitulosSitios.extraerNombreBase(
            urlODominio = "",
            rawTitulo = "www.infinitestudio.art"
        )
        val r4 = NormalizadorTitulosSitios.extraerNombreBase(
            urlODominio = "",
            rawTitulo = "infinitestudio.art"
        )

        assertEquals("Infinitestudio", r1)
        assertEquals("Infinitestudio", r2)
        assertEquals("Infinitestudio", r3)
        assertEquals("Infinitestudio", r4)
    }

    @Test
    fun `no confunde urls que empiezan con www con paquetes android`() {
        assertEquals(false, LanzadorEnlaces.esNombrePaquete("www.infinitestudio.art"))
        assertEquals(false, LanzadorEnlaces.esNombrePaquete("www.google.com"))
        assertEquals(true, LanzadorEnlaces.esNombrePaquete("com.brakefield.painter"))
    }
}
