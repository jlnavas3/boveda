/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda.resolverSugerenciasBusquedaInteligentes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResolverSugerenciasBusquedaInteligentesTest {

    private val entradasBasicas = listOf(
        Entrada(id = "1", titulo = "GitHub", tipo = TipoEntrada.LOGIN, secretoTotp = "JBSWY3DPEHPK3PXP", favorito = true),
        Entrada(id = "2", titulo = "Tarjeta Débito", tipo = TipoEntrada.TARJETA)
    )

    @Test
    fun noSugiereWifiNiServidorSiNoExistenEnBoveda() {
        val sugerencias = resolverSugerenciasBusquedaInteligentes(
            entradas = entradasBasicas,
            categorias = emptyList(),
            etiquetasDisponibles = emptyList(),
            textoBusqueda = ""
        )

        val comandos = sugerencias.map { it.comando }
        assertTrue(comandos.contains("t:login"))
        assertTrue(comandos.contains("t:tarjeta"))
        assertTrue(comandos.contains("has:totp"))
        assertTrue(comandos.contains("is:fav"))

        // NO deben existir porque no hay datos de esos tipos
        assertFalse(comandos.contains("t:wifi"))
        assertFalse(comandos.contains("t:servidor"))
        assertFalse(comandos.contains("t:banco"))
        assertFalse(comandos.contains("cat:"))
        assertFalse(comandos.contains("tag:"))
    }

    @Test
    fun sugiereCategoriasYEtiquetasSoloSiEstanConfiguradas() {
        val categorias = listOf(Categoria(id = "c1", nombre = "Personal"))
        val etiquetas = listOf("finanzas", "trabajo")

        val sugerencias = resolverSugerenciasBusquedaInteligentes(
            entradas = entradasBasicas,
            categorias = categorias,
            etiquetasDisponibles = etiquetas,
            textoBusqueda = ""
        )

        val comandos = sugerencias.map { it.comando }
        assertTrue(comandos.contains("cat:"))
        assertTrue(comandos.contains("tag:"))
    }

    @Test
    fun autocompletaNombresDeCategoriasAlEscribirPrefijo() {
        val categorias = listOf(
            Categoria(id = "c1", nombre = "Personal"),
            Categoria(id = "c2", nombre = "Finanzas")
        )

        val sugerencias = resolverSugerenciasBusquedaInteligentes(
            entradas = entradasBasicas,
            categorias = categorias,
            etiquetasDisponibles = emptyList(),
            textoBusqueda = "cat:Fin"
        )

        assertEquals(1, sugerencias.size)
        assertEquals("cat:\"Finanzas\"", sugerencias.first().comando)
    }

    @Test
    fun filtraPorTerminoParcial() {
        val entradasConWifi = entradasBasicas + Entrada(id = "3", titulo = "Mi Wifi", tipo = TipoEntrada.WIFI)

        val sugerencias = resolverSugerenciasBusquedaInteligentes(
            entradas = entradasConWifi,
            categorias = emptyList(),
            etiquetasDisponibles = emptyList(),
            textoBusqueda = "wi"
        )

        assertEquals(1, sugerencias.size)
        assertEquals("t:wifi", sugerencias.first().comando)
    }
}
