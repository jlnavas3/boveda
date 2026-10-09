package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.util.DetectorServiciosAccesibilidad
import com.jlnavas3.bovedalocal.util.DiagnosticoAccesibilidad
import com.jlnavas3.bovedalocal.util.ServicioAccesibilidadDetectado
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DetectorServiciosAccesibilidadTest {

    @Test
    fun `valor por defecto de proteccion tapjacking esta activo`() {
        assertTrue(AjustesDefaults.Seguridad.PROTECCION_TAPJACKING)
    }

    @Test
    fun `diagnostico seguro cuando no hay servicios sospechosos`() {
        val diag = DiagnosticoAccesibilidad(
            totalActivos = 1,
            servicios = listOf(
                ServicioAccesibilidadDetectado(
                    nombrePaquete = "com.google.android.marvin.talkback",
                    etiquetaApp = "TalkBack",
                    esAppSistema = true,
                    esSospechoso = false
                )
            ),
            serviciosSospechosos = emptyList(),
            esSeguro = true
        )

        assertTrue(diag.esSeguro)
        assertEquals(1, diag.totalActivos)
        assertEquals(0, diag.serviciosSospechosos.size)
    }

    @Test
    fun `diagnostico inseguro cuando se detecta app de terceros con accesibilidad`() {
        val servicioSospechoso = ServicioAccesibilidadDetectado(
            nombrePaquete = "com.app.dudosa",
            etiquetaApp = "App Espía",
            esAppSistema = false,
            esSospechoso = true
        )
        val diag = DiagnosticoAccesibilidad(
            totalActivos = 1,
            servicios = listOf(servicioSospechoso),
            serviciosSospechosos = listOf(servicioSospechoso),
            esSeguro = false
        )

        assertFalse(diag.esSeguro)
        assertEquals(1, diag.totalActivos)
        assertEquals(1, diag.serviciosSospechosos.size)
        assertEquals("App Espía", diag.serviciosSospechosos.first().etiquetaApp)
    }

    @Test
    fun `lista blanca vacia por defecto en ajustes defaults`() {
        assertTrue(AjustesDefaults.Seguridad.LISTA_BLANCA_ACCESIBILIDAD.isEmpty())
    }

    @Test
    fun `app sospechosa en lista blanca no es considerada sospechosa y diagnostico es seguro`() {
        val servicioEnListaBlanca = ServicioAccesibilidadDetectado(
            nombrePaquete = "com.cube.recorder",
            etiquetaApp = "Cube ACR App Connector",
            esAppSistema = false,
            esSospechoso = false,
            esEnListaBlanca = true
        )
        val diag = DiagnosticoAccesibilidad(
            totalActivos = 1,
            servicios = listOf(servicioEnListaBlanca),
            serviciosSospechosos = emptyList(),
            esSeguro = true
        )

        assertTrue(diag.esSeguro)
        assertTrue(servicioEnListaBlanca.esEnListaBlanca)
        assertFalse(servicioEnListaBlanca.esSospechoso)
        assertEquals(0, diag.serviciosSospechosos.size)
    }

    @Test
    fun `servicio del sistema inactivo se marca como del sistema y no sospechoso`() {
        val servicioSistemaInactivo = ServicioAccesibilidadDetectado(
            nombrePaquete = "com.google.android.marvin.talkback",
            etiquetaApp = "TalkBack",
            esAppSistema = true,
            estaActivo = false,
            esSospechoso = false
        )

        assertFalse(servicioSistemaInactivo.estaActivo)
        assertTrue(servicioSistemaInactivo.esAppSistema)
        assertFalse(servicioSistemaInactivo.esSospechoso)
    }
}
