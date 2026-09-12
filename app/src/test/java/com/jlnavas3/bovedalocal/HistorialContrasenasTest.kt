package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.CambioContrasena
import com.jlnavas3.bovedalocal.data.Entrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HistorialContrasenasTest {

    private val MAX_HISTORIAL = 10

    /**
     * Simula la lógica de guardado, deduplicación y rotación de contraseñas de VaultRepository.
     */
    private fun actualizarEntrada(previa: Entrada, nueva: Entrada, momento: Long): Entrada {
        val historial = if (previa.contrasena.isNotBlank() && previa.contrasena != nueva.contrasena) {
            val previaSinDuplicados = previa.historialContrasenas
                .distinctBy { it.contrasena }
                .filterNot { it.contrasena == previa.contrasena || it.contrasena == nueva.contrasena }
            (listOf(CambioContrasena(previa.contrasena, previa.modificadaEn.takeIf { it > 0 } ?: momento)) + previaSinDuplicados)
                .take(MAX_HISTORIAL)
        } else {
            previa.historialContrasenas
                .distinctBy { it.contrasena }
                .filterNot { it.contrasena == nueva.contrasena }
        }
        return nueva.copy(
            modificadaEn = momento,
            creadaEn = previa.creadaEn,
            historialContrasenas = historial
        )
    }

    @Test
    fun `cambiar contrasena acumula la clave previa en el historial`() {
        val inicial = Entrada(
            id = "1",
            titulo = "Google",
            contrasena = "ClaveInicial123",
            modificadaEn = 1000L
        )

        val modificada = actualizarEntrada(
            previa = inicial,
            nueva = inicial.copy(contrasena = "ClaveNueva456"),
            momento = 2000L
        )

        assertEquals("ClaveNueva456", modificada.contrasena)
        assertEquals(1, modificada.historialContrasenas.size)
        assertEquals("ClaveInicial123", modificada.historialContrasenas[0].contrasena)
        assertEquals(1000L, modificada.historialContrasenas[0].cambiadaEn)
    }

    @Test
    fun `editar otros campos como usuario o notas no genera historial de contrasena`() {
        val inicial = Entrada(
            id = "1",
            titulo = "Google",
            usuario = "viejo@correo.com",
            contrasena = "MismaClave123",
            notas = "nota 1",
            modificadaEn = 1000L
        )

        val modificada = actualizarEntrada(
            previa = inicial,
            nueva = inicial.copy(usuario = "nuevo@correo.com", notas = "nota 2"),
            momento = 2000L
        )

        assertEquals("MismaClave123", modificada.contrasena)
        assertEquals(0, modificada.historialContrasenas.size)
    }

    @Test
    fun `el historial respeta el limite maximo de 10 contrasenas`() {
        var actual = Entrada(id = "1", contrasena = "Pass_0", modificadaEn = 100L)

        // Cambiar 15 veces la contraseña con claves distintas
        for (i in 1..15) {
            actual = actualizarEntrada(
                previa = actual,
                nueva = actual.copy(contrasena = "Pass_$i"),
                momento = 100L + (i * 10L)
            )
        }

        assertEquals("Pass_15", actual.contrasena)
        assertEquals(10, actual.historialContrasenas.size)
        // La primera en la lista es la más reciente (Pass_14)
        assertEquals("Pass_14", actual.historialContrasenas[0].contrasena)
        // La última conservada es Pass_5
        assertEquals("Pass_5", actual.historialContrasenas[9].contrasena)
    }

    @Test
    fun `restaurar una clave anterior no genera duplicados`() {
        val inicial = Entrada(id = "1", contrasena = "ClaveA", modificadaEn = 1000L)

        // Cambiamos a ClaveB -> Historial: [ClaveA]
        val paso1 = actualizarEntrada(
            previa = inicial,
            nueva = inicial.copy(contrasena = "ClaveB"),
            momento = 2000L
        )
        assertEquals("ClaveB", paso1.contrasena)
        assertEquals(listOf("ClaveA"), paso1.historialContrasenas.map { it.contrasena })

        // Restauramos ClaveA -> Activa: ClaveA, Historial: [ClaveB] (sin duplicar ClaveA)
        val paso2 = actualizarEntrada(
            previa = paso1,
            nueva = paso1.copy(contrasena = "ClaveA"),
            momento = 3000L
        )
        assertEquals("ClaveA", paso2.contrasena)
        assertEquals(listOf("ClaveB"), paso2.historialContrasenas.map { it.contrasena })
        assertFalse(paso2.historialContrasenas.any { it.contrasena == "ClaveA" })

        // Restauramos ClaveB -> Activa: ClaveB, Historial: [ClaveA] (sin duplicar ClaveB)
        val paso3 = actualizarEntrada(
            previa = paso2,
            nueva = paso2.copy(contrasena = "ClaveB"),
            momento = 4000L
        )
        assertEquals("ClaveB", paso3.contrasena)
        assertEquals(listOf("ClaveA"), paso3.historialContrasenas.map { it.contrasena })
        assertFalse(paso3.historialContrasenas.any { it.contrasena == "ClaveB" })

        // Restauramos de nuevo ClaveA repetidamente
        val paso4 = actualizarEntrada(
            previa = paso3,
            nueva = paso3.copy(contrasena = "ClaveA"),
            momento = 5000L
        )
        assertEquals("ClaveA", paso4.contrasena)
        assertEquals(listOf("ClaveB"), paso4.historialContrasenas.map { it.contrasena })
    }

    @Test
    fun `limpieza de duplicados preexistentes en historial`() {
        // Simular una entrada que ya tenía entradas duplicadas en el historial
        val conDuplicados = Entrada(
            id = "1",
            contrasena = "ClaveActual",
            modificadaEn = 1000L,
            historialContrasenas = listOf(
                CambioContrasena("ClaveVieja", 900L),
                CambioContrasena("ClaveVieja", 800L),
                CambioContrasena("ClaveActual", 700L),
                CambioContrasena("ClaveOtra", 600L)
            )
        )

        val resultado = actualizarEntrada(
            previa = conDuplicados,
            nueva = conDuplicados.copy(contrasena = "ClaveNueva"),
            momento = 2000L
        )

        assertEquals("ClaveNueva", resultado.contrasena)
        val clavesEnHistorial = resultado.historialContrasenas.map { it.contrasena }
        // Debe contener ClaveActual, ClaveVieja (una sola vez) y ClaveOtra (una sola vez)
        assertEquals(listOf("ClaveActual", "ClaveVieja", "ClaveOtra"), clavesEnHistorial)
    }
}
