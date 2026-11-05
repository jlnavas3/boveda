/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.contactos.ParserVcardContacto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParserVcardContactoTest {

    @Test
    fun parsear_vcard30_individual() {
        val vcard = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Dr. Carlos Benítez
            N:Benítez;Carlos;;Dr.;
            TEL;TYPE=CELL,VOICE:+34 611 22 33 44
            TEL;TYPE=WORK:+34 91 555 44 33
            EMAIL;TYPE=PREF,INTERNET:carlos.benitez@clinica.es
            EMAIL;TYPE=HOME:carlos@personal.com
            ORG:Clínica Dental Benítez;Cirugía
            TITLE:Director Médico
            ADR;TYPE=WORK:;;Paseo de la Castellana 100;Madrid;Madrid;28046;España
            NOTE:Contacto recomendado por el Dr. Gómez.\nRevisión anual pendiente.
            URL:https://clinicabenitez.es
            END:VCARD
        """.trimIndent()

        val contactos = ParserVcardContacto.parsear(vcard)
        assertEquals(1, contactos.size)

        val c = contactos.first()
        assertEquals("Dr. Carlos Benítez", c.nombre)
        assertEquals(2, c.telefonos.size)
        assertEquals("+34 611 22 33 44", c.telefonos[0])
        assertEquals("+34 91 555 44 33", c.telefonos[1])
        assertEquals(2, c.correos.size)
        assertEquals("carlos.benitez@clinica.es", c.correos[0])
        assertEquals("Clínica Dental Benítez - Cirugía", c.organizacion)
        assertEquals("Director Médico", c.cargo)
        assertNotNull(c.notas)
        assertTrue(c.notas!!.contains("Revisión anual pendiente"))
        assertEquals(1, c.direcciones.size)
        assertEquals("Madrid", c.direcciones[0].ciudad)
        assertEquals("28046", c.direcciones[0].codigoPostal)
        assertEquals("España", c.direcciones[0].pais)
        assertEquals("https://clinicabenitez.es", c.sitiosWeb.firstOrNull())
    }

    @Test
    fun parsear_vcard_multiple() {
        val vcfMultiple = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Alice Smith
            TEL:+1555123456
            EMAIL:alice@example.com
            END:VCARD
            BEGIN:VCARD
            VERSION:3.0
            FN:Bob Johnson
            TEL:+1555987654
            EMAIL:bob@example.com
            ORG:Acme Corp
            END:VCARD
        """.trimIndent()

        val contactos = ParserVcardContacto.parsear(vcfMultiple)
        assertEquals(2, contactos.size)
        assertEquals("Alice Smith", contactos[0].nombre)
        assertEquals("+1555123456", contactos[0].telefonos[0])
        assertEquals("Bob Johnson", contactos[1].nombre)
        assertEquals("Acme Corp", contactos[1].organizacion)
    }

    @Test
    fun parsear_vcard21_quoted_printable_con_despliegue_lineas() {
        val vcard21 = """
            BEGIN:VCARD
            VERSION:2.1
            N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:Garc=C3=ADa;Mar=C3=ADa;;;
            FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:Mar=C3=ADa Garc=C3=ADa
            TEL;CELL:+34600000000
            NOTE;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:Esta es una nota muy larga que requiere des=
            pliegue de l=C3=ADneas en vCard 2.1
            END:VCARD
        """.trimIndent()

        val contactos = ParserVcardContacto.parsear(vcard21)
        assertEquals(1, contactos.size)

        val c = contactos.first()
        assertEquals("María García", c.nombre)
        assertEquals("+34600000000", c.telefonos[0])
        assertNotNull(c.notas)
        assertTrue(c.notas!!.contains("líneas en vCard 2.1"))
    }

    @Test
    fun parsearAEntradas_genera_entradas_contacto_completas() {
        val vcard = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Laura Méndez
            TEL:+34677889900
            EMAIL:laura@correo.es
            END:VCARD
        """.trimIndent()

        val entradas = ParserVcardContacto.parsearAEntradas(vcard)
        assertEquals(1, entradas.size)

        val entrada = entradas.first()
        assertEquals("Laura Méndez", entrada.titulo)
        assertEquals(TipoEntrada.CONTACTO, entrada.tipo)
        assertEquals("+34677889900", entrada.usuario)
        assertEquals("laura@correo.es", entrada.datosContacto?.correo)
    }
}
