package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AjustesMappersTest {

    @Test
    fun opcionesAutoBloqueo_contieneValoresEsperados() {
        val opciones = AlmacenAjustes.OPCIONES_AUTO_BLOQUEO
        assertTrue(opciones.isNotEmpty())
        assertTrue(opciones.any { it.first == 30 })
        assertTrue(opciones.any { it.first == 60 })
        assertTrue(opciones.any { it.first == 300 })
    }

    @Test
    fun opcionesPortapapeles_contieneValoresEsperados() {
        val opciones = AlmacenAjustes.OPCIONES_PORTAPAPELES
        assertTrue(opciones.isNotEmpty())
        assertTrue(opciones.any { it.first == 15 })
        assertTrue(opciones.any { it.first == 30 })
        assertTrue(opciones.any { it.first == 60 })
    }

    @Test
    fun opcionesRecordatorioExportacion_contieneDiasValidos() {
        val opciones = AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION
        assertTrue(opciones.isNotEmpty())
        val dias = opciones.map { it.first }
        assertTrue(dias.contains(0)) // Nunca
        assertTrue(dias.contains(-30)) // Cada 30 minutos (prueba)
        assertTrue(dias.contains(30))
        assertTrue(dias.contains(60))
        assertTrue(dias.contains(90))
    }

    @Test
    fun opcionesTema_contieneTresModos() {
        val temas = AlmacenAjustes.OPCIONES_TEMA
        assertEquals(3, temas.size)
        val claves = temas.map { it.first }
        assertTrue(claves.contains("sistema"))
        assertTrue(claves.contains("oscuro"))
        assertTrue(claves.contains("claro"))
    }

    @Test
    fun opcionesDensidadLista_contieneTresNiveles() {
        val densidades = AlmacenAjustes.OPCIONES_DENSIDAD_LISTA
        assertEquals(3, densidades.size)
        val claves = densidades.map { it.first }
        assertTrue(claves.contains("predeterminada"))
        assertTrue(claves.contains("comoda"))
        assertTrue(claves.contains("compacta"))
    }

    @Test
    fun opcionesTileLongitud_contieneLongitudesSeguras() {
        val longitudes = AlmacenAjustes.OPCIONES_TILE_LONGITUD
        assertTrue(longitudes.isNotEmpty())
        val valores = longitudes.map { it.first }
        assertTrue(valores.contains(10))
        assertTrue(valores.contains(15))
        assertTrue(valores.contains(20))
        assertTrue(valores.contains(30))
    }

    @Test
    fun motoresCamara_contieneOpcionesValidas() {
        val motores = MotorCamara.entries
        assertTrue(motores.size >= 3)
        assertNotNull(motores.firstOrNull { it.clave == "auto" })
        assertNotNull(motores.firstOrNull { it.clave == "compatible" })
        assertNotNull(motores.firstOrNull { it.clave == "camerax" })
    }
}
