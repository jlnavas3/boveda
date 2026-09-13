package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.util.FormateadorCampos
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FormateadorCamposTest {

    @Test
    fun `formatearFecha formatea correctamente segun cada patron`() {
        assertEquals("31/12/2026", FormateadorCampos.formatearFecha(2026, 11, 31, FormateadorCampos.FECHA_DD_MM_AAAA))
        assertEquals("2026-12-31", FormateadorCampos.formatearFecha(2026, 11, 31, FormateadorCampos.FECHA_AAAA_MM_DD))
        assertEquals("12/31/2026", FormateadorCampos.formatearFecha(2026, 11, 31, FormateadorCampos.FECHA_MM_DD_AAAA))
        assertEquals("31-12-2026", FormateadorCampos.formatearFecha(2026, 11, 31, FormateadorCampos.FECHA_DD_GUION_MM_AAAA))
    }

    @Test
    fun `parsearFecha recupera anio mes dia correctamente`() {
        val parseado1 = FormateadorCampos.parsearFecha("31/12/2026", FormateadorCampos.FECHA_DD_MM_AAAA)
        assertEquals(2026, parseado1?.first)
        assertEquals(11, parseado1?.second) // 0-based month
        assertEquals(31, parseado1?.third)

        val parseado2 = FormateadorCampos.parsearFecha("2026-12-31", FormateadorCampos.FECHA_AAAA_MM_DD)
        assertEquals(2026, parseado2?.first)
        assertEquals(11, parseado2?.second)
        assertEquals(31, parseado2?.third)

        val parseado3 = FormateadorCampos.parsearFecha("12/31/2026", FormateadorCampos.FECHA_MM_DD_AAAA)
        assertEquals(2026, parseado3?.first)
        assertEquals(11, parseado3?.second)
        assertEquals(31, parseado3?.third)

        assertNull(FormateadorCampos.parsearFecha("invalido", FormateadorCampos.FECHA_DD_MM_AAAA))
        assertNull(FormateadorCampos.parsearFecha("", FormateadorCampos.FECHA_DD_MM_AAAA))
    }

    @Test
    fun `formatearHora maneja modo 24h y 12h con AM PM`() {
        assertEquals("19:30", FormateadorCampos.formatearHora(19, 30, FormateadorCampos.HORA_24H))
        assertEquals("07:30 PM", FormateadorCampos.formatearHora(19, 30, FormateadorCampos.HORA_12H))
        assertEquals("12:00 PM", FormateadorCampos.formatearHora(12, 0, FormateadorCampos.HORA_12H))
        assertEquals("12:00 AM", FormateadorCampos.formatearHora(0, 0, FormateadorCampos.HORA_12H))
        assertEquals("09:05 AM", FormateadorCampos.formatearHora(9, 5, FormateadorCampos.HORA_12H))
    }

    @Test
    fun `parsearHora extrae hora y minuto correctamente`() {
        val h24 = FormateadorCampos.parsearHora("19:30", FormateadorCampos.HORA_24H)
        assertEquals(19, h24?.first)
        assertEquals(30, h24?.second)

        val h12pm = FormateadorCampos.parsearHora("07:30 PM", FormateadorCampos.HORA_12H)
        assertEquals(19, h12pm?.first)
        assertEquals(30, h12pm?.second)

        val h12am = FormateadorCampos.parsearHora("12:15 AM", FormateadorCampos.HORA_12H)
        assertEquals(0, h12am?.first)
        assertEquals(15, h12am?.second)

        assertNull(FormateadorCampos.parsearHora("invalido", FormateadorCampos.HORA_24H))
        assertNull(FormateadorCampos.parsearHora("", FormateadorCampos.HORA_24H))
    }

    @Test
    fun `sanitizarNumero elimina cualquier caracter no digito`() {
        assertEquals("123456", FormateadorCampos.sanitizarNumero("123abc456!"))
        assertEquals("9876", FormateadorCampos.sanitizarNumero(" 98-76 "))
        assertEquals("", FormateadorCampos.sanitizarNumero("letras"))
    }

    @Test
    fun `sanitizarDecimal normaliza y permite solo un separador decimal`() {
        // Con punto como separador
        assertEquals("1250.50", FormateadorCampos.sanitizarDecimal("1250.50", "."))
        assertEquals("1250.50", FormateadorCampos.sanitizarDecimal("1250,50", "."))
        assertEquals("1250.50", FormateadorCampos.sanitizarDecimal("1250.5.0", "."))
        assertEquals("1250.50", FormateadorCampos.sanitizarDecimal("abc1250.50xyz", "."))

        // Con coma como separador
        assertEquals("1250,50", FormateadorCampos.sanitizarDecimal("1250,50", ","))
        assertEquals("1250,50", FormateadorCampos.sanitizarDecimal("1250.50", ","))
        assertEquals("1250,50", FormateadorCampos.sanitizarDecimal("1250,5,0", ","))
    }

    @Test
    fun `aplicarMascaraTelefono formatea segun el patron elegido`() {
        // Con espacios ### ### ####
        assertEquals("612 345 678", FormateadorCampos.aplicarMascaraTelefono("612345678", FormateadorCampos.TEL_ESPACIOS))

        // Con guiones ###-###-####
        assertEquals("612-345-678", FormateadorCampos.aplicarMascaraTelefono("612345678", FormateadorCampos.TEL_GUIONES))

        // Con parentesis (###) ###-####
        assertEquals("(612) 345-678", FormateadorCampos.aplicarMascaraTelefono("612345678", FormateadorCampos.TEL_PARENTESIS))

        // Internacional +## ### ### ####
        assertEquals("+34 612 345 678", FormateadorCampos.aplicarMascaraTelefono("+34612345678", FormateadorCampos.TEL_INTERNACIONAL))

        // Sin mascara
        assertEquals("612345678", FormateadorCampos.aplicarMascaraTelefono("612345678", FormateadorCampos.TEL_SIN_MASCARA))
    }
}
