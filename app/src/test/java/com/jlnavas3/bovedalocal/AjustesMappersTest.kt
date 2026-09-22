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
        assertTrue(dias.contains(7)) // Cada 7 días
        assertTrue(dias.contains(30))
        assertTrue(dias.contains(60))
        assertTrue(dias.contains(90))
    }

    @Test
    fun opcionesMaxCopiasBackupAuto_contieneValoresEsperados() {
        val opciones = AlmacenAjustes.OPCIONES_MAX_COPIAS_BACKUP_AUTO
        val valores = opciones.map { it.first }
        assertEquals(listOf(5, 10, 20, 100, 0), valores)
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

    @Test
    fun widget1x1Ajustes_valoresPorDefectoSonCorrectos() {
        val ajustes = com.jlnavas3.bovedalocal.data.AjustesApp()
        assertEquals(0f, ajustes.widget1x1GrosorBordeDp)
        assertEquals(15f, ajustes.widget1x1CurvaturaEsquinasDp)
        assertEquals(1.0f, ajustes.widget1x1TransparenciaFondo)
        assertEquals(55f, ajustes.widget1x1TamanoDp)
        assertEquals(55f, ajustes.widget1x1AnchoDp)
        assertEquals(51f, ajustes.widget1x1AltoDp)
        assertEquals(false, ajustes.widget1x1BloquearProporcion)
        assertEquals(0f, ajustes.widget1x1OffsetX)
        assertEquals(4f, ajustes.widget1x1OffsetY)
        assertEquals("arriba", ajustes.widget1x1Alineamiento)
        assertEquals("#33332E", ajustes.widget1x1ColorBorde)
        assertEquals("#E6FCFF", ajustes.widget1x1ColorIcono)
        assertEquals("#2E3333", ajustes.widget1x1ColorFondo)
        assertEquals("aleatoria", ajustes.widget1x1Modo)
    }

    @Test
    fun passwordGenerator_simbolosPorDefectoValidos() {
        val simbolos = com.jlnavas3.bovedalocal.crypto.PasswordGenerator.SIMBOLOS
        assertTrue(simbolos.isNotEmpty())
        assertTrue(simbolos.contains("#"))
        assertTrue(simbolos.contains("$"))
        assertTrue(simbolos.contains("!"))
    }
}

