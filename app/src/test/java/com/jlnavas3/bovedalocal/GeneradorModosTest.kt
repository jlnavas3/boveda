package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PLANTILLAS_PATRON_RAPIDO
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GeneradorModosTest {

    @Test
    fun plantillasPatronRapido_contienePatronesEsperados() {
        val nombres = PLANTILLAS_PATRON_RAPIDO.map { it.first }
        assertTrue(nombres.contains("Clave Windows"))
        assertTrue(nombres.contains("PIN 4 dígitos"))
        assertTrue(nombres.contains("PIN 6 dígitos"))
        assertTrue(nombres.contains("Token 16"))
        assertTrue(nombres.contains("Frase + Dígitos"))

        val pin4 = PLANTILLAS_PATRON_RAPIDO.first { it.first == "PIN 4 dígitos" }.second
        assertEquals("9999", pin4)
    }

    @Test
    fun generador_modoAleatorio_respetaLongitud() {
        val opciones = OpcionesGenerador(longitud = 24, mayusculas = true, minusculas = true, digitos = true, simbolos = true)
        val password = PasswordGenerator.generar(opciones)
        assertEquals(24, password.length)
    }

    @Test
    fun generador_modoDiceware_respetaCantidadPalabras() {
        val opciones = OpcionesGenerador(modoFrase = true, palabras = 5, separadorFrase = "-")
        val password = PasswordGenerator.generar(opciones)
        val palabras = password.split("-")
        assertEquals(5, palabras.size)
    }

    @Test
    fun generador_modoPatron_pin4Digitos() {
        val opciones = OpcionesGenerador(modoPatron = true, patron = "9999")
        val pin = PasswordGenerator.generar(opciones)
        assertEquals(4, pin.length)
        assertTrue(pin.all { it.isDigit() })
    }

    @Test
    fun entropiaBits_calculaEntropiaPositiva() {
        val bitsAleatoria = PasswordGenerator.entropiaBits(OpcionesGenerador(longitud = 16))
        assertTrue(bitsAleatoria > 50.0)

        val bitsDiceware = PasswordGenerator.entropiaBits(OpcionesGenerador(modoFrase = true, palabras = 6))
        assertTrue(bitsDiceware > 60.0)
    }

    @Test
    fun tiempoDeCrackeo_produceFormatoLegible() {
        val tiempo = PasswordGenerator.tiempoDeCrackeo(80.0)
        assertNotNull(tiempo)
        assertTrue(tiempo.isNotEmpty())
    }
}
