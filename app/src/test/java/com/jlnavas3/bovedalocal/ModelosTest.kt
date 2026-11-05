/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelosTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `serializacion y deserializacion de campos personalizados`() {
        val entrada = Entrada(
            id = "test-1",
            titulo = "Banco",
            camposPersonalizados = listOf(
                CampoPersonalizado(etiqueta = "PIN Cajero", valor = "1234", tipo = TipoCampo.PIN),
                CampoPersonalizado(etiqueta = "Pregunta Seguridad", valor = "Mi mascota", tipo = TipoCampo.OCULTO),
                CampoPersonalizado(etiqueta = "Número de Cuenta", valor = "ES12345678", tipo = TipoCampo.TEXTO)
            )
        )

        val serializado = json.encodeToString(entrada)
        val deserializado = json.decodeFromString<Entrada>(serializado)

        assertEquals(3, deserializado.camposPersonalizados.size)
        assertEquals(TipoCampo.PIN, deserializado.camposPersonalizados[0].tipo)
        assertEquals("1234", deserializado.camposPersonalizados[0].valor)
        assertEquals(TipoCampo.OCULTO, deserializado.camposPersonalizados[1].tipo)
        assertEquals(TipoCampo.TEXTO, deserializado.camposPersonalizados[2].tipo)
    }

    @Test
    fun `retrocompatibilidad con json sin campos personalizados`() {
        val jsonViejo = """
            {
                "id": "antigua-1",
                "tipo": "LOGIN",
                "titulo": "Google",
                "usuario": "usuario@gmail.com",
                "contrasena": "secreto123"
            }
        """.trimIndent()

        val deserializado = json.decodeFromString<Entrada>(jsonViejo)
        assertEquals("Google", deserializado.titulo)
        assertTrue(deserializado.camposPersonalizados.isEmpty())
    }

    @Test
    fun `criterios de ordenacion clasifican correctamente`() {
        val e1 = Entrada(id = "1", titulo = "Beta", creadaEn = 100L, modificadaEn = 500L, favorito = false)
        val e2 = Entrada(id = "2", titulo = "Alpha", creadaEn = 200L, modificadaEn = 300L, favorito = false)
        val e3 = Entrada(id = "3", titulo = "Gamma", creadaEn = 50L, modificadaEn = 1000L, favorito = true)

        val lista = listOf(e1, e2, e3)

        // NOMBRE_AZ sin elevar favoritos
        val ordenAZ = lista.sortedWith(
            compareBy<Entrada> { it.titulo.lowercase() }
        )
        assertEquals("Alpha", ordenAZ[0].titulo)
        assertEquals("Beta", ordenAZ[1].titulo)
        assertEquals("Gamma", ordenAZ[2].titulo)

        // NOMBRE_ZA sin elevar favoritos
        val ordenZA = lista.sortedWith(
            compareByDescending<Entrada> { it.titulo.lowercase() }
        )
        assertEquals("Gamma", ordenZA[0].titulo)
        assertEquals("Beta", ordenZA[1].titulo)
        assertEquals("Alpha", ordenZA[2].titulo)

        // MODIFICACION_RECIENTE
        val ordenMod = lista.sortedWith(
            compareByDescending<Entrada> { it.modificadaEn }
        )
        assertEquals("Gamma", ordenMod[0].titulo) // 1000L
        assertEquals("Beta", ordenMod[1].titulo) // 500L
        assertEquals("Alpha", ordenMod[2].titulo) // 300L

        // CREACION_RECIENTE
        val ordenCrea = lista.sortedWith(
            compareByDescending<Entrada> { it.creadaEn }
        )
        assertEquals("Alpha", ordenCrea[0].titulo) // 200L
        assertEquals("Beta", ordenCrea[1].titulo) // 100L
        assertEquals("Gamma", ordenCrea[2].titulo) // 50L
    }
}
