package com.jlnavas3.bovedalocal.cxf

import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Test

class CxfFiltradorEntradasExportacionTest {

    private fun crearEntrada(id: String, titulo: String): Entrada {
        return Entrada(
            id = id,
            titulo = titulo,
            usuario = "user_$id",
            contrasena = "pass_$id"
        )
    }

    @Test
    fun filtrar_conSeleccionPersonalizada_devuelveSoloIdsSeleccionadas() {
        val e1 = crearEntrada("id-1", "Google")
        val e2 = crearEntrada("id-2", "GitHub")
        val e3 = crearEntrada("id-3", "Amazon")
        val todas = listOf(e1, e2, e3)

        val resultado = CxfFiltradorEntradasExportacion.filtrar(
            entradasDisponibles = todas,
            idsPreseleccionadas = setOf("id-2"),
            esSeleccionPersonalizada = true
        )

        assertEquals(1, resultado.size)
        assertEquals("id-2", resultado.first().id)
    }

    @Test
    fun filtrar_conSeleccionCompleta_devuelveTodas() {
        val e1 = crearEntrada("id-1", "Google")
        val e2 = crearEntrada("id-2", "GitHub")
        val todas = listOf(e1, e2)

        val resultado = CxfFiltradorEntradasExportacion.filtrar(
            entradasDisponibles = todas,
            idsPreseleccionadas = setOf("id-1"),
            esSeleccionPersonalizada = false
        )

        assertEquals(2, resultado.size)
    }

    @Test
    fun filtrar_conIdsVacias_devuelveTodas() {
        val e1 = crearEntrada("id-1", "Google")
        val todas = listOf(e1)

        val resultado = CxfFiltradorEntradasExportacion.filtrar(
            entradasDisponibles = todas,
            idsPreseleccionadas = emptySet(),
            esSeleccionPersonalizada = true
        )

        assertEquals(1, resultado.size)
    }
}
