/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.FiltroSintaxisBusqueda
import org.junit.Assert.assertEquals
import org.junit.Test

class FiltroSintaxisBusquedaTest {

    private val entradas = listOf(
        Entrada(id = "1", titulo = "GitHub", usuario = "dev", tipo = TipoEntrada.LOGIN, secretoTotp = "JBSWY3DPEHPK3PXP", etiquetas = listOf("trabajo")),
        Entrada(id = "2", titulo = "Router Casa", tipo = TipoEntrada.WIFI, notas = "Clave WPA3"),
        Entrada(id = "3", titulo = "Banco Santander", usuario = "titular", tipo = TipoEntrada.CUENTA_BANCARIA, favorito = true),
        Entrada(id = "4", titulo = "GitLab", usuario = "corp", tipo = TipoEntrada.LOGIN, etiquetas = listOf("trabajo", "devops"))
    )

    @Test
    fun testFiltroPorTipo() {
        val wifi = FiltroSintaxisBusqueda.filtrar(entradas, "t:wifi")
        assertEquals(1, wifi.size)
        assertEquals("Router Casa", wifi.first().titulo)
    }

    @Test
    fun testFiltroPorTag() {
        val trabajo = FiltroSintaxisBusqueda.filtrar(entradas, "tag:trabajo")
        assertEquals(2, trabajo.size)
    }

    @Test
    fun testFiltroHasTotp() {
        val totp = FiltroSintaxisBusqueda.filtrar(entradas, "has:totp")
        assertEquals(1, totp.size)
        assertEquals("GitHub", totp.first().titulo)
    }

    @Test
    fun testFiltroCombinado() {
        val combinada = FiltroSintaxisBusqueda.filtrar(entradas, "t:login tag:devops Git")
        assertEquals(1, combinada.size)
        assertEquals("GitLab", combinada.first().titulo)
    }
}
