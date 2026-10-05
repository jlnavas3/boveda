package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.util.Dominios
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DominiosTest {

    @Test
    fun `extrae el host de una url completa`() {
        assertEquals("ejemplo.com", Dominios.host("https://www.ejemplo.com/entrar?x=1#a"))
        assertEquals("ejemplo.com", Dominios.host("EJEMPLO.COM"))
        assertEquals("ejemplo.com", Dominios.host("https://usuario:clave@ejemplo.com:8443/ruta"))
    }

    @Test
    fun `calcula el dominio raiz`() {
        assertEquals("ejemplo.com", Dominios.raiz("https://cuentas.ejemplo.com/login"))
        assertEquals("ejemplo.co.uk", Dominios.raiz("https://login.ejemplo.co.uk"))
        assertEquals("ejemplo.com.ar", Dominios.raiz("tienda.ejemplo.com.ar"))
        assertEquals("cedia.edu.ec", Dominios.raiz("https://login.cedia.edu.ec"))
        assertEquals("ejemplo.co.za", Dominios.raiz("https://login.ejemplo.co.za"))
        assertEquals("ejemplo.com.au", Dominios.raiz("https://login.ejemplo.com.au"))
    }

    @Test
    fun `calcula la marca para agrupar dominios con distinto tld`() {
        assertEquals("amazon", Dominios.marca("https://www.amazon.com"))
        assertEquals("amazon", Dominios.marca("https://amazon.com.mx"))
        assertEquals("amazon", Dominios.marca("https://login.amazon.co.uk"))
        assertEquals("amazonaws", Dominios.marca("https://console.amazonaws.com"))
        assertEquals("cedia", Dominios.marca("cedia.edu.ec"))
        assertEquals("utpl", Dominios.marca("utpl.edu.ec"))
        assertEquals("ejemplo", Dominios.marca("https://login.ejemplo.co.za"))
    }

    @Test
    fun `coincide entre subdominios del mismo sitio`() {
        assertTrue(Dominios.coincide("ejemplo.com", "cuentas.ejemplo.com"))
        assertTrue(Dominios.coincide("https://ejemplo.com/entrar", "ejemplo.com"))
    }

    @Test
    fun `no coincide con dominios que solo se parecen`() {
        assertFalse(Dominios.coincide("ejemplo.com", "ejemplo.com.evil.net"))
        assertFalse(Dominios.coincide("ejemplo.com", "ejemplo-com.net"))
        assertFalse(Dominios.coincide("ejemplo.com", "miejemplo.com"))
        assertFalse(Dominios.coincide("banco.es", "banco.es.phishing.com"))
        assertFalse(Dominios.coincide("", "ejemplo.com"))
        assertFalse(Dominios.coincide("ejemplo.com", ""))
    }

    @Test
    fun `coincide entre paquetes android y esquemas android`() {
        assertTrue(Dominios.coincide("android://com.dvdb.bergnotes", "com.dvdb.bergnotes"))
        assertTrue(Dominios.coincide("com.dvdb.bergnotes", "android://com.dvdb.bergnotes"))
        assertTrue(Dominios.coincide("android://hash@com.dvdb.bergnotes/", "com.dvdb.bergnotes"))
        assertFalse(Dominios.coincide("android://com.otro.app", "com.dvdb.bergnotes"))
    }

    @Test
    fun `deriva un dominio desde el paquete de la app`() {
        assertEquals("ejemplo.com", Dominios.dominioDePaquete("com.ejemplo.android"))
        assertEquals("banco.es", Dominios.dominioDePaquete("es.banco"))
    }

    @Test
    fun `coincide entre paquetes populares de apps y dominios web`() {
        assertTrue(Dominios.coincide("https://facebook.com", "com.facebook.katana"))
        assertTrue(Dominios.coincide("facebook.com", "com.facebook.katana"))
        assertTrue(Dominios.coincide("com.facebook.katana", "https://facebook.com"))
        assertTrue(Dominios.coincide("https://instagram.com", "com.instagram.android"))
        assertTrue(Dominios.coincide("https://x.com", "com.twitter.android"))
        assertTrue(Dominios.coincide("https://spotify.com", "com.spotify.music"))
        assertTrue(Dominios.coincide("https://tiktok.com", "com.zhiliaoapp.musically"))
        assertFalse(Dominios.coincide("https://google.com", "com.facebook.katana"))
    }
}
