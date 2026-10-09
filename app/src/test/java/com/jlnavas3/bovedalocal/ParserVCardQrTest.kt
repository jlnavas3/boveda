package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.ParserVCardQr
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParserVCardQrTest {

    @Test
    fun `esVCard identifica correctamente cadenas vCard`() {
        assertTrue(ParserVCardQr.esVCard("BEGIN:VCARD\nVERSION:3.0\nFN:Juan Perez\nEND:VCARD"))
        assertTrue(ParserVCardQr.esVCard("BEGIN:vcard\nVERSION:2.1\nFN:Maria\nEND:VCARD"))
        assertTrue(ParserVCardQr.esVCard("version:3.0\nfn:Carlos"))
        assertFalse(ParserVCardQr.esVCard("otpauth://totp/Test?secret=ABC"))
        assertFalse(ParserVCardQr.esVCard("WIFI:T:WPA;S:MiWifi;P:1234;;"))
        assertFalse(ParserVCardQr.esVCard("Texto plano cualquiera"))
    }

    @Test
    fun `parsear vCard con FN y telefono crea Entrada CONTACTO`() {
        val vcard = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Carlos Gómez
            TEL;TYPE=CELL:+34600112233
            EMAIL;TYPE=WORK:carlos@empresa.com
            ORG:Empresa Tech
            TITLE:Director de IT
            ADR;TYPE=WORK:;;Calle Mayor 10;Madrid;;28001;Espana
            NOTE:Contacto de confianza
            END:VCARD
        """.trimIndent()

        val entrada = ParserVCardQr.parsear(vcard)
        assertNotNull(entrada)
        assertEquals(TipoEntrada.CONTACTO, entrada!!.tipo)
        assertEquals("Carlos Gómez", entrada.titulo)
        assertEquals("+34600112233", entrada.usuario)
        assertEquals("Contacto de confianza", entrada.notas)

        // Verificar campos personalizados
        val emailCampo = entrada.camposPersonalizados.firstOrNull { it.etiqueta == "Correo electrónico" }
        assertNotNull(emailCampo)
        assertEquals("carlos@empresa.com", emailCampo!!.valor)

        val orgCampo = entrada.camposPersonalizados.firstOrNull { it.etiqueta == "Empresa / Organización" }
        assertNotNull(orgCampo)
        assertEquals("Empresa Tech", orgCampo!!.valor)

        val cargoCampo = entrada.camposPersonalizados.firstOrNull { it.etiqueta == "Cargo / Puesto" }
        assertNotNull(cargoCampo)
        assertEquals("Director de IT", cargoCampo!!.valor)
    }

    @Test
    fun `parsear vCard solo con N descompone nombre si FN no existe`() {
        val vcard = """
            BEGIN:VCARD
            VERSION:2.1
            N:Perez;Juan
            TEL:123456789
            END:VCARD
        """.trimIndent()

        val entrada = ParserVCardQr.parsear(vcard)
        assertNotNull(entrada)
        assertEquals("Juan Perez", entrada!!.titulo)
        assertEquals("123456789", entrada.usuario)
    }

    @Test
    fun `parsear texto invalido devuelve null`() {
        assertNull(ParserVCardQr.parsear("WIFI:T:WPA;S:MiWifi;P:pass;;"))
        assertNull(ParserVCardQr.parsear(""))
    }
}
