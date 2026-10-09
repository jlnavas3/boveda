package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.contactos.ContactoDispositivo
import com.jlnavas3.bovedalocal.data.contactos.ConversorContactoEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ConversorContactoEntradaTest {

    @Test
    fun `convertir crea entrada con tipo CONTACTO y campos asignados correctamente`() {
        val contacto = ContactoDispositivo(
            id = "c-101",
            nombre = "María López",
            telefonos = listOf("+34611223344", "+34911223344"),
            correos = listOf("maria@empresa.com", "maria.personal@gmail.com"),
            organizacion = "Innovaciones Tech",
            cargo = "Directora de Operaciones",
            notas = "Amiga de la universidad"
        )

        val entrada = ConversorContactoEntrada.convertir(contacto)

        assertEquals("María López", entrada.titulo)
        assertEquals(TipoEntrada.CONTACTO, entrada.tipo)
        assertEquals("+34611223344", entrada.usuario)
        assertEquals("Amiga de la universidad", entrada.notas)

        // Teléfono secundario
        val telSecundario = entrada.camposPersonalizados.find { it.valor == "+34911223344" }
        assertNotNull(telSecundario)
        assertEquals(TipoCampo.TELEFONO, telSecundario?.tipo)

        // Correos
        val email1 = entrada.camposPersonalizados.find { it.valor == "maria@empresa.com" }
        assertNotNull(email1)
        assertEquals(TipoCampo.EMAIL, email1?.tipo)

        // Empresa
        val org = entrada.camposPersonalizados.find { it.valor == "Innovaciones Tech" }
        assertNotNull(org)
        assertEquals("Empresa / Organización", org?.etiqueta)

        // Cargo
        val cargo = entrada.camposPersonalizados.find { it.valor == "Directora de Operaciones" }
        assertNotNull(cargo)
        assertEquals("Cargo / Puesto", cargo?.etiqueta)
    }
}
