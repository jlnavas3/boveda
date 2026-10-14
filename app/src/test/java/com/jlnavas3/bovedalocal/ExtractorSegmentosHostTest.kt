/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.util.ExtractorSegmentosHost
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtractorSegmentosHostTest {

    @Test
    fun `extrae segmentos con multiples subdominios`() {
        assertEquals(
            listOf("Pornhub", "App"),
            ExtractorSegmentosHost.extraer("https://pornhub.app.com/login", listOf("app", "com"))
        )

        assertEquals(
            listOf("Piccolericette", "Alternativeindustries"),
            ExtractorSegmentosHost.extraer("piccolericette.alternativeindustries.com")
        )

        assertEquals(
            listOf("Api-Dashboard", "Search", "Brave"),
            ExtractorSegmentosHost.extraer("https://api-dashboard.search.brave.com")
        )
    }

    @Test
    fun `extrae segmento unico para dominios simples`() {
        assertEquals(
            listOf("Google"),
            ExtractorSegmentosHost.extraer("https://www.google.com")
        )
        assertEquals(
            listOf("Github"),
            ExtractorSegmentosHost.extraer("github.com")
        )
    }

    @Test
    fun `ignora ips y textos invalidos`() {
        assertEquals(
            emptyList<String>(),
            ExtractorSegmentosHost.extraer("192.168.1.1:8080")
        )
        assertEquals(
            emptyList<String>(),
            ExtractorSegmentosHost.extraer("")
        )
        assertEquals(
            emptyList<String>(),
            ExtractorSegmentosHost.extraer("MiServicioSinPunto")
        )
    }
}
