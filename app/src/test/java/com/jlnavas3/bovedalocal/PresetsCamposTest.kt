package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.PresetsCampos
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PresetsCamposTest {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @Test
    fun `presets estaticos estan correctamente definidos con identificadores unicos`() {
        val presets = PresetsCampos.todos
        assertEquals(6, presets.size)

        val ids = presets.map { it.id }
        assertEquals(ids.distinct().size, ids.size)

        presets.forEach { preset ->
            assertTrue(preset.titulo.isNotBlank())
            assertTrue(preset.descripcion.isNotBlank())
            val campos = preset.generarCampos()
            assertTrue(campos.isNotEmpty())

            // Cada invocación debe generar UUIDs frescos
            val campos2 = preset.generarCampos()
            assertNotEquals(campos.first().id, campos2.first().id)
        }
    }

    @Test
    fun `invariante de seguridad - los datos sensibles tienen esSensible y esSensibleEfectivo activo`() {
        val tarjeta = PresetsCampos.tarjeta()
        val cvv = tarjeta.firstOrNull { it.etiqueta.contains("CVV") }
        assertNotNull(cvv)
        assertTrue("CVV debe ser sensible", cvv!!.esSensible)
        assertTrue("CVV debe tener esSensibleEfectivo activo", cvv.esSensibleEfectivo)
        assertEquals(TipoCampo.NUMERO, cvv.tipo)

        val pin = tarjeta.firstOrNull { it.etiqueta.contains("PIN") }
        assertNotNull(pin)
        assertTrue("PIN debe ser sensible", pin!!.esSensibleEfectivo)
        assertEquals(TipoCampo.PIN, pin.tipo)

        val wifi = PresetsCampos.wifi()
        val wifiClave = wifi.firstOrNull { it.etiqueta.contains("Contraseña") }
        assertNotNull(wifiClave)
        assertTrue("Clave Wi-Fi debe ser sensible", wifiClave!!.esSensible)
        assertEquals(TipoCampo.TEXTO, wifiClave.tipo)

        val ssid = wifi.firstOrNull { it.etiqueta.contains("SSID") }
        assertNotNull(ssid)
        assertFalse("SSID no debe ser sensible", ssid!!.esSensible)
        assertFalse("SSID no debe tener esSensibleEfectivo", ssid.esSensibleEfectivo)

        val crypto = PresetsCampos.criptoWallet()
        val seed = crypto.firstOrNull { it.etiqueta.contains("semilla") }
        assertNotNull(seed)
        assertTrue("Frase semilla debe ser sensible", seed!!.esSensible)
        assertEquals(TipoCampo.NOTAS, seed.tipo)
    }

    @Test
    fun `retrocompatibilidad - campos legacy con OCULTO deserializan y son tratados como sensibles`() {
        // Simular un JSON existente de una versión anterior que guardó tipo = OCULTO
        val jsonLegacy = """
            {
                "id": "campo-123",
                "etiqueta": "Clave Secreta",
                "valor": "super_secreto",
                "tipo": "OCULTO"
            }
        """.trimIndent()

        val deserializado = json.decodeFromString<CampoPersonalizado>(jsonLegacy)
        assertEquals("campo-123", deserializado.id)
        assertEquals("Clave Secreta", deserializado.etiqueta)
        assertEquals("super_secreto", deserializado.valor)
        @Suppress("DEPRECATION")
        assertEquals(TipoCampo.OCULTO, deserializado.tipo)
        assertTrue("Campo legacy OCULTO debe calcular esSensibleEfectivo como true", deserializado.esSensibleEfectivo)
    }

    @Test
    fun `retrocompatibilidad - entrada LOGIN clasica funciona sin campos adicionales`() {
        val jsonEntrada = """
            {
                "id": "login-999",
                "tipo": "LOGIN",
                "titulo": "Google Account",
                "usuario": "usuario@gmail.com",
                "contrasena": "SecretPassword123!"
            }
        """.trimIndent()

        val entrada = json.decodeFromString<Entrada>(jsonEntrada)
        assertEquals(TipoEntrada.LOGIN, entrada.tipo)
        assertEquals("Google Account", entrada.titulo)
        assertEquals("usuario@gmail.com", entrada.usuario)
        assertEquals("SecretPassword123!", entrada.contrasena)
        assertTrue("Campos personalizados deben ser lista vacia por defecto", entrada.camposPersonalizados.isEmpty())
    }

    @Test
    fun `todos los tipos de entrada tienen etiqueta valida`() {
        TipoEntrada.entries.forEach { tipo ->
            assertTrue(tipo.etiqueta.isNotBlank())
        }
        assertEquals("Contraseña", TipoEntrada.LOGIN.etiqueta)
        assertEquals("Nota segura", TipoEntrada.NOTA.etiqueta)
        assertEquals("Tarjeta bancaria", TipoEntrada.TARJETA.etiqueta)
        assertEquals("Red Wi-Fi", TipoEntrada.WIFI.etiqueta)
        assertEquals("Cuenta bancaria", TipoEntrada.CUENTA_BANCARIA.etiqueta)
        assertEquals("Documento de identidad", TipoEntrada.IDENTIDAD.etiqueta)
        assertEquals("Servidor / SSH", TipoEntrada.SERVIDOR.etiqueta)
        assertEquals("Cripto Wallet", TipoEntrada.WALLET.etiqueta)
        assertEquals("Passkey", TipoEntrada.PASSKEY.etiqueta)
    }

    @Test
    fun `todos los tipos de campo tienen etiqueta valida`() {
        TipoCampo.entries.forEach { tipo ->
            assertTrue(tipo.etiqueta.isNotBlank())
        }
        assertEquals("Texto", TipoCampo.TEXTO.etiqueta)
        assertEquals("Número", TipoCampo.NUMERO.etiqueta)
        assertEquals("Decimal", TipoCampo.DECIMAL.etiqueta)
        assertEquals("PIN", TipoCampo.PIN.etiqueta)
        assertEquals("Email", TipoCampo.EMAIL.etiqueta)
        assertEquals("URL", TipoCampo.URL.etiqueta)
        assertEquals("Teléfono", TipoCampo.TELEFONO.etiqueta)
        assertEquals("Fecha", TipoCampo.FECHA.etiqueta)
        assertEquals("Hora", TipoCampo.HORA.etiqueta)
        assertEquals("Lista", TipoCampo.LISTA.etiqueta)
        assertEquals("Notas", TipoCampo.NOTAS.etiqueta)
    }
}
