package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.colecciones.IconosColecciones
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ColeccionesTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `serializacion y deserializacion de colecciones en ContenidoBoveda`() {
        val col1 = Coleccion(
            id = "col-trabajo",
            nombre = "Trabajo",
            icono = "maletin",
            colorHex = "#3B82F6",
            creadaEn = 1000L,
            modificadaEn = 2000L
        )
        val col2 = Coleccion(
            id = "col-finanzas",
            nombre = "Finanzas",
            icono = "banco",
            colorHex = "#10B981",
            creadaEn = 3000L,
            modificadaEn = 4000L
        )

        val e1 = Entrada(
            id = "e-1",
            titulo = "Slack Empresa",
            colecciones = listOf("col-trabajo")
        )
        val e2 = Entrada(
            id = "e-2",
            titulo = "Banco",
            colecciones = listOf("col-finanzas", "col-trabajo")
        )

        val contenido = ContenidoBoveda(
            entradas = listOf(e1, e2),
            colecciones = listOf(col1, col2)
        )

        val serializado = json.encodeToString(contenido)
        val deserializado = json.decodeFromString<ContenidoBoveda>(serializado)

        assertEquals(2, deserializado.colecciones.size)
        assertEquals("Trabajo", deserializado.colecciones[0].nombre)
        assertEquals("maletin", deserializado.colecciones[0].icono)
        assertEquals("#3B82F6", deserializado.colecciones[0].colorHex)
        assertEquals(2, deserializado.entradas.size)
        assertEquals(listOf("col-trabajo"), deserializado.entradas[0].colecciones)
        assertEquals(listOf("col-finanzas", "col-trabajo"), deserializado.entradas[1].colecciones)
    }

    @Test
    fun `retrocompatibilidad con json sin campo colecciones`() {
        val jsonViejo = """
            {
                "version": 1,
                "entradas": [
                    {
                        "id": "antigua-1",
                        "tipo": "LOGIN",
                        "titulo": "Google",
                        "usuario": "usuario@gmail.com",
                        "contrasena": "secreto123"
                    }
                ],
                "historialGenerador": []
            }
        """.trimIndent()

        val deserializado = json.decodeFromString<ContenidoBoveda>(jsonViejo)
        assertEquals(1, deserializado.entradas.size)
        assertTrue(deserializado.entradas[0].colecciones.isEmpty())
        assertTrue(deserializado.colecciones.isEmpty())
    }

    @Test
    fun `filtrado de entradas por coleccion`() {
        val e1 = Entrada(id = "1", titulo = "GitHub", colecciones = listOf("dev"))
        val e2 = Entrada(id = "2", titulo = "AWS", colecciones = listOf("dev", "cloud"))
        val e3 = Entrada(id = "3", titulo = "Banco", colecciones = listOf("personal"))

        val entradas = listOf(e1, e2, e3)

        val filtroDev = entradas.filter { it.colecciones.contains("dev") }
        assertEquals(2, filtroDev.size)
        assertTrue(filtroDev.any { it.id == "1" })
        assertTrue(filtroDev.any { it.id == "2" })

        val filtroCloud = entradas.filter { it.colecciones.contains("cloud") }
        assertEquals(1, filtroCloud.size)
        assertEquals("2", filtroCloud.first().id)

        val filtroInexistente = entradas.filter { it.colecciones.contains("inexistente") }
        assertTrue(filtroInexistente.isEmpty())
    }

    @Test
    fun `catalogo de iconos y colores disponibles`() {
        assertTrue(IconosColecciones.OPCIONES.isNotEmpty())
        assertTrue(IconosColecciones.COLORES_PREDETERMINADOS.isNotEmpty())

        val iconoTrabajo = IconosColecciones.obtenerIcono("trabajo")
        assertNotNull(iconoTrabajo)

        val colorValido = IconosColecciones.parsearColorHex("#3B82F6")
        assertNotNull(colorValido)

        val colorNulo = IconosColecciones.parsearColorHex(null)
        assertEquals(null, colorNulo)

        val colorInvalido = IconosColecciones.parsearColorHex("no-es-hex")
        assertEquals(null, colorInvalido)
    }
}
