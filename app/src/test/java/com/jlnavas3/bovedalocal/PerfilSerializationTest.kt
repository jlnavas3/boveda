/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Perfil
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PerfilSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun deserializacionRetrocompatibleDesdeIdentidades() {
        val jsonString = """
            {
                "version": 1,
                "entradas": [],
                "identidades": [
                    {
                        "id": "id-123",
                        "nombre": "Trabajo",
                        "correoPrincipal": "trabajo@empresa.com"
                    }
                ]
            }
        """.trimIndent()

        val contenido = json.decodeFromString<ContenidoBoveda>(jsonString)
        assertEquals(1, contenido.perfiles.size)
        assertEquals("Trabajo", contenido.perfiles[0].nombre)
        assertEquals("trabajo@empresa.com", contenido.perfiles[0].correoPrincipal)
        assertEquals("id-123", contenido.perfiles[0].id)
        // Verificar alias identidades
        assertEquals(1, contenido.identidades.size)
        assertEquals("Trabajo", contenido.identidades[0].nombre)
    }

    @Test
    fun serializacionProduceIdentidadesParaRetrocompatibilidad() {
        val contenido = ContenidoBoveda(
            perfiles = listOf(
                Perfil(
                    id = "id-456",
                    nombre = "Personal",
                    correoPrincipal = "personal@correo.com"
                )
            )
        )

        val serializado = json.encodeToString(ContenidoBoveda.serializer(), contenido)
        assertTrue(serializado.contains("\"identidades\""))
        assertTrue(serializado.contains("\"Personal\""))
    }

    @Test
    fun serializacionYDeserializacionConAsociacionesMultiples() {
        val perfil = Perfil(
            id = "perfil-test-1",
            nombre = "Compras Online",
            correoPrincipal = "compras@correo.com",
            tarjetasIds = listOf("tarjeta-1", "tarjeta-2"),
            tarjetaPredeterminadaId = "tarjeta-1",
            cuentasIds = listOf("cuenta-1"),
            cuentaPredeterminadaId = "cuenta-1",
            contactosIds = listOf("contacto-1", "contacto-2"),
            contactoPredeterminadoId = "contacto-2"
        )
        val contenido = ContenidoBoveda(perfiles = listOf(perfil))

        val serializado = json.encodeToString(ContenidoBoveda.serializer(), contenido)
        val deserializado = json.decodeFromString<ContenidoBoveda>(serializado)

        val resultado = deserializado.perfiles.first()
        assertEquals(2, resultado.tarjetasIds.size)
        assertEquals("tarjeta-1", resultado.tarjetaPredeterminadaId)
        assertEquals(1, resultado.cuentasIds.size)
        assertEquals("cuenta-1", resultado.cuentaPredeterminadaId)
        assertEquals(2, resultado.contactosIds.size)
        assertEquals("contacto-2", resultado.contactoPredeterminadoId)
    }

    @Test
    fun retrocompatibilidadConJsonSinCamposAsociaciones() {
        val jsonLegacy = """
            {
                "id": "perfil-legado",
                "nombre": "Antiguo",
                "correoPrincipal": "antiguo@correo.com"
            }
        """.trimIndent()

        val perfil = json.decodeFromString<Perfil>(jsonLegacy)
        assertEquals("perfil-legado", perfil.id)
        assertTrue(perfil.tarjetasIds.isEmpty())
        assertTrue(perfil.tarjetaPredeterminadaId == null)
        assertTrue(perfil.cuentasIds.isEmpty())
        assertTrue(perfil.cuentaPredeterminadaId == null)
        assertTrue(perfil.contactosIds.isEmpty())
        assertTrue(perfil.contactoPredeterminadoId == null)
    }
}
