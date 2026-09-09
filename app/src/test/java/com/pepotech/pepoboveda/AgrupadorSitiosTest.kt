package com.pepotech.pepoboveda

import com.pepotech.pepoboveda.data.Entrada
import com.pepotech.pepoboveda.util.ItemAgrupado
import com.pepotech.pepoboveda.util.construirItemsAgrupadosPorSitio
import org.junit.Assert.assertEquals
import org.junit.Test

class AgrupadorSitiosTest {

    @Test
    fun `las urls android usan el titulo importado y no el paquete com`() {
        val entradas = listOf(
            Entrada("1", titulo = "brakepak.com", urls = listOf("android://token@com.brakepak/")),
            Entrada("2", titulo = "gaia.com", urls = listOf("android://token@com.gaiamtv/")),
            Entrada("3", titulo = "mercadolibre.com", urls = listOf("android://token@com.mercadolibre/")),
            Entrada("4", titulo = "waze.com", urls = listOf("android://token@com.waze/"))
        )

        val items = construirItemsAgrupadosPorSitio(entradas) { false }

        assertEquals(entradas.size, items.size)
        assertEquals(true, items.all { it is ItemAgrupado.Suelto })
    }
}