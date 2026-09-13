package com.jlnavas3.bovedalocal

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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

    @Test
    fun `transformarConMascara posiciona el cursor despues del numero al insertar separadores`() {
        // Caso 1: Escribir el 4to dígito en '### ### ####'
        // El texto previo era '612' (cursor en 3). Se escribe '3' -> '6123' (cursor en 4)
        val tfv4 = TextFieldValue(text = "6123", selection = TextRange(4))
        val res4 = FormateadorCampos.transformarConMascara(tfv4, "612") {
            FormateadorCampos.aplicarMascaraTelefono(it, FormateadorCampos.TEL_ESPACIOS)
        }
        assertEquals("612 3", res4.text)
        // El cursor debe estar en 5 (después del '3', no antes)
        assertEquals(5, res4.selection.end)

        // Caso 2: Escribir el 7mo dígito en '### ### ####'
        // El texto previo era '612 345' (cursor en 7). Se escribe '6' -> '612 3456' (cursor en 8)
        val tfv7 = TextFieldValue(text = "612 3456", selection = TextRange(8))
        val res7 = FormateadorCampos.transformarConMascara(tfv7, "612 345") {
            FormateadorCampos.aplicarMascaraTelefono(it, FormateadorCampos.TEL_ESPACIOS)
        }
        assertEquals("612 345 6", res7.text)
        // El cursor debe estar en 9 (después del '6', no antes)
        assertEquals(9, res7.selection.end)

        // Caso 3: Máscara con paréntesis '(###) ###-####'
        // Escribir '6' en texto vacío -> '(6' con cursor en 2
        val tfvPar1 = TextFieldValue(text = "6", selection = TextRange(1))
        val resPar1 = FormateadorCampos.transformarConMascara(tfvPar1, "") {
            FormateadorCampos.aplicarMascaraTelefono(it, FormateadorCampos.TEL_PARENTESIS)
        }
        assertEquals("(6", resPar1.text)
        assertEquals(2, resPar1.selection.end)

        // Escribir '3' tras '(612' -> '(612) 3' con cursor en 7
        val tfvPar4 = TextFieldValue(text = "(6123", selection = TextRange(5))
        val resPar4 = FormateadorCampos.transformarConMascara(tfvPar4, "(612") {
            FormateadorCampos.aplicarMascaraTelefono(it, FormateadorCampos.TEL_PARENTESIS)
        }
        assertEquals("(612) 3", resPar4.text)
        assertEquals(7, resPar4.selection.end)

        // Caso 4: Pulsar backspace borrando '3' de '612 3'
        val tfvBorrar = TextFieldValue(text = "612 ", selection = TextRange(4))
        val resBorrar = FormateadorCampos.transformarConMascara(tfvBorrar, "612 3") {
            FormateadorCampos.aplicarMascaraTelefono(it, FormateadorCampos.TEL_ESPACIOS)
        }
        assertEquals("612", resBorrar.text)
        assertEquals(3, resBorrar.selection.end)

        // Caso 5: Solo mover el cursor sin cambiar texto
        val tfvMov = TextFieldValue(text = "612 345", selection = TextRange(2))
        val resMov = FormateadorCampos.transformarConMascara(tfvMov, "612 345") {
            FormateadorCampos.aplicarMascaraTelefono(it, FormateadorCampos.TEL_ESPACIOS)
        }
        assertEquals("612 345", resMov.text)
        assertEquals(2, resMov.selection.end)
    }
}
