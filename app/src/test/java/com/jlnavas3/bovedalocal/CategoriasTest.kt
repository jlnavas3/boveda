package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.IconosCategorias
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoriasTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `serializacion y deserializacion de categorias en ContenidoBoveda`() {
        val cat1 = Categoria(
            id = "cat-trabajo",
            nombre = "Trabajo",
            icono = "maletin",
            colorHex = "#3B82F6",
            creadaEn = 1000L,
            modificadaEn = 2000L
        )
        val cat2 = Categoria(
            id = "cat-finanzas",
            nombre = "Finanzas",
            icono = "banco",
            colorHex = "#10B981",
            creadaEn = 3000L,
            modificadaEn = 4000L
        )

        val e1 = Entrada(
            id = "e-1",
            titulo = "Slack Empresa",
            categorias = listOf("cat-trabajo")
        )
        val e2 = Entrada(
            id = "e-2",
            titulo = "Banco",
            categorias = listOf("cat-finanzas", "cat-trabajo")
        )

        val contenido = ContenidoBoveda(
            entradas = listOf(e1, e2),
            categorias = listOf(cat1, cat2)
        )

        val serializado = json.encodeToString(contenido)
        // Verificar que en JSON el campo se llama "colecciones" para retrocompatibilidad
        assertTrue(serializado.contains("\"colecciones\":"))

        val deserializado = json.decodeFromString<ContenidoBoveda>(serializado)

        assertEquals(2, deserializado.categorias.size)
        assertEquals("Trabajo", deserializado.categorias[0].nombre)
        assertEquals("maletin", deserializado.categorias[0].icono)
        assertEquals("#3B82F6", deserializado.categorias[0].colorHex)
        assertEquals(2, deserializado.entradas.size)
        assertEquals(listOf("cat-trabajo"), deserializado.entradas[0].categorias)
        assertEquals(listOf("cat-finanzas", "cat-trabajo"), deserializado.entradas[1].categorias)

        // Verificar alias retrocompatible colecciones
        assertEquals(deserializado.categorias, deserializado.colecciones)
        assertEquals(deserializado.entradas[0].categorias, deserializado.entradas[0].colecciones)
    }

    @Test
    fun `retrocompatibilidad con json sin campo colecciones o categorias`() {
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
        assertTrue(deserializado.entradas[0].categorias.isEmpty())
        assertTrue(deserializado.categorias.isEmpty())
    }

    @Test
    fun `filtrado de entradas por categoria`() {
        val e1 = Entrada(id = "1", titulo = "GitHub", categorias = listOf("dev"))
        val e2 = Entrada(id = "2", titulo = "AWS", categorias = listOf("dev", "cloud"))
        val e3 = Entrada(id = "3", titulo = "Banco", categorias = listOf("personal"))

        val entradas = listOf(e1, e2, e3)

        val filtroDev = entradas.filter { it.categorias.contains("dev") }
        assertEquals(2, filtroDev.size)
        assertTrue(filtroDev.any { it.id == "1" })
        assertTrue(filtroDev.any { it.id == "2" })

        val filtroCloud = entradas.filter { it.categorias.contains("cloud") }
        assertEquals(1, filtroCloud.size)
        assertEquals("2", filtroCloud.first().id)

        val filtroInexistente = entradas.filter { it.categorias.contains("inexistente") }
        assertTrue(filtroInexistente.isEmpty())
    }

    @Test
    fun `catalogo de iconos y colores disponibles`() {
        assertTrue(IconosCategorias.OPCIONES.isNotEmpty())
        assertTrue(IconosCategorias.COLORES_PREDETERMINADOS.isNotEmpty())

        val iconoTrabajo = IconosCategorias.obtenerIcono("trabajo")
        assertNotNull(iconoTrabajo)

        val colorValido = IconosCategorias.parsearColorHex("#3B82F6")
        assertNotNull(colorValido)

        val colorNulo = IconosCategorias.parsearColorHex(null)
        assertEquals(null, colorNulo)

        val colorInvalido = IconosCategorias.parsearColorHex("no-es-hex")
        assertEquals(null, colorInvalido)
    }
}
