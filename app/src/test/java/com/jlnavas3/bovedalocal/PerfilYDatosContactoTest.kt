/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.DatosContacto
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.FusionadorSubmodelosNativos
import com.jlnavas3.bovedalocal.data.MigradorEntradasNativas
import com.jlnavas3.bovedalocal.data.Perfil
import com.jlnavas3.bovedalocal.data.PreferenciaIdentificadorPerfil
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PerfilYDatosContactoTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `datosContacto formatea direccion y serializa correctamente`() {
        val contacto = DatosContacto(
            telefonoSecundario = "+593991234567",
            correo = "contacto@ejemplo.com",
            direccion = "Av. Amazonas 123 y Naciones Unidas",
            codigoPostal = "170505",
            ciudad = "Quito",
            provincia = "Pichincha",
            pais = "Ecuador"
        )

        assertTrue(contacto.tieneDireccion)
        assertEquals("Av. Amazonas 123 y Naciones Unidas, Quito, Pichincha, 170505, Ecuador", contacto.direccionFormateada)

        val serializado = json.encodeToString(contacto)
        val deserializado = json.decodeFromString<DatosContacto>(serializado)

        assertEquals(contacto, deserializado)
    }

    @Test
    fun `datosContacto vacio no tiene direccion`() {
        val contactoVacio = DatosContacto()
        assertFalse(contactoVacio.tieneDireccion)
        assertEquals("", contactoVacio.direccionFormateada)
    }

    @Test
    fun `migradorEntradasNativas extrae datosContacto desde campos personalizados`() {
        val entradaLegacy = Entrada(
            id = "c-1",
            titulo = "Juan Pérez",
            usuario = "+34600112233",
            tipo = TipoEntrada.CONTACTO,
            camposPersonalizados = listOf(
                CampoPersonalizado(etiqueta = "Teléfono secundario", valor = "+34600998877", tipo = TipoCampo.TELEFONO),
                CampoPersonalizado(etiqueta = "Correo", valor = "juan@empresa.es", tipo = TipoCampo.EMAIL),
                CampoPersonalizado(etiqueta = "Dirección", valor = "Calle Mayor 10"),
                CampoPersonalizado(etiqueta = "Código postal", valor = "28013"),
                CampoPersonalizado(etiqueta = "Ciudad", valor = "Madrid"),
                CampoPersonalizado(etiqueta = "Provincia", valor = "Madrid"),
                CampoPersonalizado(etiqueta = "País", valor = "España")
            )
        )

        val migrada = MigradorEntradasNativas.migrarEntrada(entradaLegacy)
        val contacto = migrada.contactoEfectivo

        assertNotNull(contacto)
        assertEquals("+34600998877", contacto?.telefonoSecundario)
        assertEquals("juan@empresa.es", contacto?.correo)
        assertEquals("Calle Mayor 10", contacto?.direccion)
        assertEquals("28013", contacto?.codigoPostal)
        assertEquals("Madrid", contacto?.ciudad)
        assertEquals("Madrid", contacto?.provincia)
        assertEquals("España", contacto?.pais)
    }

    @Test
    fun `fusionadorSubmodelosNativos fusiona datosContacto entre versiones locales y secundarias`() {
        val entradaPrincipal = Entrada(
            id = "c-prin",
            titulo = "Juan Principal",
            tipo = TipoEntrada.CONTACTO,
            datosContacto = DatosContacto(
                direccion = "Calle Nueva 2",
                codigoPostal = "28001",
                ciudad = "Madrid"
            )
        )
        val entradaSecundaria = Entrada(
            id = "c-sec",
            titulo = "Juan Secundario",
            tipo = TipoEntrada.CONTACTO,
            datosContacto = DatosContacto(
                direccion = "Calle Antigua 1",
                ciudad = "Madrid",
                pais = "España"
            )
        )

        val fusion = FusionadorSubmodelosNativos.fusionar(
            principal = entradaPrincipal,
            secundarias = listOf(entradaSecundaria)
        )

        val resultado = fusion.datosContacto
        assertNotNull(resultado)
        assertEquals("Calle Nueva 2", resultado?.direccion)
        assertEquals("28001", resultado?.codigoPostal)
        assertEquals("España", resultado?.pais)
    }

    @Test
    fun `perfil serializa y deserializa con documentos vinculados`() {
        val perfil = Perfil(
            id = "perfil-1",
            nombre = "Personal",
            correoPrincipal = "test@boveda.app",
            correosSecundarios = listOf("alias@boveda.app"),
            tipoDocumento = "CI",
            numeroDocumento = "0987654321",
            preferenciaIdentificador = PreferenciaIdentificadorPerfil.DOCUMENTO,
            colorHex = "#4CAF50",
            tarjetasIds = listOf("t-1"),
            tarjetaPredeterminadaId = "t-1",
            documentosIds = listOf("doc-1", "doc-2"),
            documentoPredeterminadoId = "doc-1"
        )

        val raw = json.encodeToString(perfil)
        val recuperado = json.decodeFromString<Perfil>(raw)

        assertEquals(perfil.id, recuperado.id)
        assertEquals(perfil.nombre, recuperado.nombre)
        assertEquals(listOf("doc-1", "doc-2"), recuperado.documentosIds)
        assertEquals("doc-1", recuperado.documentoPredeterminadoId)
        assertEquals("0987654321", recuperado.identificadorPreferido)
        assertTrue(recuperado.tieneDocumento)
    }
}
