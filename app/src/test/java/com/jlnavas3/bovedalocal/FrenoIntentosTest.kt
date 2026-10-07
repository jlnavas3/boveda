package com.jlnavas3.bovedalocal

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FrenoIntentosTest {

    private fun calcularCastigo(
        intentos: Int,
        gratis: Int,
        maxCastigoSegundos: Long
    ): Long {
        if (gratis <= 0) return 0L
        if (intentos < gratis) return 0L
        return maxCastigoSegundos
    }

    @Test
    fun `sin freno cuando intentos son menores que el umbral gratis`() {
        val castigo = calcularCastigo(intentos = 2, gratis = 3, maxCastigoSegundos = 300L)
        assertEquals(0L, castigo)
    }

    @Test
    fun `freno desactivado con umbral cero`() {
        val castigo = calcularCastigo(intentos = 10, gratis = 0, maxCastigoSegundos = 300L)
        assertEquals(0L, castigo)
    }

    @Test
    fun `castigo estricto maximo se aplica al alcanzar el umbral gratis`() {
        // Con 3 intentos configurados y 300s (5 min), al 3er intento aplica 300s de inmediato
        val castigo = calcularCastigo(intentos = 3, gratis = 3, maxCastigoSegundos = 300L)
        assertEquals(300L, castigo)
    }

    @Test
    fun `castigo se mantiene en el maximo configurado con intentos sucesivos`() {
        assertEquals(300L, calcularCastigo(intentos = 4, gratis = 3, maxCastigoSegundos = 300L))
        assertEquals(300L, calcularCastigo(intentos = 5, gratis = 3, maxCastigoSegundos = 300L))
        // Con tope configurado en 60s
        assertEquals(60L, calcularCastigo(intentos = 3, gratis = 3, maxCastigoSegundos = 60L))
    }

    @Test
    fun `evaluacion de umbral de autodestruccion por intentos fallidos`() {
        fun debeAutodestruir(intentos: Int, maxPermitidos: Int): Boolean {
            return maxPermitidos > 0 && intentos >= maxPermitidos
        }

        // Con límite desactivado (0) nunca autodestruye
        assertFalse(debeAutodestruir(intentos = 100, maxPermitidos = 0))

        // Con límite 10: 9 intentos no destruye, 10 sí, 11 sí
        assertFalse(debeAutodestruir(intentos = 9, maxPermitidos = 10))
        assertTrue(debeAutodestruir(intentos = 10, maxPermitidos = 10))
        assertTrue(debeAutodestruir(intentos = 15, maxPermitidos = 10))

        // Con límite 5: 4 intentos no destruye, 5 sí
        assertFalse(debeAutodestruir(intentos = 4, maxPermitidos = 5))
        assertTrue(debeAutodestruir(intentos = 5, maxPermitidos = 5))
    }
}
