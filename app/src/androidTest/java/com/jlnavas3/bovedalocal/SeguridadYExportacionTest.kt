package com.jlnavas3.bovedalocal

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SeguridadYExportacionTest {

    @Test
    fun la_lista_local_detecta_contrasenas_muy_comunes() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext

        assertTrue(ContrasenasComunes.esComun(contexto, "password"))
        assertTrue(ContrasenasComunes.esComun(contexto, "welcome1"))
        assertTrue(ContrasenasComunes.esComun(contexto, "Qwerty123"))
        assertFalse(ContrasenasComunes.esComun(contexto, "9jB$7fV!nR^kQ2s"))
    }

    @Test
    fun el_recordatorio_de_exportacion_persiste_en_ajustes() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val prefs = contexto.getSharedPreferences("ajustes_pepo_boveda", android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().commit()

        val almacen = AlmacenAjustes(contexto)
        almacen.actualizar { it.copy(recordatorioExportacionDias = 60, ultimaExportacionEn = 1700000000000L) }

        val reLeido = AlmacenAjustes(contexto)
        assertEquals(60, reLeido.actual.recordatorioExportacionDias)
        assertEquals(1700000000000L, reLeido.actual.ultimaExportacionEn)
    }

    @Test
    fun la_densidad_de_la_lista_persiste_en_ajustes() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val prefs = contexto.getSharedPreferences("ajustes_pepo_boveda", android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().commit()

        val almacen = AlmacenAjustes(contexto)
        almacen.actualizar { it.copy(densidadLista = "compacta") }

        val reLeido = AlmacenAjustes(contexto)
        assertEquals("compacta", reLeido.actual.densidadLista)
    }
}
