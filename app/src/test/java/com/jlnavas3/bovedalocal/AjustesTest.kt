package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AjustesTest {

    @Test
    fun `valores por defecto de AjustesApp son seguros y coherentes`() {
        val ajustes = AjustesApp()

        // Seguridad
        assertEquals(60, ajustes.autoBloqueoSegundos)
        assertFalse("La biometría debe estar desactivada por defecto", ajustes.biometriaActiva)
        assertEquals("", ajustes.biometriaModo)

        // Quick Settings Tile
        assertEquals("longitud", ajustes.tileModo)
        assertEquals(20, ajustes.tileLongitud)
        assertTrue(ajustes.tileCopiarPortapapeles)
        assertTrue(ajustes.tileMostrarToast)
        assertTrue(ajustes.tileHaptica)
        assertEquals("XXXXX-XXXXX-XXXXX-XXXXX", ajustes.tilePatron)

        // Formas y geometría
        assertEquals(16f, ajustes.curvaturaEsquinasDp, 0.01f)
        assertEquals(1.0f, ajustes.grosorBordeDp, 0.01f)
        assertEquals("acento", ajustes.estiloBorde)
        assertEquals(14f, ajustes.espaciadoComponentesDp, 0.01f)

        // Widget 2FA
        assertEquals(0f, ajustes.widgetGrosorBordeDp, 0.01f)
        assertEquals(0f, ajustes.widgetCurvaturaEsquinasDp, 0.01f)
        assertEquals(0.50f, ajustes.widgetTransparenciaFondo, 0.01f)

        // Historial de contraseñas generadas
        assertEquals(15, ajustes.historialClavesMax)
        assertTrue(ajustes.historialClavesVaciadoAuto)
        assertEquals(30 * 60 * 1000L, ajustes.historialClavesTiempoAutoDestruccion)
        assertTrue(ajustes.historialClaves.isEmpty())

        // Backup automático local
        assertEquals(0, ajustes.backupAutoFrecuenciaDias)
        assertEquals(5, ajustes.backupAutoMaxCopias)
        assertEquals("{99}-backup-{FECHA}", ajustes.backupAutoPatronNombre)
        assertEquals(0, ajustes.backupAutoSecuencia)

        // Tipografía
        assertEquals(1.0f, ajustes.escalaTexto, 0.01f)
        assertEquals("normal", ajustes.pesoTexto)
        assertFalse(ajustes.cursivaTexto)
        assertEquals(0.0f, ajustes.espaciadoLetrasSp, 0.01f)
        assertEquals(1.0f, ajustes.interlineadoFactor, 0.01f)
        assertEquals("sans", ajustes.familiaFuente)

        // Índice alfabético lateral
        assertTrue(ajustes.mostrarIndiceAlfabetico)
        assertTrue(ajustes.indiceEfectoOla)
        assertEquals(109f, ajustes.indiceAmplitudOlaDp, 0.01f)
        assertEquals(169f, ajustes.indiceRadioOlaDp, 0.01f)
        assertEquals(1.5f, ajustes.indiceEscalaLetras, 0.01f)
        assertTrue(ajustes.indiceMostrarCirculo)
        assertEquals(50f, ajustes.indiceTamanoCirculoDp, 0.01f)
        assertEquals(136f, ajustes.indiceOffsetCirculoDp, 0.01f)
        assertTrue(ajustes.indiceHaptica)
        assertEquals(45f, ajustes.indiceAnchoTactilDp, 0.01f)
        assertEquals(80f, ajustes.indiceTonoLetras, 0.01f)
        assertTrue(ajustes.indiceIncluirEnie)
        assertTrue(ajustes.indiceResaltarEntradas)
        assertTrue(ajustes.indiceResaltarSoloPrimera)
    }

    @Test
    fun `las opciones de auto-bloqueo estan dentro del rango seguro sin cero ni negativos`() {
        val segundos = AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.map { it.first }
        assertTrue(segundos.isNotEmpty())
        assertTrue("El bloqueo mínimo no debe ser inferior a 5 segundos", segundos.minOrNull()!! >= 5)
        assertTrue("El bloqueo máximo no debe exceder 300 segundos (5 min)", segundos.maxOrNull()!! <= 300)
        assertTrue(segundos.contains(30))
        assertTrue(segundos.contains(60))
    }

    @Test
    fun `las opciones de longitud del tile son validas`() {
        val longitudes = AlmacenAjustes.OPCIONES_TILE_LONGITUD.map { it.first }
        assertEquals(listOf(10, 15, 20, 30), longitudes)
    }

    @Test
    fun `la modificacion por copia conserva inmutabilidad`() {
        val inicial = AjustesApp()
        val modificado = inicial.copy(autoBloqueoSegundos = 120, tileModo = "patron", tilePatron = "9999")

        assertEquals(60, inicial.autoBloqueoSegundos)
        assertEquals("longitud", inicial.tileModo)

        assertEquals(120, modificado.autoBloqueoSegundos)
        assertEquals("patron", modificado.tileModo)
        assertEquals("9999", modificado.tilePatron)
    }

    @Test
    fun `recordatorio opciones y patron backup por defecto`() {
        val ajustes = AjustesApp()
        assertEquals("{99}-backup-{FECHA}", ajustes.backupAutoPatronNombre)
        assertTrue(AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION.any { it.first == -30 })
    }

    @Test
    fun `opciones y valor por defecto de animacion de desbloqueo`() {
        val ajustes = AjustesApp()
        assertEquals("engranajes", ajustes.animacionDesbloqueo)
        val opciones = AlmacenAjustes.OPCIONES_ANIMACION_DESBLOQUEO.map { it.first }
        assertTrue(opciones.contains("puerta"))
        assertTrue(opciones.contains("engranajes"))
    }

    @Test
    fun `mostrarIdsAjustes esta desactivado por defecto y se puede alternar`() {
        val ajustes = AjustesApp()
        assertFalse("Los IDs deben estar ocultos por defecto", ajustes.mostrarIdsAjustes)

        val activado = ajustes.copy(mostrarIdsAjustes = true)
        assertTrue(activado.mostrarIdsAjustes)
    }
}
