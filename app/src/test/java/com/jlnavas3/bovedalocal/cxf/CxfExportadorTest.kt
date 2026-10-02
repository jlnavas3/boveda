package com.jlnavas3.bovedalocal.cxf

import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CxfExportadorTest {

    @Test
    fun exportarEntradaConPasskeyYContrasena() {
        val entrada = Entrada(
            id = "test-1",
            tipo = TipoEntrada.PASSKEY,
            titulo = "GitHub",
            usuario = "octocat@github.com",
            contrasena = "MiClaveSecreta123!",
            urls = listOf("https://github.com"),
            secretoTotp = "JBSWY3DPEHPK3PXP",
            totpEmisor = "GitHub",
            passkey = DatosPasskey(
                rpId = "github.com",
                rpName = "GitHub Inc",
                userHandle = "dXNlcl9oYW5kbGU=",
                credId = "Y3JlZF9pZA==",
                clavePrivada = "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAChRANCAARAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
                algoritmo = "ES256",
                usuario = "octocat@github.com"
            )
        )

        val json = CxfExportador.exportarAJson(listOf(entrada))
        assertNotNull(json)
        assertTrue(json.contains("com.jlnavas3.bovedalocal"))
        assertTrue(json.contains("octocat@github.com"))
        assertTrue(json.contains("passkey"))
        assertTrue(json.contains("basic-auth"))
        assertTrue(json.contains("totp"))
        assertTrue("Debe contener collections en account", json.contains("\"collections\""))
        assertTrue("Debe contener androidApps en scope", json.contains("\"androidApps\""))
        assertTrue("Debe contener fieldType string en minúsculas", json.contains("\"string\""))
        assertTrue("Debe contener fieldType concealed-string en minúsculas", json.contains("\"concealed-string\""))

        // Round-trip con CxfConvertidor
        val resultadoConversion = CxfConvertidor.convertir(json)
        assertEquals(1, resultadoConversion.entradas.size)

        val recuperada = resultadoConversion.entradas.first()
        assertEquals("GitHub", recuperada.titulo)
        assertEquals("octocat@github.com", recuperada.usuario)
        assertEquals("MiClaveSecreta123!", recuperada.contrasena)
        assertEquals("https://github.com", recuperada.urls.first())
        assertNotNull(recuperada.passkey)
        assertEquals("github.com", recuperada.passkey?.rpId)
        assertEquals("JBSWY3DPEHPK3PXP", recuperada.secretoTotp)
    }

    @Test
    fun exportarListaVaciaProduceDocumentoValido() {
        val json = CxfExportador.exportarAJson(emptyList())
        val resultadoConversion = CxfConvertidor.convertir(json)
        assertTrue(resultadoConversion.entradas.isEmpty())
    }

    @Test
    fun exportarNotaSegura() {
        val nota = Entrada(
            id = "nota-1",
            tipo = TipoEntrada.NOTA,
            titulo = "Código de la caja fuerte",
            notas = "1234-5678"
        )
        val json = CxfExportador.exportarAJson(listOf(nota))
        assertTrue(json.contains("note"))
        assertTrue(json.contains("1234-5678"))
    }

    @Test
    fun exportarContieneAccountsEItemsSinDuplicadosEnConvertidor() {
        val entrada1 = Entrada(id = "1", titulo = "Cuenta 1", contrasena = "pass1")
        val entrada2 = Entrada(id = "2", titulo = "Cuenta 2", contrasena = "pass2")

        val json = CxfExportador.exportarAJson(listOf(entrada1, entrada2))
        assertTrue("Debe contener bloque accounts para Google y Dashlane", json.contains("\"accounts\""))
        assertTrue("Debe contener bloque items para compatibilidad", json.contains("\"items\""))

        // Al convertir, no debe duplicar las entradas
        val resultado = CxfConvertidor.convertir(json)
        assertEquals(2, resultado.entradas.size)
        assertEquals(2, resultado.totalContrasenas)
    }

    @Test
    fun exportacionSelectivaExportaUnicamenteEntradasElegidas() {
        val todas = listOf(
            Entrada(id = "1", titulo = "Cuenta 1", contrasena = "pass1"),
            Entrada(id = "2", titulo = "Cuenta 2", contrasena = "pass2"),
            Entrada(id = "3", titulo = "Cuenta 3", contrasena = "pass3")
        )
        val seleccionadas = todas.filter { it.id == "2" }

        val json = CxfExportador.exportarAJson(seleccionadas)
        val resultado = CxfConvertidor.convertir(json)

        assertEquals(1, resultado.entradas.size)
        assertEquals("Cuenta 2", resultado.entradas.first().titulo)
    }
}
