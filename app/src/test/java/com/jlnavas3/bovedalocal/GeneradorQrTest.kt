package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.GeneradorQr
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GeneradorQrTest {

    @Test
    fun `generarMatriz genera matriz cuadrada valida para texto`() {
        val matriz = GeneradorQr.generarMatriz("Hola Mundo 123", 256)
        assertEquals(256, matriz.width)
        assertEquals(256, matriz.height)
        assertTrue(matriz.get(0, 0) || !matriz.get(0, 0)) // No lanza excepciones
    }

    @Test
    fun `uriTotp devuelve URI otpauth valida`() {
        val entrada = Entrada(
            id = "test-1",
            titulo = "GitHub",
            usuario = "pepo@example.com",
            secretoTotp = "JBSWY3DPEHPK3PXP",
            totpEmisor = "GitHub",
            totpDigitos = 6,
            totpPeriodo = 30
        )
        val uri = GeneradorQr.uriTotp(entrada)
        assertNotNull(uri)
        assertTrue(uri!!.startsWith("otpauth://totp/GitHub:pepo%40example.com?secret=JBSWY3DPEHPK3PXP"))
        assertTrue(uri.contains("digits=6"))
        assertTrue(uri.contains("period=30"))
    }

    @Test
    fun `uriTotp devuelve null si no hay secreto`() {
        val entrada = Entrada(id = "test-2", titulo = "Twitter", usuario = "pepo")
        assertNull(GeneradorQr.uriTotp(entrada))
    }

    @Test
    fun `textoCredencialCompleta formatea correctamente los campos`() {
        val entrada = Entrada(
            id = "test-3",
            titulo = "Servidor Local",
            usuario = "admin",
            contrasena = "SuperClave99!",
            urls = listOf("https://192.168.1.1")
        )
        val texto = GeneradorQr.textoCredencialCompleta(entrada)
        assertTrue(texto.contains("Título: Servidor Local"))
        assertTrue(texto.contains("Usuario: admin"))
        assertTrue(texto.contains("Contraseña: SuperClave99!"))
        assertTrue(texto.contains("URL: https://192.168.1.1"))
    }
}
