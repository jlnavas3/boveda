/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.DatosCriptoWallet
import com.jlnavas3.bovedalocal.data.DatosCuentaBancaria
import com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.DatosRedWifi
import com.jlnavas3.bovedalocal.data.DatosServidorSsh
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.ResolverSubtituloEntrada
import org.junit.Assert.assertEquals
import org.junit.Test

class ResolverSubtituloEntradaTest {

    @Test
    fun subtituloTarjetaBancaria() {
        val entrada = Entrada(
            id = "1",
            titulo = "Mi Tarjeta",
            tipo = TipoEntrada.TARJETA,
            datosTarjeta = DatosTarjetaBancaria(
                marca = "Visa",
                numero = "4532 0123 4567 8910",
                titular = "Jose Navas"
            )
        )
        val subtitulo = ResolverSubtituloEntrada.resolver(entrada)
        assertEquals("Visa · •••• 8910", subtitulo)
    }

    @Test
    fun subtituloRedWifi() {
        val entrada = Entrada(
            id = "1",
            titulo = "Wi-Fi Casa",
            tipo = TipoEntrada.WIFI,
            datosWifi = DatosRedWifi(
                ssid = "FibraOptica_5G",
                tipoSeguridad = "WPA3"
            )
        )
        val subtitulo = ResolverSubtituloEntrada.resolver(entrada)
        assertEquals("WPA3 · FibraOptica_5G", subtitulo)
    }

    @Test
    fun subtituloCuentaBancaria() {
        val entrada = Entrada(
            id = "1",
            titulo = "Nómina",
            tipo = TipoEntrada.CUENTA_BANCARIA,
            datosCuentaBancaria = DatosCuentaBancaria(
                banco = "Banco Pichincha",
                numeroCuenta = "2201984562"
            )
        )
        val subtitulo = ResolverSubtituloEntrada.resolver(entrada)
        assertEquals("Banco Pichincha · •••• 4562", subtitulo)
    }

    @Test
    fun subtituloDocumentoIdentidad() {
        val entrada = Entrada(
            id = "1",
            titulo = "Cédula",
            tipo = TipoEntrada.IDENTIDAD,
            datosIdentidad = DatosDocumentoIdentidad(
                tipoDocumento = "Cédula",
                numero = "1204620338",
                paisEmisor = "EC"
            )
        )
        val subtitulo = ResolverSubtituloEntrada.resolver(entrada)
        assertEquals("Cédula · 1204620338 (EC)", subtitulo)
    }

    @Test
    fun subtituloServidorSsh() {
        val entrada = Entrada(
            id = "1",
            titulo = "VPS Producción",
            tipo = TipoEntrada.SERVIDOR,
            datosServidor = DatosServidorSsh(
                host = "192.168.1.100",
                usuario = "root",
                puerto = "2222"
            )
        )
        val subtitulo = ResolverSubtituloEntrada.resolver(entrada)
        assertEquals("root@192.168.1.100:2222", subtitulo)
    }

    @Test
    fun subtituloCriptoWallet() {
        val entrada = Entrada(
            id = "1",
            titulo = "Ledger",
            tipo = TipoEntrada.WALLET,
            datosWallet = DatosCriptoWallet(
                red = "Ethereum",
                direccionPublica = "0x71C634C245ad2480E32102f7779b76179668976F"
            )
        )
        val subtitulo = ResolverSubtituloEntrada.resolver(entrada)
        assertEquals("Ethereum · 0x71C6...976F", subtitulo)
    }

    @Test
    fun subtituloPasskey() {
        val entrada = Entrada(
            id = "1",
            titulo = "PayPal",
            tipo = TipoEntrada.PASSKEY,
            usuario = "jolunavi",
            passkey = DatosPasskey(
                rpId = "paypal.com",
                rpName = "PayPal",
                userHandle = "123",
                credId = "abc",
                clavePrivada = "priv",
                usuario = "jolunavi"
            )
        )
        val subtitulo = ResolverSubtituloEntrada.resolver(entrada)
        assertEquals("jolunavi · paypal.com", subtitulo)
    }
}
