package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.UtilesSeguridadVisual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SeguridadVisualTest {

    @Test
    fun `valores por defecto de Seguridad Visual son coherentes`() {
        val ajustes = AjustesApp()

        assertFalse("Seguridad visual debe estar desactivada por defecto", ajustes.seguridadVisualActiva)
        assertEquals("desenfoque", ajustes.estiloOcultamientoVisual)
        assertEquals(10, ajustes.tiempoAutoOcultarSegundos)
        assertTrue("Ocultar usuario debe estar activo por defecto", ajustes.ocultarUsuario)
        assertTrue("Ocultar contraseña debe estar activo por defecto", ajustes.ocultarContrasena)
        assertFalse("Ocultar TOTP debe estar desactivado por defecto", ajustes.ocultarTotp)
        assertTrue("Ocultar notas debe estar activo por defecto", ajustes.ocultarNotas)
        assertTrue("Ocultar campos debe estar activo por defecto", ajustes.ocultarCampos)
    }

    @Test
    fun `opciones de estilo de ocultamiento contienen estilos validos`() {
        val claves = AlmacenAjustes.OPCIONES_ESTILO_OCULTAMIENTO.map { it.first }
        assertEquals(3, claves.size)
        assertTrue(claves.contains("desenfoque"))
        assertTrue(claves.contains("puntos_fijos"))
        assertTrue(claves.contains("puntos_reales"))
    }

    @Test
    fun `opciones de tiempo de auto-ocultar contienen tiempos correctos y modo manual`() {
        val tiempos = AlmacenAjustes.OPCIONES_TIEMPO_AUTO_OCULTAR.map { it.first }
        assertTrue(tiempos.contains(5))
        assertTrue(tiempos.contains(10))
        assertTrue(tiempos.contains(15))
        assertTrue(tiempos.contains(30))
        assertTrue("Debe incluir modo manual (0 segundos)", tiempos.contains(0))
    }

    @Test
    fun `utilidades de enmascaramiento funcionan correctamente`() {
        // Puntos fijos
        assertEquals("••••••••", UtilesSeguridadVisual.enmascarar("admin@empresa.com", "puntos_fijos", 8))
        assertEquals("••••", UtilesSeguridadVisual.enmascarar("clave", "puntos_fijos", 4))

        // Puntos reales
        assertEquals("•••••", UtilesSeguridadVisual.enmascarar("12345", "puntos_reales"))
        assertEquals("••••••••", UtilesSeguridadVisual.enmascarar("password", "puntos_reales"))

        // Texto vacío
        assertEquals("", UtilesSeguridadVisual.enmascarar("", "puntos_fijos"))
        assertEquals("", UtilesSeguridadVisual.enmascarar("", "puntos_reales"))
        assertEquals("", UtilesSeguridadVisual.enmascarar("", "desenfoque"))
    }

    @Test
    fun `modificacion de ajustes de seguridad visual es inmutable`() {
        val inicial = AjustesApp()
        val modificado = inicial.copy(
            seguridadVisualActiva = true,
            estiloOcultamientoVisual = "puntos_fijos",
            tiempoAutoOcultarSegundos = 15,
            ocultarTotp = true
        )

        assertFalse(inicial.seguridadVisualActiva)
        assertEquals("desenfoque", inicial.estiloOcultamientoVisual)
        assertEquals(10, inicial.tiempoAutoOcultarSegundos)
        assertFalse(inicial.ocultarTotp)

        assertTrue(modificado.seguridadVisualActiva)
        assertEquals("puntos_fijos", modificado.estiloOcultamientoVisual)
        assertEquals(15, modificado.tiempoAutoOcultarSegundos)
        assertTrue(modificado.ocultarTotp)
    }
}
