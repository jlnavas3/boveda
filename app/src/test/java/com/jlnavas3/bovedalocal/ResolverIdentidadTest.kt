package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ResolverIdentidadTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val idPersonal = Identidad(
        id = "id-pers",
        nombre = "Personal",
        correoPrincipal = "juan@gmail.com",
        correosSecundarios = listOf("juan.alias@gmail.com"),
        colorHex = "#3B82F6",
        icono = "person"
    )

    private val idTrabajo = Identidad(
        id = "id-trab",
        nombre = "Trabajo",
        correoPrincipal = "juan@empresa.com",
        correosSecundarios = emptyList(),
        colorHex = "#10B981",
        icono = "work"
    )

    private val listaIdentidades = listOf(idPersonal, idTrabajo)

    @Test
    fun `resolucion explicita por identidadId tiene maxima prioridad`() {
        val entrada = Entrada(
            id = "e1",
            titulo = "Servicio X",
            usuario = "juan@empresa.com", // Coincide con Trabajo
            identidadId = "id-pers"       // Pero tiene asignado Personal explícitamente
        )
        val res = resolverIdentidadParaEntrada(entrada, listaIdentidades)
        assertNotNull(res)
        assertEquals("id-pers", res?.id)
        assertEquals("Personal", res?.nombre)
    }

    @Test
    fun `resolucion automatica por correo principal insensible a mayusculas`() {
        val entrada = Entrada(
            id = "e2",
            titulo = "GitHub",
            usuario = "JUAN@GMAIL.COM"
        )
        val res = resolverIdentidadParaEntrada(entrada, listaIdentidades)
        assertNotNull(res)
        assertEquals("id-pers", res?.id)
    }

    @Test
    fun `resolucion automatica por correo secundario o alias`() {
        val entrada = Entrada(
            id = "e3",
            titulo = "Newsletter",
            usuario = " Juan.Alias@Gmail.com " // Con espacios y mayúsculas
        )
        val res = resolverIdentidadParaEntrada(entrada, listaIdentidades)
        assertNotNull(res)
        assertEquals("id-pers", res?.id)
    }

    @Test
    fun `sin coincidencia devuelve null para tipos no correo o desconocidos`() {
        val entradaWifi = Entrada(
            id = "e4",
            titulo = "Wi-Fi Casa",
            tipo = TipoEntrada.WIFI,
            usuario = "SSID_CASA"
        )
        val resWifi = resolverIdentidadParaEntrada(entradaWifi, listaIdentidades)
        assertNull(resWifi)

        val entradaDesconocida = Entrada(
            id = "e5",
            titulo = "Servidor Root",
            usuario = "root"
        )
        val resDesconocida = resolverIdentidadParaEntrada(entradaDesconocida, listaIdentidades)
        assertNull(resDesconocida)
    }

    @Test
    fun `serializacion y deserializacion en ContenidoBoveda mantiene identidades y enlace`() {
        val entrada = Entrada(
            id = "e1",
            titulo = "Banco",
            usuario = "juan@empresa.com",
            identidadId = "id-trab"
        )
        val boveda = ContenidoBoveda(
            entradas = listOf(entrada),
            identidades = listaIdentidades
        )

        val jsonStr = json.encodeToString(boveda)
        val recuperada = json.decodeFromString<ContenidoBoveda>(jsonStr)

        assertEquals(2, recuperada.identidades.size)
        assertEquals("Personal", recuperada.identidades[0].nombre)
        assertEquals("Trabajo", recuperada.identidades[1].nombre)
        assertEquals("id-trab", recuperada.entradas[0].identidadId)
    }
}
