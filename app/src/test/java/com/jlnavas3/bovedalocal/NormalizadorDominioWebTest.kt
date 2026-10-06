package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.util.NormalizadorDominioWeb
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NormalizadorDominioWebTest {

    @Test
    fun normalizar_subdominioMovil_extraeDominioRaizYCandidatos() {
        val resultado = NormalizadorDominioWeb.normalizar("https://m.facebook.com/login")
        assertNotNull(resultado)
        assertEquals("m.facebook.com", resultado!!.hostOriginal)
        assertEquals("facebook.com", resultado.dominioRaiz)
        assertTrue(resultado.hostsCandidatos.contains("m.facebook.com"))
        assertTrue(resultado.hostsCandidatos.contains("facebook.com"))
        assertTrue(resultado.hostsCandidatos.contains("www.facebook.com"))
    }

    @Test
    fun normalizar_tldCompuesto_manejaPslCorrectamente() {
        val resultado = NormalizadorDominioWeb.normalizar("auth.mercadolibre.com.ec")
        assertNotNull(resultado)
        assertEquals("auth.mercadolibre.com.ec", resultado!!.hostOriginal)
        assertEquals("mercadolibre.com.ec", resultado.dominioRaiz)
        assertTrue(resultado.hostsCandidatos.contains("mercadolibre.com.ec"))
    }

    @Test
    fun normalizar_urlCompletaConParametros_extraeHostLimpio() {
        val resultado = NormalizadorDominioWeb.normalizar("https://instagram.com/accounts/login?source=auth")
        assertNotNull(resultado)
        assertEquals("instagram.com", resultado!!.hostOriginal)
        assertEquals("instagram.com", resultado.dominioRaiz)
        assertTrue(resultado.hostsCandidatos.contains("instagram.com"))
        assertTrue(resultado.hostsCandidatos.contains("www.instagram.com"))
    }

    @Test
    fun normalizar_cadenaVacia_retornaNull() {
        assertNull(NormalizadorDominioWeb.normalizar(""))
        assertNull(NormalizadorDominioWeb.normalizar("   "))
    }
}
