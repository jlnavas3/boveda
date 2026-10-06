package com.jlnavas3.bovedalocal

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FrenoIntentosTest {

    private fun calcularCastigo(
        intentos: Int,
        gratis: Int,
        castigoBaseSegundos: Long,
        maxCastigoSegundos: Long
    ): Long {
        if (gratis <= 0) return 0L
        if (intentos < gratis) return 0L
        return minOf(
            maxCastigoSegundos,
            castigoBaseSegundos * (1L shl minOf(6, intentos - gratis))
        )
    }

    @Test
    fun `sin freno cuando intentos son menores que el umbral gratis`() {
        val castigo = calcularCastigo(intentos = 4, gratis = 5, castigoBaseSegundos = 5L, maxCastigoSegundos = 300L)
        assertEquals(0L, castigo)
    }

    @Test
    fun `freno desactivado con umbral cero`() {
        val castigo = calcularCastigo(intentos = 10, gratis = 0, castigoBaseSegundos = 5L, maxCastigoSegundos = 300L)
        assertEquals(0L, castigo)
    }

    @Test
    fun `castigo inicial se aplica al alcanzar el umbral gratis`() {
        // Al 5to intento (intentos = 5, gratis = 5): 5 * 2^0 = 5 segundos
        val castigo = calcularCastigo(intentos = 5, gratis = 5, castigoBaseSegundos = 5L, maxCastigoSegundos = 300L)
        assertEquals(5L, castigo)
    }

    @Test
    fun `castigo crece exponencialmente con intentos sucesivos`() {
        // Intento 6: 5 * 2^1 = 10s
        assertEquals(10L, calcularCastigo(6, 5, 5L, 300L))
        // Intento 7: 5 * 2^2 = 20s
        assertEquals(20L, calcularCastigo(7, 5, 5L, 300L))
        // Intento 8: 5 * 2^3 = 40s
        assertEquals(40L, calcularCastigo(8, 5, 5L, 300L))
        // Intento 9: 5 * 2^4 = 80s
        assertEquals(80L, calcularCastigo(9, 5, 5L, 300L))
        // Intento 10: 5 * 2^5 = 160s
        assertEquals(160L, calcularCastigo(10, 5, 5L, 300L))
    }

    @Test
    fun `castigo se limita estrictamente al maximo configurado`() {
        // Intento 11: 5 * 2^6 = 320s -> debe topar en maxCastigo (300s)
        assertEquals(300L, calcularCastigo(11, 5, 5L, 300L))
        // Con tope configurado en 60s
        assertEquals(60L, calcularCastigo(11, 5, 5L, 60L))
    }

    @Test
    fun `umbral estricto de 3 intentos comienza penalizacion antes`() {
        // Al 3er intento con umbral 3 ya se penaliza
        val castigo = calcularCastigo(intentos = 3, gratis = 3, castigoBaseSegundos = 5L, maxCastigoSegundos = 300L)
        assertEquals(5L, castigo)
    }
}
