package com.jlnavas3.bovedalocal.cxf

import org.junit.Assert.assertEquals
import org.junit.Test

class CxfNormalizadorUrlTest {

    @Test
    fun urlConEsquemaHttpsPermaneceIgual() {
        val url = "https://github.com/login"
        assertEquals(url, CxfNormalizadorUrl.normalizar(url))
    }

    @Test
    fun urlConEsquemaHttpPermaneceIgual() {
        val url = "http://intranet.local"
        assertEquals(url, CxfNormalizadorUrl.normalizar(url))
    }

    @Test
    fun dominioSimpleRecibeEsquemaHttps() {
        val dominio = "account.termius.com"
        assertEquals("https://account.termius.com", CxfNormalizadorUrl.normalizar(dominio))
    }

    @Test
    fun direccionIpRecibeEsquemaHttp() {
        val ip = "192.168.1.1"
        assertEquals("http://192.168.1.1", CxfNormalizadorUrl.normalizar(ip))
    }

    @Test
    fun cadenaVaciaDevuelveVacio() {
        assertEquals("", CxfNormalizadorUrl.normalizar("   "))
    }
}
