package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.GeneradorVCard
import org.junit.Assert.assertTrue
import org.junit.Test

class GeneradorVCardTest {

    @Test
    fun `generarVCard crea formato RFC 2426 vCard 3 valido`() {
        val entrada = Entrada(
            id = "test-vcard",
            titulo = "Carlos Santana Martínez",
            usuario = "+34655443322",
            tipo = TipoEntrada.CONTACTO,
            camposPersonalizados = listOf(
                CampoPersonalizado(etiqueta = "Email", valor = "carlos@musica.org", tipo = TipoCampo.EMAIL),
                CampoPersonalizado(etiqueta = "Empresa", valor = "Orquesta Rock", tipo = TipoCampo.TEXTO),
                CampoPersonalizado(etiqueta = "Dirección", valor = "Calle Mayor 10, Madrid", tipo = TipoCampo.TEXTO)
            ),
            notas = "Guitarrista principal"
        )

        val vcard = GeneradorVCard.generarVCard(entrada)

        assertTrue(vcard.startsWith("BEGIN:VCARD"))
        assertTrue(vcard.contains("VERSION:3.0"))
        assertTrue(vcard.contains("FN:Carlos Santana Martínez"))
        assertTrue(vcard.contains("N:Martínez;Carlos Santana;;;"))
        assertTrue(vcard.contains("TEL;TYPE=CELL:+34655443322"))
        assertTrue(vcard.contains("EMAIL;TYPE=INTERNET:carlos@musica.org"))
        assertTrue(vcard.contains("ORG:Orquesta Rock"))
        assertTrue(vcard.contains("ADR;TYPE=HOME:;;Calle Mayor 10\\, Madrid;;;;"))
        assertTrue(vcard.contains("NOTE:Guitarrista principal"))
        assertTrue(vcard.endsWith("END:VCARD"))
    }
}
