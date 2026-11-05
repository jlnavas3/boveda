/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.DatosCuentaBancaria
import com.jlnavas3.bovedalocal.data.DatosDocumentoIdentidad
import com.jlnavas3.bovedalocal.data.DatosRedWifi
import com.jlnavas3.bovedalocal.data.DatosServidorSsh
import com.jlnavas3.bovedalocal.data.DatosTarjetaBancaria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.MigradorEntradasNativas
import com.jlnavas3.bovedalocal.data.PresetsCampos
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MigradorEntradasNativasTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun migrarEntradaTarjetaHeredadaExtraeDatosYConservaCamposExtra() {
        val camposOriginales = PresetsCampos.tarjeta().map { campo ->
            when (campo.etiqueta) {
                "Titular" -> campo.copy(valor = "Juan Perez")
                "Número de tarjeta" -> campo.copy(valor = "4532 1234 5678 9010")
                "Vencimiento" -> campo.copy(valor = "12/28")
                "CVV" -> campo.copy(valor = "789")
                "PIN de tarjeta" -> campo.copy(valor = "1234")
                else -> campo
            }
        }.toMutableList()

        // Añadir un campo personalizado extra deliberado por el usuario
        val campoExtra = CampoPersonalizado(
            etiqueta = "Límite de crédito mensual",
            valor = "5000 USD",
            tipo = TipoCampo.TEXTO
        )
        camposOriginales.add(campoExtra)

        val entradaHeredada = Entrada(
            id = "tarjeta-1",
            tipo = TipoEntrada.TARJETA,
            titulo = "Visa Infinite",
            camposPersonalizados = camposOriginales
        )

        val tarjeta = entradaHeredada.tarjetaEfectiva
        assertNotNull(tarjeta)
        assertEquals("Juan Perez", tarjeta?.titular)
        assertEquals("4532 1234 5678 9010", tarjeta?.numero)
        assertEquals("12/28", tarjeta?.vencimiento)
        assertEquals("789", tarjeta?.cvv)
        assertEquals("1234", tarjeta?.pin)

        // Al ejecutar migrarEntrada(), los campos base se extraen y solo queda el campo extra
        val migrada = MigradorEntradasNativas.migrarEntrada(entradaHeredada)
        assertNotNull(migrada.datosTarjeta)
        assertEquals("Juan Perez", migrada.datosTarjeta?.titular)
        assertEquals(1, migrada.camposPersonalizados.size)
        assertEquals("Límite de crédito mensual", migrada.camposPersonalizados.first().etiqueta)
        assertEquals("5000 USD", migrada.camposPersonalizados.first().valor)
    }

    @Test
    fun migrarEntradaWifiHeredadaExtraeSsidYClave() {
        val camposWifi = PresetsCampos.wifi().map { campo ->
            when (campo.etiqueta) {
                "Nombre de red (SSID)" -> campo.copy(valor = "MiFibra_5G")
                "Contraseña Wi-Fi" -> campo.copy(valor = "SecretoSuperSeguro")
                "Tipo de seguridad" -> campo.copy(valor = "WPA2")
                else -> campo
            }
        }

        val entradaHeredada = Entrada(
            id = "wifi-1",
            tipo = TipoEntrada.WIFI,
            titulo = "Wi-Fi Casa",
            camposPersonalizados = camposWifi
        )

        val wifi = entradaHeredada.wifiEfectivo
        assertNotNull(wifi)
        assertEquals("MiFibra_5G", wifi?.ssid)
        assertEquals("SecretoSuperSeguro", wifi?.contrasena)
        assertEquals("WPA2", wifi?.tipoSeguridad)

        val migrada = MigradorEntradasNativas.migrarEntrada(entradaHeredada)
        assertEquals("MiFibra_5G", migrada.datosWifi?.ssid)
        assertTrue(migrada.camposPersonalizados.isEmpty())
    }

    @Test
    fun migrarEntradaIdentidadHeredadaExtraeDocumento() {
        val camposIdentidad = PresetsCampos.identidad().map { campo ->
            when (campo.etiqueta) {
                "Tipo de documento" -> campo.copy(valor = "DNI")
                "Número de documento" -> campo.copy(valor = "00123456X")
                "Fecha de expedición" -> campo.copy(valor = "01/01/2020")
                "Fecha de caducidad" -> campo.copy(valor = "01/01/2030")
                "País emisor" -> campo.copy(valor = "España")
                else -> campo
            }
        }

        val entradaHeredada = Entrada(
            id = "doc-1",
            tipo = TipoEntrada.IDENTIDAD,
            titulo = "DNI Nacional",
            usuario = "Juan Perez",
            camposPersonalizados = camposIdentidad
        )

        val doc = entradaHeredada.identidadEfectiva
        assertNotNull(doc)
        assertEquals("DNI", doc?.tipoDocumento)
        assertEquals("00123456X", doc?.numero)
        assertEquals("España", doc?.paisEmisor)
        assertEquals("Juan Perez", doc?.nombreCompleto)
    }

    @Test
    fun serializacionJsonEntradaConSubmodelosNativosEsCompatible() {
        val entradaOriginal = Entrada(
            id = "tarjeta-nativa",
            tipo = TipoEntrada.TARJETA,
            titulo = "Mastercard Gold",
            datosTarjeta = DatosTarjetaBancaria(
                titular = "Carlos Ruiz",
                numero = "5412 8888 9999 1111",
                vencimiento = "10/29",
                cvv = "123",
                pin = "9988"
            )
        )

        val jsonStr = json.encodeToString(Entrada.serializer(), entradaOriginal)
        val deserializada = json.decodeFromString(Entrada.serializer(), jsonStr)

        assertEquals("Mastercard Gold", deserializada.titulo)
        assertEquals("Carlos Ruiz", deserializada.datosTarjeta?.titular)
        assertEquals("5412 8888 9999 1111", deserializada.datosTarjeta?.numero)
        assertEquals("10/29", deserializada.datosTarjeta?.vencimiento)
        assertEquals("123", deserializada.datosTarjeta?.cvv)
        assertEquals("9988", deserializada.datosTarjeta?.pin)
    }

    @Test
    fun jsonHeredadoSinSubmodelosSeDeserializaCorrectamenteConNulls() {
        val jsonLegacy = """
            {
                "id": "legacy-1",
                "tipo": "WIFI",
                "titulo": "Red Oficina",
                "camposPersonalizados": [
                    {
                        "id": "c1",
                        "etiqueta": "Nombre de red (SSID)",
                        "valor": "Oficina_WiFi",
                        "tipo": "TEXTO",
                        "esSensible": false
                    },
                    {
                        "id": "c2",
                        "etiqueta": "Contraseña Wi-Fi",
                        "valor": "Password123",
                        "tipo": "TEXTO",
                        "esSensible": true
                    }
                ]
            }
        """.trimIndent()

        val deserializada = json.decodeFromString(Entrada.serializer(), jsonLegacy)
        assertEquals("legacy-1", deserializada.id)
        assertEquals(null, deserializada.datosWifi) // Es null en JSON puro
        // Pero wifiEfectivo extrae automáticamente:
        val efectivo = deserializada.wifiEfectivo
        assertNotNull(efectivo)
        assertEquals("Oficina_WiFi", efectivo?.ssid)
        assertEquals("Password123", efectivo?.contrasena)
    }

    @Test
    fun migrarEntradaCuentaBancariaHeredadaExtraeDatos() {
        val campos = PresetsCampos.cuentaBancaria().map { c ->
            when (c.etiqueta) {
                "Banco / Entidad" -> c.copy(valor = "Banco Santander")
                "Titular" -> c.copy(valor = "Maria Gomez")
                "Número de cuenta / IBAN" -> c.copy(valor = "ES91 2100 0418 4502 0005 1234")
                "CBU / CLABE / SWIFT" -> c.copy(valor = "SANESMMXXX")
                else -> c
            }
        }
        val entrada = Entrada(id = "cta-1", tipo = TipoEntrada.CUENTA_BANCARIA, titulo = "Cuenta Santander", camposPersonalizados = campos)
        val cta = entrada.cuentaBancariaEfectiva
        assertNotNull(cta)
        assertEquals("Banco Santander", cta?.banco)
        assertEquals("Maria Gomez", cta?.titular)
        assertEquals("ES91 2100 0418 4502 0005 1234", cta?.numeroCuenta)
        assertEquals("SANESMMXXX", cta?.swiftBic)

        val migrada = MigradorEntradasNativas.migrarEntrada(entrada)
        assertEquals("Banco Santander", migrada.datosCuentaBancaria?.banco)
        assertTrue(migrada.camposPersonalizados.isEmpty())
    }

    @Test
    fun migrarEntradaServidorHeredadaExtraeDatos() {
        val campos = PresetsCampos.servidorSsh().map { c ->
            when (c.etiqueta) {
                "Host o IP" -> c.copy(valor = "192.168.1.100")
                "Puerto" -> c.copy(valor = "2222")
                "Usuario SSH" -> c.copy(valor = "admin")
                "Clave privada / Contraseña" -> c.copy(valor = "-----BEGIN RSA PRIVATE KEY-----")
                else -> c
            }
        }
        val entrada = Entrada(id = "srv-1", tipo = TipoEntrada.SERVIDOR, titulo = "Servidor Local", camposPersonalizados = campos)
        val srv = entrada.servidorEfectivo
        assertNotNull(srv)
        assertEquals("192.168.1.100", srv?.host)
        assertEquals("2222", srv?.puerto)
        assertEquals("admin", srv?.usuario)
        assertEquals("-----BEGIN RSA PRIVATE KEY-----", srv?.clavePrivada)

        val migrada = MigradorEntradasNativas.migrarEntrada(entrada)
        assertEquals("192.168.1.100", migrada.datosServidor?.host)
        assertTrue(migrada.camposPersonalizados.isEmpty())
    }

    @Test
    fun migrarEntradaWalletHeredadaExtraeDatos() {
        val campos = PresetsCampos.criptoWallet().map { c ->
            when (c.etiqueta) {
                "Red / Blockchain" -> c.copy(valor = "Ethereum")
                "Dirección pública" -> c.copy(valor = "0x71C...8976F")
                "Frase semilla (Seed phrase)" -> c.copy(valor = "apple banana orange grape lemon lime melon mango cherry pear peach plum")
                "Clave privada" -> c.copy(valor = "0xprivkey123")
                else -> c
            }
        }
        val entrada = Entrada(id = "wal-1", tipo = TipoEntrada.WALLET, titulo = "Metamask Main", camposPersonalizados = campos)
        val wal = entrada.walletEfectiva
        assertNotNull(wal)
        assertEquals("Ethereum", wal?.red)
        assertEquals("0x71C...8976F", wal?.direccionPublica)
        assertEquals("apple banana orange grape lemon lime melon mango cherry pear peach plum", wal?.fraseSemilla)
        assertEquals("0xprivkey123", wal?.clavePrivada)

        val migrada = MigradorEntradasNativas.migrarEntrada(entrada)
        assertEquals("Ethereum", migrada.datosWallet?.red)
        assertTrue(migrada.camposPersonalizados.isEmpty())
    }

    @Test
    fun migrarEntradaIdentidadConExpedicionYCaducidadCortaPurgaCamposPersonalizados() {
        val campos = listOf(
            CampoPersonalizado(etiqueta = "Tipo de documento", valor = "CI", tipo = TipoCampo.TEXTO),
            CampoPersonalizado(etiqueta = "Número de documento", valor = "1204620338", tipo = TipoCampo.TEXTO),
            CampoPersonalizado(etiqueta = "Titular", valor = "José Luis Navas", tipo = TipoCampo.TEXTO),
            CampoPersonalizado(etiqueta = "Expedición", valor = "30/01/2025", tipo = TipoCampo.FECHA),
            CampoPersonalizado(etiqueta = "Caducidad", valor = "30/10/2030", tipo = TipoCampo.FECHA),
            CampoPersonalizado(etiqueta = "País emisor", valor = "Ecuador", tipo = TipoCampo.TEXTO)
        )

        val entrada = Entrada(
            id = "ci-1",
            tipo = TipoEntrada.IDENTIDAD,
            titulo = "Cédula",
            camposPersonalizados = campos
        )

        val iden = entrada.identidadEfectiva
        assertNotNull(iden)
        assertEquals("1204620338", iden?.numero)
        assertEquals("José Luis Navas", iden?.nombreCompleto)
        assertEquals("30/01/2025", iden?.expedicion)
        assertEquals("30/10/2030", iden?.caducidad)
        assertEquals("Ecuador", iden?.paisEmisor)

        val migrada = MigradorEntradasNativas.migrarEntrada(entrada)
        assertTrue("Los campos base deben quedar totalmente purgados de camposPersonalizados", migrada.camposPersonalizados.isEmpty())
        assertTrue("camposPersonalizadosEfectivos debe estar vacío", entrada.camposPersonalizadosEfectivos.isEmpty())
    }

    @Test
    fun presetsCamposParaTipoEntradaNativaRetornaListaVacia() {
        assertTrue(PresetsCampos.paraTipoEntrada(TipoEntrada.TARJETA).isEmpty())
        assertTrue(PresetsCampos.paraTipoEntrada(TipoEntrada.WIFI).isEmpty())
        assertTrue(PresetsCampos.paraTipoEntrada(TipoEntrada.CUENTA_BANCARIA).isEmpty())
        assertTrue(PresetsCampos.paraTipoEntrada(TipoEntrada.IDENTIDAD).isEmpty())
        assertTrue(PresetsCampos.paraTipoEntrada(TipoEntrada.SERVIDOR).isEmpty())
        assertTrue(PresetsCampos.paraTipoEntrada(TipoEntrada.WALLET).isEmpty())
    }
}
