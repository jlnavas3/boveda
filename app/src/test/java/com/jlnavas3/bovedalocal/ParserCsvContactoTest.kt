/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.contactos.ConversorContactoEntrada
import com.jlnavas3.bovedalocal.data.contactos.LectorArchivoContactos
import com.jlnavas3.bovedalocal.data.contactos.ParserCsvContacto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParserCsvContactoTest {

    @Test
    fun parsear_csv_google_contacts() {
        val csvGoogle = """
            Name,Given Name,Family Name,E-mail 1 - Type,E-mail 1 - Value,Phone 1 - Type,Phone 1 - Value,Phone 2 - Type,Phone 2 - Value,Address 1 - Street,Address 1 - City,Address 1 - Region,Address 1 - Postal Code,Address 1 - Country,Organization 1 - Name,Organization 1 - Title,Website 1 - Value,Notes
            "Laura Gómez",Laura,Gómez,* Home,laura@gmail.com,Mobile,+34 600 11 22 33,Work,+34 91 222 33 44,"Calle Gran Vía 12",Madrid,Madrid,28013,España,"Tech Solutions",Arquitecta,https://lauragomez.dev,"Contacto prioritario de ingeniería"
        """.trimIndent()

        val contactos = ParserCsvContacto.parsear(csvGoogle)
        assertEquals(1, contactos.size)

        val c = contactos.first()
        assertEquals("Laura Gómez", c.nombre)
        assertEquals(2, c.telefonos.size)
        assertEquals("+34 600 11 22 33", c.telefonos[0])
        assertEquals("+34 91 222 33 44", c.telefonos[1])
        assertEquals("laura@gmail.com", c.correos.firstOrNull())
        assertEquals("Tech Solutions", c.organizacion)
        assertEquals("Arquitecta", c.cargo)
        assertEquals("https://lauragomez.dev", c.sitiosWeb.firstOrNull())
        assertEquals("Contacto prioritario de ingeniería", c.notas)

        assertEquals(1, c.direcciones.size)
        val dir = c.direcciones.first()
        assertEquals("Calle Gran Vía 12", dir.calle)
        assertEquals("Madrid", dir.ciudad)
        assertEquals("28013", dir.codigoPostal)
        assertEquals("España", dir.pais)

        // Verificación de conversión a Entrada
        val entrada = ConversorContactoEntrada.convertir(c)
        assertEquals("Laura Gómez", entrada.titulo)
        assertEquals("+34 600 11 22 33", entrada.usuario)
        assertEquals("+34 91 222 33 44", entrada.datosContacto?.telefonoSecundario)
        assertEquals("laura@gmail.com", entrada.datosContacto?.correo)
        assertEquals("Calle Gran Vía 12", entrada.datosContacto?.direccion)
        assertEquals("Madrid", entrada.datosContacto?.ciudad)
        assertEquals("28013", entrada.datosContacto?.codigoPostal)
        assertEquals("España", entrada.datosContacto?.pais)
        assertTrue(entrada.camposPersonalizados.any { it.etiqueta == "Empresa / Organización" && it.valor == "Tech Solutions" })
        assertTrue(entrada.camposPersonalizados.any { it.etiqueta == "Cargo / Puesto" && it.valor == "Arquitecta" })
        assertEquals("https://lauragomez.dev", entrada.urls.firstOrNull())
    }

    @Test
    fun parsear_csv_outlook_contacts() {
        val csvOutlook = """
            First Name,Last Name,E-mail Address,Mobile Phone,Home Phone,Home Street,Home City,Home State,Home Postal Code,Home Country/Region,Company,Job Title,Web Page,Notes
            Roberto,Martínez,roberto@outlook.com,+1 415 555 0199,+1 415 555 0100,"100 Market St",San Francisco,California,94105,USA,"Global Corp",Consultor,https://globalcorp.com,"Nota de Outlook"
        """.trimIndent()

        val contactos = ParserCsvContacto.parsear(csvOutlook)
        assertEquals(1, contactos.size)

        val c = contactos.first()
        assertEquals("Roberto Martínez", c.nombre)
        assertEquals(2, c.telefonos.size)
        assertEquals("+1 415 555 0199", c.telefonos[0])
        assertEquals("roberto@outlook.com", c.correos.firstOrNull())
        assertEquals("Global Corp", c.organizacion)
        assertEquals("Consultor", c.cargo)
        assertEquals("San Francisco", c.direcciones.firstOrNull()?.ciudad)
        assertEquals("USA", c.direcciones.firstOrNull()?.pais)
    }

    @Test
    fun parsear_csv_generico_con_punto_y_coma_y_multilinea() {
        val csvSemicolon = """
            Nombre;Teléfono;Correo;Dirección;Ciudad;Notas
            "Ana Belén";"+593 99 123 4567";ana@empresa.ec;"Av. Amazonas 100";Quito;"Nota con
            salto de línea y ""comillas""${'"'}
            "Pedro Páramo";"+593 98 765 4321";pedro@paramo.ec;"Comala Centro";Colima;"Sin notas"
        """.trimIndent()

        val contactos = ParserCsvContacto.parsear(csvSemicolon)
        assertEquals(2, contactos.size)

        assertEquals("Ana Belén", contactos[0].nombre)
        assertEquals("+593 99 123 4567", contactos[0].telefonos.firstOrNull())
        assertEquals("ana@empresa.ec", contactos[0].correos.firstOrNull())
        assertEquals("Quito", contactos[0].direcciones.firstOrNull()?.ciudad)
        assertTrue(contactos[0].notas!!.contains("salto de línea"))
        assertTrue(contactos[0].notas!!.contains("\"comillas\""))

        assertEquals("Pedro Páramo", contactos[1].nombre)
        assertEquals("Colima", contactos[1].direcciones.firstOrNull()?.ciudad)
    }

    @Test
    fun enrutador_unificado_lector_archivo_contactos() {
        // vCard
        val vcard = """
            BEGIN:VCARD
            VERSION:3.0
            FN:Elena Rivas
            TEL:+34655443322
            END:VCARD
        """.trimIndent()

        val cVcard = LectorArchivoContactos.parsear(vcard)
        assertEquals(1, cVcard.size)
        assertEquals("Elena Rivas", cVcard.first().nombre)

        // CSV
        val csv = """
            Nombre,Teléfono,Correo
            Elena Rivas,+34655443322,elena@rivas.com
        """.trimIndent()

        val cCsv = LectorArchivoContactos.parsear(csv)
        assertEquals(1, cCsv.size)
        assertEquals("Elena Rivas", cCsv.first().nombre)
        assertEquals("elena@rivas.com", cCsv.first().correos.firstOrNull())
    }

    @Test
    fun ingesta_zero_loss_columnas_extra_como_campos_personalizados() {
        val csvConExtras = """
            Nombre,Teléfono,Departamento,ID Empleado,WhatsApp
            "Martín Vega",+34 677 88 99 00,Finanzas,EMP-90210,+34 677 88 99 00
        """.trimIndent()

        val contactos = ParserCsvContacto.parsear(csvConExtras)
        assertEquals(1, contactos.size)

        val c = contactos.first()
        assertEquals("Martín Vega", c.nombre)
        assertEquals("+34 677 88 99 00", c.telefonos.first())
        assertEquals("Finanzas", c.camposExtra["Departamento"])
        assertEquals("EMP-90210", c.camposExtra["ID Empleado"])

        // Al convertir a Entrada, deben existir como CampoPersonalizado
        val entrada = ConversorContactoEntrada.convertir(c)
        assertTrue(entrada.camposPersonalizados.any { it.etiqueta == "Departamento" && it.valor == "Finanzas" })
        assertTrue(entrada.camposPersonalizados.any { it.etiqueta == "ID Empleado" && it.valor == "EMP-90210" })
    }

    @Test
    fun parsear_csv_deduplicacion_telefonos_equivalentes_y_exclusion_metadatos_sincronizacion() {
        // Caso real Google Contacts: mismo teléfono con y sin guiones, más Group Membership
        val csvCarla = """
            Name,Phone 1 - Type,Phone 1 - Value,Phone 2 - Type,Phone 2 - Value,E-mail 1 - Type,E-mail 1 - Value,Organization 1 - Name,Organization 1 - Title,Group Membership
            "Carla Carvajal Mega Corp",Mobile,+59399-461-5671,Home,+593994615671,* Work,carla.carvajal_mega@outlook.com,Megacorp,"Asesora Comercial",* myContacts ::: Friends
        """.trimIndent()

        val contactos = ParserCsvContacto.parsear(csvCarla)
        assertEquals(1, contactos.size)

        val c = contactos.first()
        assertEquals("Carla Carvajal Mega Corp", c.nombre)
        // Solo debe haber 1 teléfono (el equivalente fue unificado)
        assertEquals(1, c.telefonos.size)
        assertEquals("carla.carvajal_mega@outlook.com", c.correos.first())
        assertEquals("Megacorp", c.organizacion)
        assertEquals("Asesora Comercial", c.cargo)
        // Group Membership no debe estar en camposExtra
        assertTrue(c.camposExtra.none { it.key.contains("Group", ignoreCase = true) })

        val entrada = ConversorContactoEntrada.convertir(c)
        assertEquals("Carla Carvajal Mega Corp", entrada.titulo)
        assertEquals(c.telefonos.first(), entrada.usuario)
        // telefonoSecundario debe estar vacío, no duplicar el principal
        assertEquals("", entrada.datosContacto?.telefonoSecundario)
        assertEquals("carla.carvajal_mega@outlook.com", entrada.datosContacto?.correo)
        // En camposPersonalizados no debe haber ningún teléfono adicional duplicado
        assertTrue(entrada.camposPersonalizados.none { it.tipo == com.jlnavas3.bovedalocal.data.TipoCampo.TELEFONO })
        // Y no debe existir Group Membership en campos personalizados
        assertTrue(entrada.camposPersonalizados.none { it.etiqueta.contains("Group", ignoreCase = true) })
    }
}
