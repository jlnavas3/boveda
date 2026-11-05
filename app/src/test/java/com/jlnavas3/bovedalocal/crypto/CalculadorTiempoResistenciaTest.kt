/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.crypto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculadorTiempoResistenciaTest {

    @Test
    fun testBajosBitsDescifrableAlInstante() {
        assertEquals("Descifrable al instante", CalculadorTiempoResistencia.formatear(0.0))
        assertEquals("Descifrable al instante", CalculadorTiempoResistencia.formatear(10.0))
        assertEquals("Descifrable al instante", CalculadorTiempoResistencia.formatear(30.0))
    }

    @Test
    fun testSegundosMinutosHoras() {
        // 2^(bits - 1) / 10^11
        // Para 40 bits: 2^39 / 1e11 = 5.49 s -> Descifrable en 5 segundos
        val resultado40 = CalculadorTiempoResistencia.formatear(40.0)
        assertTrue("Esperaba segundos para 40 bits pero fue $resultado40", resultado40.contains("segundos"))

        // Para 48 bits: 2^47 / 1e11 = 1407 s (~23 min)
        val resultado48 = CalculadorTiempoResistencia.formatear(48.0)
        assertTrue("Esperaba minutos para 48 bits pero fue $resultado48", resultado48.contains("minutos"))

        // Para 52 bits: 2^51 / 1e11 = 22517 s (~6 horas)
        val resultado52 = CalculadorTiempoResistencia.formatear(52.0)
        assertTrue("Esperaba horas para 52 bits pero fue $resultado52", resultado52.contains("horas"))
    }

    @Test
    fun testDiasMesesAnos() {
        // Para 56 bits: 2^55 / 1e11 = 360287 s (~4.1 días)
        val resultado56 = CalculadorTiempoResistencia.formatear(56.0)
        assertTrue("Esperaba días para 56 bits pero fue $resultado56", resultado56.contains("días"))

        // Para 62 bits: 2^61 / 1e11 = 23058430 s (~8.8 meses)
        val resultado62 = CalculadorTiempoResistencia.formatear(62.0)
        assertTrue("Esperaba meses para 62 bits pero fue $resultado62", resultado62.contains("meses"))

        // Para 66 bits: 2^65 / 1e11 = 3.68e8 s (~11.6 años)
        val resultado66 = CalculadorTiempoResistencia.formatear(66.0)
        assertTrue("Esperaba años para 66 bits pero fue $resultado66", resultado66.contains("años"))
    }

    @Test
    fun testMilesYMillonesDeAnos() {
        // Para 75 bits: 2^74 / 1e11 / 31536000 ~= 5988 años (~6 mil años)
        val resultado75 = CalculadorTiempoResistencia.formatear(75.0)
        assertTrue("Esperaba mil años para 75 bits pero fue $resultado75", resultado75.contains("mil años"))

        // Para 89 bits (el caso de la captura: 14 caracteres):
        // 2^88 / 1e11 / 31536000 ~= 98.1 millones de años
        val resultado89 = CalculadorTiempoResistencia.formatear(89.0)
        assertTrue("Esperaba 98 millones de años para 89 bits pero fue $resultado89", resultado89.contains("millones de años"))
    }

    @Test
    fun testMilesDeMillonesYBillones() {
        // Para 100 bits: 2^99 / 1e11 / 31536000 ~= 200 mil millones de años
        val resultado100 = CalculadorTiempoResistencia.formatear(100.0)
        assertTrue("Esperaba mil millones de años para 100 bits pero fue $resultado100", resultado100.contains("mil millones de años"))

        // Para 110 bits: 2^109 / 1e11 / 31536000 ~= 205 billones de años
        val resultado110 = CalculadorTiempoResistencia.formatear(110.0)
        assertTrue("Esperaba billones de años para 110 bits pero fue $resultado110", resultado110.contains("billones de años"))
    }

    @Test
    fun testCuatrillonYMasivos() {
        // Para 150 bits: masivo
        val resultado150 = CalculadorTiempoResistencia.formatear(150.0)
        assertEquals("Resiste más de 1 cuatrillón de años", resultado150)
    }
}
