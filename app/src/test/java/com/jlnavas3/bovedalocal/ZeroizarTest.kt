package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.crypto.Zeroizar
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ZeroizarTest {

    @Test
    fun `borrar sobreescribe todos los bytes con cero`() {
        val datos = byteArrayOf(1, 2, 3, 4, 5, 255.toByte(), 127)
        Zeroizar.borrar(datos)
        assertTrue(datos.all { it == 0.toByte() })
    }

    @Test
    fun `borrar sobreescribe todos los caracteres con caracter nulo`() {
        val clave = "MiPasswordSecreto123!".toCharArray()
        Zeroizar.borrar(clave)
        assertTrue(clave.all { it == '\u0000' })
    }

    @Test
    fun `borrar tolera parametros nulos sin lanzar excepcion`() {
        val bytesNulos: ByteArray? = null
        val charsNulos: CharArray? = null
        Zeroizar.borrar(bytesNulos)
        Zeroizar.borrar(charsNulos)
    }

    @Test
    fun `aBytes convierte caracteres a UTF-8 correctamente`() {
        val texto = "Contraseña123€"
        val chars = texto.toCharArray()
        val bytes = Zeroizar.aBytes(chars)

        assertEquals(texto, String(bytes, Charsets.UTF_8))
        assertArrayEquals(texto.toByteArray(Charsets.UTF_8), bytes)
    }
}
