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

    @Test
    fun `textoWifiDesdeEntrada genera formato estandar valido para el caso del usuario`() {
        val entrada = Entrada(
            id = "wifi-spider",
            tipo = com.jlnavas3.bovedalocal.data.TipoEntrada.WIFI,
            titulo = "SPIDER2",
            camposPersonalizados = listOf(
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(
                    etiqueta = "Nombre de red (SSID)",
                    valor = "SPIDER2"
                ),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(
                    etiqueta = "Contraseña Wi-Fi",
                    valor = "La contraseña",
                    esSensible = true
                ),
                com.jlnavas3.bovedalocal.data.CampoPersonalizado(
                    etiqueta = "Tipo de seguridad",
                    valor = "WPA2-PSK [AES]"
                )
            )
        )

        val qrTexto = GeneradorQr.textoWifiDesdeEntrada(entrada)
        assertEquals("WIFI:T:WPA;S:SPIDER2;P:La contraseña;;", qrTexto)

        // Verificamos que el parser de ZXing (el mismo de Android) lo interprete correctamente
        val result = com.google.zxing.Result(qrTexto, null, null, com.google.zxing.BarcodeFormat.QR_CODE)
        val parsed = com.google.zxing.client.result.WifiResultParser().parse(result)
        assertNotNull(parsed)
        assertEquals("SPIDER2", parsed.ssid)
        assertEquals("La contraseña", parsed.password)
        assertEquals("WPA", parsed.networkEncryption)
    }

    @Test
    fun `textoConfiguracionWifi maneja caracteres especiales con escape`() {
        val qrTexto = GeneradorQr.textoConfiguracionWifi(
            ssid = "Mi;Red:WiFi",
            clave = "P@ss;w:ord\\123",
            tipoSeguridad = "WPA2"
        )
        assertEquals("WIFI:T:WPA;S:Mi\\;Red\\:WiFi;P:P@ss\\;w\\:ord\\\\123;;", qrTexto)

        val result = com.google.zxing.Result(qrTexto, null, null, com.google.zxing.BarcodeFormat.QR_CODE)
        val parsed = com.google.zxing.client.result.WifiResultParser().parse(result)
        assertNotNull(parsed)
        assertEquals("Mi;Red:WiFi", parsed.ssid)
        assertEquals("P@ss;w:ord\\123", parsed.password)
    }

    @Test
    fun `textoConfiguracionWifi para red abierta omite contrasena y usa nopass`() {
        val qrTexto = GeneradorQr.textoConfiguracionWifi(
            ssid = "RedLibre",
            clave = "",
            tipoSeguridad = "Abierta"
        )
        assertEquals("WIFI:T:nopass;S:RedLibre;;", qrTexto)

        val result = com.google.zxing.Result(qrTexto, null, null, com.google.zxing.BarcodeFormat.QR_CODE)
        val parsed = com.google.zxing.client.result.WifiResultParser().parse(result)
        assertNotNull(parsed)
        assertEquals("RedLibre", parsed.ssid)
        assertEquals("nopass", parsed.networkEncryption)
        assertNull(parsed.password)
    }
}
