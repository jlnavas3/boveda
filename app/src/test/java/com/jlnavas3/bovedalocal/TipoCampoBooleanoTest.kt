/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TipoCampoBooleanoTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `tipo campo booleano tiene etiqueta correcta y no es sensible por defecto`() {
        val campo = CampoPersonalizado(
            etiqueta = "¿Requiere VPN?",
            valor = "true",
            tipo = TipoCampo.BOOLEANO
        )

        assertEquals("Sí/No (Booleano)", TipoCampo.BOOLEANO.etiqueta)
        assertEquals(TipoCampo.BOOLEANO, campo.tipo)
        assertEquals("true", campo.valor)
        assertFalse(campo.esSensibleEfectivo)
    }

    @Test
    fun `serializacion y deserializacion json conserva tipo booleano y valor`() {
        val original = CampoPersonalizado(
            etiqueta = "Autenticación 2FA Activada",
            valor = "false",
            tipo = TipoCampo.BOOLEANO
        )

        val raw = json.encodeToString(original)
        val recuperado = json.decodeFromString<CampoPersonalizado>(raw)

        assertEquals(original.id, recuperado.id)
        assertEquals(original.etiqueta, recuperado.etiqueta)
        assertEquals(original.valor, recuperado.valor)
        assertEquals(TipoCampo.BOOLEANO, recuperado.tipo)
    }

    @Test
    fun `evaluacion booleana tolera mayusculas y minusculas`() {
        val c1 = CampoPersonalizado(valor = "TRUE", tipo = TipoCampo.BOOLEANO)
        val c2 = CampoPersonalizado(valor = "True", tipo = TipoCampo.BOOLEANO)
        val c3 = CampoPersonalizado(valor = "false", tipo = TipoCampo.BOOLEANO)
        val c4 = CampoPersonalizado(valor = "", tipo = TipoCampo.BOOLEANO)

        assertTrue(c1.valor.equals("true", ignoreCase = true))
        assertTrue(c2.valor.equals("true", ignoreCase = true))
        assertFalse(c3.valor.equals("true", ignoreCase = true))
        assertFalse(c4.valor.equals("true", ignoreCase = true))
    }
}
