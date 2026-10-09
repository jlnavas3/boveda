package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.GeneradorQr
import com.jlnavas3.bovedalocal.util.ParserBovedaQr
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParserBovedaQrTest {

    @Test
    fun `esBovedaTransfer identifica esquemas bovedalocal y boveda`() {
        assertTrue(ParserBovedaQr.esBovedaTransfer("bovedalocal://importar?payload=abc"))
        assertTrue(ParserBovedaQr.esBovedaTransfer("boveda://importar?payload=abc"))
        assertFalse(ParserBovedaQr.esBovedaTransfer("https://bovedalocal.com"))
        assertFalse(ParserBovedaQr.esBovedaTransfer("WIFI:T:WPA;S:Red;;"))
    }

    @Test
    fun `parsear payload generado por GeneradorQr reconstruye Entrada con nuevo ID`() {
        val original = Entrada(
            id = "id-original-123",
            tipo = TipoEntrada.LOGIN,
            titulo = "Cuenta Streaming",
            usuario = "usuario@stream.com",
            contrasena = "Pass1234!",
            notas = "Compartida con familia"
        )

        val uriTransferencia = GeneradorQr.textoTransferenciaBoveda(original)
        assertTrue(ParserBovedaQr.esBovedaTransfer(uriTransferencia))

        val parseada = ParserBovedaQr.parsear(uriTransferencia)
        assertNotNull(parseada)
        assertNotEquals("id-original-123", parseada!!.id) // Debe asignarse un nuevo UUID para evitar colisiones
        assertEquals(TipoEntrada.LOGIN, parseada.tipo)
        assertEquals("Cuenta Streaming", parseada.titulo)
        assertEquals("usuario@stream.com", parseada.usuario)
        assertEquals("Pass1234!", parseada.contrasena)
        assertEquals("Compartida con familia", parseada.notas)
    }

    @Test
    fun `parsear payload invalido devuelve null`() {
        assertNull(ParserBovedaQr.parsear("bovedalocal://importar?payload=jsonInvalidoNoCerrado{"))
        assertNull(ParserBovedaQr.parsear("bovedalocal://importar"))
        assertNull(ParserBovedaQr.parsear("otro://esquema"))
    }
}
