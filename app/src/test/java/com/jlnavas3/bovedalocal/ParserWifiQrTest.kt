package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.ParserWifiQr
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParserWifiQrTest {

    @Test
    fun `esWifi identifica correctamente esquemas WIFI`() {
        assertTrue(ParserWifiQr.esWifi("WIFI:T:WPA;S:MiWifi;P:1234;;"))
        assertTrue(ParserWifiQr.esWifi("wifi:t:wpa;s:casa;;"))
        assertFalse(ParserWifiQr.esWifi("BEGIN:VCARD\nFN:Juan\nEND:VCARD"))
        assertFalse(ParserWifiQr.esWifi("bovedalocal://importar"))
    }

    @Test
    fun `parsear wifi estandar crea Entrada WIFI con campos correctos`() {
        val qrTexto = "WIFI:T:WPA;S:SPIDER2;P:MiClaveSecreta;;"
        val entrada = ParserWifiQr.parsear(qrTexto)

        assertNotNull(entrada)
        assertEquals(TipoEntrada.WIFI, entrada!!.tipo)
        assertEquals("SPIDER2", entrada.titulo)
        assertEquals("MiClaveSecreta", entrada.contrasena)

        val campoSsid = entrada.camposPersonalizados.firstOrNull { it.etiqueta == "Nombre de red (SSID)" }
        assertNotNull(campoSsid)
        assertEquals("SPIDER2", campoSsid!!.valor)

        val campoPass = entrada.camposPersonalizados.firstOrNull { it.etiqueta == "Contraseña Wi-Fi" }
        assertNotNull(campoPass)
        assertEquals("MiClaveSecreta", campoPass!!.valor)
        assertTrue(campoPass.esSensible)

        val campoTipo = entrada.camposPersonalizados.firstOrNull { it.etiqueta == "Tipo de seguridad" }
        assertNotNull(campoTipo)
        assertEquals("WPA", campoTipo!!.valor)
    }

    @Test
    fun `parsear wifi con caracteres escapados respeta barras invertidas y separadores`() {
        val qrTexto = "WIFI:T:WPA;S:Mi\\;Red\\:Especial;P:P@ss\\;123\\\\;;H:true;;"
        val entrada = ParserWifiQr.parsear(qrTexto)

        assertNotNull(entrada)
        assertEquals("Mi;Red:Especial", entrada!!.titulo)
        assertEquals("P@ss;123\\", entrada.contrasena)

        val campoOculta = entrada.camposPersonalizados.firstOrNull { it.etiqueta == "Red oculta" }
        assertNotNull(campoOculta)
        assertEquals("true", campoOculta!!.valor)
    }

    @Test
    fun `parsear texto invalido o vacio devuelve null`() {
        assertNull(ParserWifiQr.parsear("WIFI:;;"))
        assertNull(ParserWifiQr.parsear("Texto normal"))
    }
}
