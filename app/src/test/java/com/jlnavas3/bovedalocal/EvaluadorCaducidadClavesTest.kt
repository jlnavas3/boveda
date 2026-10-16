/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoCaducidad
import com.jlnavas3.bovedalocal.data.EvaluadorCaducidadClaves
import com.jlnavas3.bovedalocal.data.TipoEntrada
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class EvaluadorCaducidadClavesTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `sin fecha de caducidad retorna SIN_CADUCIDAD`() {
        assertEquals(EstadoCaducidad.SIN_CADUCIDAD, EvaluadorCaducidadClaves.evaluar(null))
        assertEquals(EstadoCaducidad.SIN_CADUCIDAD, EvaluadorCaducidadClaves.evaluar(0L))
    }

    @Test
    fun `fecha en el pasado retorna EXPIRADA`() {
        val ahora = 1_700_000_000_000L
        val pasada = ahora - TimeUnit.DAYS.toMillis(5)
        assertEquals(EstadoCaducidad.EXPIRADA, EvaluadorCaducidadClaves.evaluar(pasada, ahora))
        assertTrue(EvaluadorCaducidadClaves.diasRestantes(pasada, ahora) < 0)
    }

    @Test
    fun `fecha dentro de los proximos 15 dias retorna POR_EXPIRAR`() {
        val ahora = 1_700_000_000_000L
        val proxima = ahora + TimeUnit.DAYS.toMillis(7)
        assertEquals(EstadoCaducidad.POR_EXPIRAR, EvaluadorCaducidadClaves.evaluar(proxima, ahora))
        assertEquals(7L, EvaluadorCaducidadClaves.diasRestantes(proxima, ahora))
    }

    @Test
    fun `fecha mas alla de 15 dias retorna VIGENTE`() {
        val ahora = 1_700_000_000_000L
        val vigente = ahora + TimeUnit.DAYS.toMillis(60)
        assertEquals(EstadoCaducidad.VIGENTE, EvaluadorCaducidadClaves.evaluar(vigente, ahora))
        assertEquals(60L, EvaluadorCaducidadClaves.diasRestantes(vigente, ahora))
    }

    @Test
    fun `calcularPreset suma exactamente los dias deseados`() {
        val ahora = 1_000_000L
        val en30Dias = EvaluadorCaducidadClaves.calcularPreset(30, ahora)
        assertEquals(ahora + TimeUnit.DAYS.toMillis(30), en30Dias)
    }

    @Test
    fun `serializacion de Entrada preserva fechaCaducidad`() {
        val entrada = Entrada(
            id = "e1",
            tipo = TipoEntrada.LOGIN,
            titulo = "Prueba",
            fechaCaducidad = 1_750_000_000_000L
        )
        val raw = json.encodeToString(entrada)
        val deserializada = json.decodeFromString<Entrada>(raw)
        assertEquals(1_750_000_000_000L, deserializada.fechaCaducidad)
    }

    @Test
    fun `deserializacion de JSON legado sin fechaCaducidad asigna null por defecto`() {
        val jsonLegado = """{"id":"e2","tipo":"LOGIN","titulo":"Antigua"}"""
        val deserializada = json.decodeFromString<Entrada>(jsonLegado)
        assertNull(deserializada.fechaCaducidad)
    }
}
