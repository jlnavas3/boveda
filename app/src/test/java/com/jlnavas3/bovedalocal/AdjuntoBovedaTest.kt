/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AdjuntoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.LectorAdjuntosSaf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Base64

class AdjuntoBovedaTest {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `serializacion y deserializacion de entrada con adjuntos funciona correctamente`() {
        val bytesOriginales = "Contenido confidencial de prueba 12345".toByteArray(Charsets.UTF_8)
        val base64 = Base64.getEncoder().encodeToString(bytesOriginales)

        val adjunto = AdjuntoBoveda(
            id = "adj-1",
            nombre = "documento.pdf",
            mimeType = "application/pdf",
            tamanoBytes = bytesOriginales.size.toLong(),
            contenidoBase64 = base64,
            fechaSubida = 1770000000000L
        )

        val entrada = Entrada(
            id = "ent-1",
            tipo = TipoEntrada.IDENTIDAD,
            titulo = "Pasaporte",
            adjuntos = listOf(adjunto)
        )

        val jsonStr = json.encodeToString(entrada)
        val reconstruida = json.decodeFromString<Entrada>(jsonStr)

        assertEquals(1, reconstruida.adjuntos.size)
        val adjuntoReconstruido = reconstruida.adjuntos[0]
        assertEquals("adj-1", adjuntoReconstruido.id)
        assertEquals("documento.pdf", adjuntoReconstruido.nombre)
        assertEquals("application/pdf", adjuntoReconstruido.mimeType)
        assertEquals(bytesOriginales.size.toLong(), adjuntoReconstruido.tamanoBytes)
        assertEquals(base64, adjuntoReconstruido.contenidoBase64)

        val bytesDecodificados = Base64.getDecoder().decode(adjuntoReconstruido.contenidoBase64)
        assertEquals("Contenido confidencial de prueba 12345", String(bytesDecodificados, Charsets.UTF_8))
    }

    @Test
    fun `retrocompatibilidad con entradas antiguas sin campo adjuntos`() {
        val jsonAntiguo = """
            {
                "id": "ent-antigua",
                "tipo": "LOGIN",
                "titulo": "Google",
                "usuario": "ejemplo@correo.com",
                "contrasena": "Secreta123"
            }
        """.trimIndent()

        val entrada = json.decodeFromString<Entrada>(jsonAntiguo)
        assertNotNull(entrada.adjuntos)
        assertTrue(entrada.adjuntos.isEmpty())
    }

    @Test
    fun `deduccion de tipo MIME por extension`() {
        assertEquals("application/pdf", LectorAdjuntosSaf.deducirMimeType("factura.pdf"))
        assertEquals("image/png", LectorAdjuntosSaf.deducirMimeType("captura.PNG"))
        assertEquals("image/jpeg", LectorAdjuntosSaf.deducirMimeType("foto.jpeg"))
        assertEquals("text/plain", LectorAdjuntosSaf.deducirMimeType("registro.log"))
        assertEquals("application/x-pem-file", LectorAdjuntosSaf.deducirMimeType("id_ed25519.pem"))
        assertEquals("application/octet-stream", LectorAdjuntosSaf.deducirMimeType("archivo.desconocido"))
    }

    @Test
    fun `formateo legible de tamanos`() {
        assertEquals("500 B", LectorAdjuntosSaf.formatearTamano(500))
        assertEquals("1.0 KB", LectorAdjuntosSaf.formatearTamano(1024))
        assertEquals("1.5 KB", LectorAdjuntosSaf.formatearTamano(1536))
        assertEquals("2.0 MB", LectorAdjuntosSaf.formatearTamano(2 * 1024 * 1024))
    }
}
