package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import org.junit.Assert.assertEquals
import org.junit.Test

class RenombrarEntradasTest {

    private fun renombrarEntradas(entradas: List<Entrada>, ids: Set<String>, nuevoTitulo: String, ahora: Long): List<Entrada> {
        val tituloLimpio = nuevoTitulo.trim()
        if (ids.isEmpty() || tituloLimpio.isBlank()) return entradas
        return entradas.map { ent ->
            if (ids.contains(ent.id)) ent.copy(titulo = tituloLimpio, modificadaEn = ahora)
            else ent
        }
    }

    @Test
    fun `renombrarEntradas actualiza solo el titulo y modificadaEn de las entradas seleccionadas`() {
        val e1 = Entrada(id = "1", titulo = "account.xiaomi.com", usuario = "user1", tipo = TipoEntrada.LOGIN)
        val e2 = Entrada(id = "2", titulo = "xiaomi.com", usuario = "user2", tipo = TipoEntrada.LOGIN)
        val e3 = Entrada(id = "3", titulo = "Google", usuario = "user3", tipo = TipoEntrada.LOGIN)

        val resultado = renombrarEntradas(listOf(e1, e2, e3), setOf("1", "2"), "Xiaomi", 5000L)

        val actual1 = resultado.first { it.id == "1" }
        val actual2 = resultado.first { it.id == "2" }
        val actual3 = resultado.first { it.id == "3" }

        assertEquals("Xiaomi", actual1.titulo)
        assertEquals("user1", actual1.usuario)
        assertEquals(5000L, actual1.modificadaEn)

        assertEquals("Xiaomi", actual2.titulo)
        assertEquals("user2", actual2.usuario)
        assertEquals(5000L, actual2.modificadaEn)

        assertEquals("Google", actual3.titulo)
        assertEquals("user3", actual3.usuario)
    }
}
