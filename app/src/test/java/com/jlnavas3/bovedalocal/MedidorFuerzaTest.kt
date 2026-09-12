package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.util.MedidorFuerza
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MedidorFuerzaTest {

    @Test
    fun `contrasena vacia devuelve puntuacion 0 y etiqueta Vacia`() {
        val f = MedidorFuerza.medir("")
        assertEquals(0, f.puntuacion)
        assertEquals("Vacía", f.etiqueta)
        assertEquals("Sin contraseña", f.tiempo)
        assertEquals(0.0, f.bits, 0.001)
    }

    @Test
    fun `contrasenas predecibles o comunes puntuan como debiles`() {
        val f1 = MedidorFuerza.medir("123456")
        assertTrue(f1.puntuacion <= 1)
        assertTrue(f1.etiqueta in listOf("Muy débil", "Débil"))

        val f2 = MedidorFuerza.medir("password")
        assertTrue(f2.puntuacion <= 1)

        val f3 = MedidorFuerza.medir("qwerty")
        assertTrue(f3.puntuacion <= 1)
    }

    @Test
    fun `contrasenas aleatorias largas puntuan como fuertes o excelentes`() {
        val f = MedidorFuerza.medir("k9#mP!2${'$'}vL8&wQ4*zT1")
        assertTrue(f.puntuacion >= 3)
        assertTrue(f.etiqueta in listOf("Fuerte", "Excelente"))
        assertTrue(f.bits > 50.0)
    }

    @Test
    fun `la entropia estimada es mayor a mayor variedad y longitud`() {
        val corta = MedidorFuerza.medir("abc")
        val media = MedidorFuerza.medir("abcDEF12")
        val larga = MedidorFuerza.medir("abcDEF12!@#${'$'}%^")

        assertTrue(media.bits > corta.bits)
        assertTrue(larga.bits > media.bits)
    }

    @Test
    fun `la fraccion de progreso esta acotada entre 0 y 1`() {
        val fMin = MedidorFuerza.medir("")
        assertTrue(fMin.fraccion in 0f..1f)

        val fMax = MedidorFuerza.medir("UnaFraseExtremadamenteLargaYCompleja12345!@#${'$'}")
        assertTrue(fMax.fraccion in 0.08f..1f)
    }

    @Test
    fun `soporta contrasenas muy largas sin lanzar excepciones`() {
        val enorme = "A".repeat(500)
        val f = MedidorFuerza.medir(enorme)
        assertTrue(f.bits >= 0.0)
    }
}
