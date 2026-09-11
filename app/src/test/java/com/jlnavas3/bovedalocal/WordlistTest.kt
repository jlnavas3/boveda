package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.crypto.Wordlist
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WordlistTest {

    @Test
    fun `la lista contiene un catalogo extenso de palabras`() {
        assertTrue(Wordlist.PALABRAS.size >= 1000)
    }

    @Test
    fun `no contiene palabras duplicadas`() {
        val total = Wordlist.PALABRAS.size
        val unicas = Wordlist.PALABRAS.toSet().size
        assertEquals("Existen palabras duplicadas en el catálogo Diceware", total, unicas)
    }

    @Test
    fun `todas las palabras estan en minusculas y limpias de espacios`() {
        Wordlist.PALABRAS.forEach { palabra ->
            assertEquals(palabra.lowercase(), palabra)
            assertFalse(palabra.contains(" "))
            assertFalse(palabra.contains("\t"))
            assertFalse(palabra.contains("\n"))
            assertTrue(palabra.length in 2..20)
        }
    }
}
