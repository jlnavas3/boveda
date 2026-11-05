/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.DatosCuentaBancaria
import com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad
import com.jlnavas3.bovedalocal.data.DatosRedWifi
import com.jlnavas3.bovedalocal.data.DatosServidorSsh
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.data.DatosCriptoWallet
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.data.TipoEntrada
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalizadorDuplicadosNativosTest {

    @Test
    fun `redes wifi con mismo ssid e idéntica clave se agrupan como IDENTICO`() {
        val wifi1 = Entrada(
            id = "w1",
            titulo = "Mi Wi-Fi",
            tipo = TipoEntrada.WIFI,
            datosWifi = DatosRedWifi(ssid = "SPIDER", contrasena = "12345")
        )
        val wifi2 = Entrada(
            id = "w2",
            titulo = "Wi-Fi Casa",
            tipo = TipoEntrada.WIFI,
            datosWifi = DatosRedWifi(ssid = "spider", contrasena = "12345")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(wifi1, wifi2))
        assertEquals(1, grupos.size)
        assertEquals(TipoDuplicado.IDENTICO, grupos.first().tipo)
        assertEquals(2, grupos.first().entradas.size)
        assertEquals("Wi-Fi · SPIDER", grupos.first().claveVisual)
    }

    @Test
    fun `redes wifi con mismo ssid pero distinta clave se agrupan como MISMO_IDENTIFICADOR_DISTINTA_CLAVE`() {
        val wifiPropia = Entrada(
            id = "w1",
            titulo = "Mi Wi-Fi",
            tipo = TipoEntrada.WIFI,
            datosWifi = DatosRedWifi(ssid = "SPIDER", contrasena = "12345")
        )
        val wifiAmigo = Entrada(
            id = "w2",
            titulo = "Wi-Fi Amigo",
            tipo = TipoEntrada.WIFI,
            datosWifi = DatosRedWifi(ssid = "spider", contrasena = "67890")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(wifiPropia, wifiAmigo))
        assertEquals(1, grupos.size)
        assertEquals(TipoDuplicado.MISMO_IDENTIFICADOR_DISTINTA_CLAVE, grupos.first().tipo)
        assertEquals(2, grupos.first().entradas.size)
    }

    @Test
    fun `redes wifi con distinto ssid no se agrupan aunque tengan el mismo titulo o clave`() {
        val wifi1 = Entrada(
            id = "w1",
            titulo = "Wi-Fi",
            tipo = TipoEntrada.WIFI,
            datosWifi = DatosRedWifi(ssid = "FibraCasa", contrasena = "clave123")
        )
        val wifi2 = Entrada(
            id = "w2",
            titulo = "Wi-Fi",
            tipo = TipoEntrada.WIFI,
            datosWifi = DatosRedWifi(ssid = "FibraOficina", contrasena = "clave123")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(wifi1, wifi2))
        assertTrue(grupos.isEmpty())
    }

    @Test
    fun `tarjetas con mismo numero se agrupan aunque los titulos difieran`() {
        val t1 = Entrada(
            id = "t1",
            titulo = "Visa Pichincha",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4532 0123 4567 8910", vencimiento = "05/27", cvv = "123")
        )
        val t2 = Entrada(
            id = "t2",
            titulo = "Mi Tarjeta Principal",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4532-0123-4567-8910", vencimiento = "05/27", cvv = "123")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(t1, t2))
        assertEquals(1, grupos.size)
        assertEquals(TipoDuplicado.IDENTICO, grupos.first().tipo)
    }

    @Test
    fun `tarjetas con mismo numero pero fecha o cvv renovado se agrupan con advertencia de secreto distinto`() {
        val t1 = Entrada(
            id = "t1",
            titulo = "Visa Antigua",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4532 0123 4567 8910", vencimiento = "05/24", cvv = "123")
        )
        val t2 = Entrada(
            id = "t2",
            titulo = "Visa Nueva",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4532 0123 4567 8910", vencimiento = "05/28", cvv = "987")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(t1, t2))
        assertEquals(1, grupos.size)
        assertEquals(TipoDuplicado.MISMO_IDENTIFICADOR_DISTINTA_CLAVE, grupos.first().tipo)
    }

    @Test
    fun `tarjetas con distinto numero nunca se agrupan aunque compartan el titulo Visa`() {
        val t1 = Entrada(
            id = "t1",
            titulo = "Visa",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4111 1111 1111 1111")
        )
        val t2 = Entrada(
            id = "t2",
            titulo = "Visa",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(numero = "4222 2222 2222 2222")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(t1, t2))
        assertTrue("No deben agruparse tarjetas con números distintos", grupos.isEmpty())
    }

    @Test
    fun `cuentas bancarias con distinto numero no se agrupan aunque sean del mismo banco`() {
        val c1 = Entrada(
            id = "c1",
            titulo = "Banco Pichincha",
            tipo = TipoEntrada.CUENTA_BANCARIA,
            datosCuentaBancaria = DatosCuentaBancaria(banco = "Pichincha", numeroCuenta = "1234567890")
        )
        val c2 = Entrada(
            id = "c2",
            titulo = "Banco Pichincha",
            tipo = TipoEntrada.CUENTA_BANCARIA,
            datosCuentaBancaria = DatosCuentaBancaria(banco = "Pichincha", numeroCuenta = "9876543210")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(c1, c2))
        assertTrue(grupos.isEmpty())
    }

    @Test
    fun `documentos con mismo numero y tipo se agrupan`() {
        val d1 = Entrada(
            id = "d1",
            titulo = "Cédula",
            tipo = TipoEntrada.IDENTIDAD,
            datosIdentidad = DatosDocumentoIdentidad(tipoDocumento = "CI", numero = "1204620338")
        )
        val d2 = Entrada(
            id = "d2",
            titulo = "Mi Cédula Ecuador",
            tipo = TipoEntrada.IDENTIDAD,
            datosIdentidad = DatosDocumentoIdentidad(tipoDocumento = "ci", numero = "1204620338")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(d1, d2))
        assertEquals(1, grupos.size)
        assertEquals(TipoDuplicado.IDENTICO, grupos.first().tipo)
    }

    @Test
    fun `servidores ssh con mismo host puerto y usuario se detectan`() {
        val s1 = Entrada(
            id = "s1",
            titulo = "VPS",
            tipo = TipoEntrada.SERVIDOR,
            datosServidor = DatosServidorSsh(host = "192.168.1.100", puerto = "22", usuario = "root"),
            contrasena = "pass1"
        )
        val s2 = Entrada(
            id = "s2",
            titulo = "Servidor Local",
            tipo = TipoEntrada.SERVIDOR,
            datosServidor = DatosServidorSsh(host = "192.168.1.100", puerto = "22", usuario = "root"),
            contrasena = "pass2"
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(s1, s2))
        assertEquals(1, grupos.size)
        assertEquals(TipoDuplicado.MISMO_IDENTIFICADOR_DISTINTA_CLAVE, grupos.first().tipo)
    }

    @Test
    fun `crypto wallets con misma direccion y red se detectan`() {
        val w1 = Entrada(
            id = "w1",
            titulo = "Metamask",
            tipo = TipoEntrada.WALLET,
            datosWallet = DatosCriptoWallet(red = "Ethereum", direccionPublica = "0x71C663328")
        )
        val w2 = Entrada(
            id = "w2",
            titulo = "Billetera ETH",
            tipo = TipoEntrada.WALLET,
            datosWallet = DatosCriptoWallet(red = "ethereum", direccionPublica = "0x71c663328")
        )

        val grupos = AnalizadorDuplicados.analizar(listOf(w1, w2))
        assertEquals(1, grupos.size)
        assertEquals(TipoDuplicado.IDENTICO, grupos.first().tipo)
    }
}
