/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo
import com.jlnavas3.bovedalocal.data.contactos.ConversorContactoEntrada
import com.jlnavas3.bovedalocal.data.contactos.DireccionPostalDispositivo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ConversorContactoEntradaTest {

    @Test
    fun `convertir crea entrada con tipo CONTACTO y mapea datos estructurados y personalizados`() {
        val contacto = ContactoDispositivo(
            id = "c-101",
            nombre = "María López",
            telefonos = listOf("+34611223344", "+34911223344", "+34655443322"),
            correos = listOf("maria@empresa.com", "maria.personal@gmail.com"),
            organizacion = "Innovaciones Tech",
            cargo = "Directora de Operaciones",
            notas = "Amiga de la universidad",
            direcciones = listOf(
                DireccionPostalDispositivo(
                    calle = "Gran Vía 42",
                    ciudad = "Madrid",
                    region = "Madrid",
                    codigoPostal = "28013",
                    pais = "España",
                    etiqueta = "Trabajo"
                ),
                DireccionPostalDispositivo(
                    calle = "Calle Mayor 10",
                    ciudad = "Toledo",
                    region = "Toledo",
                    codigoPostal = "45001",
                    pais = "España",
                    etiqueta = "Particular"
                )
            ),
            sitiosWeb = listOf("https://maria.dev", "https://tech.es")
        )

        val entrada = ConversorContactoEntrada.convertir(contacto)

        assertEquals("María López", entrada.titulo)
        assertEquals(TipoEntrada.CONTACTO, entrada.tipo)
        assertEquals("+34611223344", entrada.usuario)
        assertEquals("Amiga de la universidad", entrada.notas)
        assertEquals(listOf("https://maria.dev"), entrada.urls)

        // DatosContacto estructurados
        assertNotNull(entrada.datosContacto)
        assertEquals("+34911223344", entrada.datosContacto?.telefonoSecundario)
        assertEquals("maria@empresa.com", entrada.datosContacto?.correo)
        assertEquals("Gran Vía 42", entrada.datosContacto?.direccion)
        assertEquals("Madrid", entrada.datosContacto?.ciudad)
        assertEquals("Madrid", entrada.datosContacto?.provincia)
        assertEquals("28013", entrada.datosContacto?.codigoPostal)
        assertEquals("España", entrada.datosContacto?.pais)

        // 3er teléfono en campos personalizados
        val tel3 = entrada.camposPersonalizados.find { it.valor == "+34655443322" }
        assertNotNull(tel3)
        assertEquals(TipoCampo.TELEFONO, tel3?.tipo)

        // 2do correo en campos personalizados
        val email2 = entrada.camposPersonalizados.find { it.valor == "maria.personal@gmail.com" }
        assertNotNull(email2)
        assertEquals(TipoCampo.EMAIL, email2?.tipo)

        // 2da dirección postal en campos personalizados
        val dir2 = entrada.camposPersonalizados.find { it.etiqueta.startsWith("Dirección") }
        assertNotNull(dir2)
        assertEquals(TipoCampo.TEXTO, dir2?.tipo)

        // Empresa
        val org = entrada.camposPersonalizados.find { it.valor == "Innovaciones Tech" }
        assertNotNull(org)
        assertEquals("Empresa / Organización", org?.etiqueta)

        // Cargo
        val cargo = entrada.camposPersonalizados.find { it.valor == "Directora de Operaciones" }
        assertNotNull(cargo)
        assertEquals("Cargo / Puesto", cargo?.etiqueta)

        // 2do sitio web
        val web2 = entrada.camposPersonalizados.find { it.valor == "https://tech.es" }
        assertNotNull(web2)
        assertEquals(TipoCampo.URL, web2?.tipo)
    }
}
