package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.autofill.AutofillUtiles
import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AutofillUtilesTest {

    @Test
    fun `contextoSolicitante prioriza el dominio web sobre el paquete de app`() {
        val contextoWeb = AutofillUtiles.contextoSolicitante("com.android.chrome", "https://login.banco.es/auth")
        assertEquals("banco.es", contextoWeb)

        val contextoSubdominio = AutofillUtiles.contextoSolicitante("com.android.chrome", "cuentas.google.com")
        assertEquals("google.com", contextoSubdominio)
    }

    @Test
    fun `contextoSolicitante usa el paquete cuando no hay dominio web`() {
        val contextoApp = AutofillUtiles.contextoSolicitante("com.spotify.music", null)
        assertEquals("com.spotify.music", contextoApp)

        val contextoVacio = AutofillUtiles.contextoSolicitante("com.twitter.android", "")
        assertEquals("com.twitter.android", contextoVacio)
    }

    @Test
    fun `entradasCompatibles filtra por dominio web coincidente`() {
        val e1 = Entrada("1", titulo = "Google", contrasena = "clave1", urls = listOf("https://accounts.google.com"))
        val e2 = Entrada("2", titulo = "GitHub", contrasena = "clave2", urls = listOf("https://github.com"))
        val e3 = Entrada("3", titulo = "Google Sin Clave", contrasena = "", urls = listOf("https://google.com"))

        val compatibles = AutofillUtiles.entradasCompatibles(listOf(e1, e2, e3), "com.chrome", "https://mail.google.com")
        assertEquals(1, compatibles.size)
        assertEquals("Google", compatibles.first().titulo)
    }

    @Test
    fun `entradasCompatibles filtra por paquete android guardado`() {
        val e1 = Entrada("1", titulo = "MercadoLibre", contrasena = "passML", urls = listOf("android://hash@com.mercadolibre/"))
        val e2 = Entrada("2", titulo = "Amazon", contrasena = "passAmz", urls = listOf("https://amazon.com"))

        val compatibles = AutofillUtiles.entradasCompatibles(listOf(e1, e2), "com.mercadolibre", null)
        assertEquals(1, compatibles.size)
        assertEquals("MercadoLibre", compatibles.first().titulo)
    }

    @Test
    fun `entradasCompatibles descarta entradas sin contrasena aunque coincida el sitio`() {
        val eSinClave = Entrada("1", titulo = "Twitter", contrasena = "", urls = listOf("https://twitter.com"))
        val compatibles = AutofillUtiles.entradasCompatibles(listOf(eSinClave), "com.chrome", "twitter.com")
        assertTrue(compatibles.isEmpty())
    }
}
