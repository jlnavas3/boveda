/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AnalizadorDiscrepanciasUnificacion
import com.jlnavas3.bovedalocal.data.DatosRedWifi
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.FusionadorEntradas
import com.jlnavas3.bovedalocal.data.TipoEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FusionadorSubmodelosNativosTest {

    @Test
    fun `unificar entradas wifi conserva tipo WIFI y resguarda clave secundaria en notas e historial`() {
        val wifiPrincipal = Entrada(
            id = "w1",
            titulo = "Wi-Fi Principal",
            tipo = TipoEntrada.WIFI,
            contrasena = "clave123",
            datosWifi = DatosRedWifi(ssid = "SPIDER", contrasena = "clave123")
        )
        val wifiSecundaria = Entrada(
            id = "w2",
            titulo = "Wi-Fi Casa Amigo",
            tipo = TipoEntrada.WIFI,
            contrasena = "clave456",
            datosWifi = DatosRedWifi(ssid = "SPIDER", contrasena = "clave456")
        )

        val unificada = FusionadorEntradas.fusionar(wifiPrincipal, listOf(wifiSecundaria))

        // No debe degradarse a LOGIN
        assertEquals(TipoEntrada.WIFI, unificada.tipo)
        assertEquals("SPIDER", unificada.datosWifi?.ssid)
        assertEquals("clave123", unificada.datosWifi?.contrasena)

        // La clave de la secundaria debe estar respaldada en el historial de contraseñas
        assertTrue(unificada.historialContrasenas.any { it.contrasena == "clave456" })

        // Y en las notas de respaldo
        assertTrue(unificada.notas.contains("clave456"))
    }

    @Test
    fun `unificar tarjetas enriquece datos faltantes sin sobreescribir existentes`() {
        val tarjetaPrincipal = Entrada(
            id = "t1",
            titulo = "Visa",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4532111122223333", titular = "Juan Pérez", vencimiento = "", cvv = "")
        )
        val tarjetaSecundaria = Entrada(
            id = "t2",
            titulo = "Visa Respaldo",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4532111122223333", titular = "Juan", vencimiento = "08/28", cvv = "789", pin = "1234")
        )

        val unificada = FusionadorEntradas.fusionar(tarjetaPrincipal, listOf(tarjetaSecundaria))

        assertEquals(TipoEntrada.TARJETA, unificada.tipo)
        assertEquals("Juan Pérez", unificada.datosTarjeta?.titular) // Conservó el titular existente
        assertEquals("4532111122223333", unificada.datosTarjeta?.numero)
        assertEquals("08/28", unificada.datosTarjeta?.vencimiento) // Enriqueció vencimiento faltante
        assertEquals("789", unificada.datosTarjeta?.cvv) // Enriqueció CVV faltante
        assertEquals("1234", unificada.datosTarjeta?.pin) // Enriqueció PIN faltante
    }

    @Test
    fun `unificar tarjetas con números distintos archiva los datos de la secundaria en notas`() {
        val tarjeta1 = Entrada(
            id = "t1",
            titulo = "Tarjeta A",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4111111111111111", vencimiento = "01/26", cvv = "111")
        )
        val tarjeta2 = Entrada(
            id = "t2",
            titulo = "Tarjeta B",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4222222222222222", vencimiento = "02/27", cvv = "222")
        )

        val unificada = FusionadorEntradas.fusionar(tarjeta1, listOf(tarjeta2))

        assertEquals(TipoEntrada.TARJETA, unificada.tipo)
        assertEquals("4111111111111111", unificada.datosTarjeta?.numero)
        assertTrue(unificada.notas.contains("4222222222222222"))
        assertTrue(unificada.notas.contains("02/27"))
        assertTrue(unificada.notas.contains("222"))
    }

    @Test
    fun `analizador de discrepancias detecta tipos diferentes y datos de submodelos distintos`() {
        val wifi = Entrada(
            id = "w1",
            titulo = "Wi-Fi",
            tipo = TipoEntrada.WIFI,
            datosWifi = DatosRedWifi(ssid = "MiRed", contrasena = "clave1")
        )
        val tarjeta = Entrada(
            id = "t1",
            titulo = "Tarjeta",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4111111111111111")
        )

        val disc = AnalizadorDiscrepanciasUnificacion.analizar(listOf(wifi, tarjeta), wifi)
        assertTrue(disc.hayTiposDiferentes)
        assertTrue(disc.tieneAdvertenciasCriticas)
    }
}
