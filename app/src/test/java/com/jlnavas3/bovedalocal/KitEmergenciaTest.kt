package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.GeneradorKitEmergencia
import com.jlnavas3.bovedalocal.util.GeneradorKitEmergencia.OpcionesKit
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KitEmergenciaTest {

    private val entradas = listOf(
        Entrada(
            id = "1",
            titulo = "Banco Santander",
            usuario = "es12345",
            contrasena = "ClaveSecreta9!",
            favorito = true
        ),
        Entrada(
            id = "2",
            titulo = "Netflix",
            usuario = "familia@example.com",
            contrasena = "Peliculas2026",
            favorito = false
        )
    )

    @Test
    fun `generarTexto respeta opcion de ocultar contrasenas`() {
        val texto = GeneradorKitEmergencia.generarTexto(entradas, OpcionesKit(incluirContrasenas = false))
        assertTrue(texto.contains("Banco Santander"))
        assertTrue(texto.contains("Netflix"))
        assertFalse(texto.contains("ClaveSecreta9!"))
        assertFalse(texto.contains("Peliculas2026"))
        assertTrue(texto.contains("ANOTAR MANUALMENTE"))
    }

    @Test
    fun `generarTexto incluye contrasenas cuando se solicita`() {
        val texto = GeneradorKitEmergencia.generarTexto(entradas, OpcionesKit(incluirContrasenas = true))
        assertTrue(texto.contains("ClaveSecreta9!"))
        assertTrue(texto.contains("Peliculas2026"))
    }

    @Test
    fun `filtrarEntradas soloFavoritos filtra correctamente`() {
        val filtradas = GeneradorKitEmergencia.filtrarEntradas(entradas, OpcionesKit(soloFavoritos = true))
        assertTrue(filtradas.size == 1)
        assertTrue(filtradas.first().titulo == "Banco Santander")
    }

    @Test
    fun `generarHtml produce estructura valida`() {
        val html = GeneradorKitEmergencia.generarHtml(entradas, OpcionesKit())
        assertTrue(html.contains("<!DOCTYPE html>"))
        assertTrue(html.contains("Banco Santander"))
        assertTrue(html.contains("Netflix"))
    }
}
