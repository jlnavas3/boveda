/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoAvisoInterno
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.CalculadorAvisosInternos
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculadorAvisosInternosTest {

    private fun crearEntrada(
        id: String,
        titulo: String,
        usuario: String = "user@test.com",
        urls: List<String> = listOf("https://test.com"),
        contrasena: String = "ClaveSegura123!",
        creadaEn: Long = 1_000_000L
    ): Entrada = Entrada(
        id = id,
        titulo = titulo,
        usuario = usuario,
        urls = urls,
        contrasena = contrasena,
        creadaEn = creadaEn,
        tipo = TipoEntrada.LOGIN
    )

    @Test
    fun deteccionTitulosComprometidos() {
        val tldsConfig = AjustesApp().tldsDescartables // contains "es", "co", "ar", "me", "ec", etc. without dots
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("1", "https://github.com/login"), tldsConfig))
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("2", "http://mibase.net"), tldsConfig))
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("3", "android://com.twitter.android"), tldsConfig))
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("4", "com.whatsapp"), tldsConfig))
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("5", "miempresa.com"), tldsConfig))
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("6", "banco.com.ec"), tldsConfig))
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("7", "192.168.1.1"), tldsConfig))
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("8", ""), tldsConfig))
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("9", "google.com (admin)"), tldsConfig))

        // Palabras legítimas en español que terminan en "es", "co", "ar", "me" NO deben considerarse comprometidas
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("10", "GitHub"), tldsConfig))
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("11", "Banco Pichincha"), tldsConfig))
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("12", "Banco"), tldsConfig)) // termina en "co"
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("13", "Ajustes"), tldsConfig)) // termina en "es"
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("14", "Mercedes"), tldsConfig)) // termina en "es"
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("15", "Redes"), tldsConfig)) // termina en "es"
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("16", "Celular"), tldsConfig)) // termina en "ar"
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("17", "Hogar"), tldsConfig)) // termina en "ar"
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("18", "Chrome"), tldsConfig)) // termina en "me"
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("19", "Google (admin)"), tldsConfig))
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("20", "Envato / CodeCanyon"), tldsConfig)) // marca con slash y espacios
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("21", "Max (HBO)"), tldsConfig))
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("22", "X (Twitter)"), tldsConfig))
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("23", "Trabajo: VPN"), tldsConfig)) // dos puntos con texto
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("26", "Localhost", urls = listOf("http://localhost")), tldsConfig)) // Entorno local con nombre limpio
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("27", "Localhost", urls = listOf("http://localhost:8080")), tldsConfig))
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("30", "Localhost :631", urls = listOf("http://localhost:631")), tldsConfig)) // Servicio local con puerto formateado
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("31", "Localhost :63342", urls = listOf("http://localhost:63342")), tldsConfig))
        assertFalse(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("32", "Localhost :631"), tldsConfig))
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("24", "codecanyon.net/item/test"), tldsConfig)) // URL con path sin espacios
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("25", "192.168.1.1:8080"), tldsConfig)) // puerto numérico
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("28", "http://localhost"), tldsConfig)) // URL sin protocolo limpio
        assertTrue(CalculadorAvisosInternos.esTituloComprometido(crearEntrada("29", "localhost:8080"), tldsConfig)) // host con puerto numérico
    }

    @Test
    fun calcularAvisos_detectaTitulosYDuplicados() {
        val entradas = listOf(
            crearEntrada("1", "https://banco.com", usuario = "admin", urls = listOf("https://banco.com")),
            crearEntrada("2", "https://banco.com", usuario = "admin", urls = listOf("https://banco.com")), // duplicado
            crearEntrada("3", "Netflix", usuario = "user@netflix.com")
        )
        val ajustes = AjustesApp()
        val avisos = CalculadorAvisosInternos.calcular(
            entradas = entradas,
            ajustes = ajustes,
            archivoBovedaModificado = System.currentTimeMillis()
        )

        val avisoTitulos = avisos.find { it.tipo == TipoAvisoInterno.TITULOS_SIN_NORMALIZAR }
        val avisoDuplicados = avisos.find { it.tipo == TipoAvisoInterno.DUPLICADOS_DETECTADOS }

        assertTrue(avisoTitulos != null)
        assertEquals(2, avisoTitulos?.cantidadAfectados)

        assertTrue(avisoDuplicados != null)
        assertEquals(1, avisoDuplicados?.cantidadAfectados)
    }

    @Test
    fun calcularAvisos_respaldoNoDisparaFalsaAlarmaEnBovedaRecienteConEntradasAntiguas() {
        val ahora = 1_700_000_000_000L
        val creacionBoveda = ahora - (2L * 24 * 60 * 60 * 1000) // creada hace 2 días
        val entradasAntiguas = listOf(
            crearEntrada("1", "Google", creadaEn = 1_000_000L), // creada hace 10 años
            crearEntrada("2", "Amazon", creadaEn = 2_000_000L)
        )
        val ajustes = AjustesApp().copy(
            recordatorioExportacionDias = 30 // Avisar a los 30 días
        ).actualizarAvisos { it.copy(fechaCreacionBovedaLocal = creacionBoveda) }

        val avisos = CalculadorAvisosInternos.calcular(
            entradas = entradasAntiguas,
            ajustes = ajustes,
            archivoBovedaModificado = creacionBoveda,
            ahora = ahora
        )

        val avisoRespaldo = avisos.find { it.tipo == TipoAvisoInterno.RESPALDO_PENDIENTE }
        // Debe ser null porque la bóveda local apenas tiene 2 días, a pesar de que las cuentas tienen 10 años
        assertEquals(null, avisoRespaldo)
    }

    @Test
    fun calcularAvisos_respetaAvisosIgnorados() {
        val entradas = listOf(
            crearEntrada("1", "https://miweb.com")
        )
        val ajustes = AjustesApp().actualizarAvisos {
            it.copy(avisosIgnorados = setOf(TipoAvisoInterno.TITULOS_SIN_NORMALIZAR.name))
        }

        val avisos = CalculadorAvisosInternos.calcular(
            entradas = entradas,
            ajustes = ajustes,
            archivoBovedaModificado = System.currentTimeMillis()
        )

        val avisoTitulos = avisos.find { it.tipo == TipoAvisoInterno.TITULOS_SIN_NORMALIZAR }
        assertEquals(null, avisoTitulos)
    }
}
