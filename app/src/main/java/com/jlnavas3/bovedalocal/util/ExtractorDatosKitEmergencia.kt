/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase

object ExtractorDatosKitEmergencia {

    fun filtrarEntradas(entradas: List<Entrada>, opciones: GeneradorKitEmergencia.OpcionesKit): List<Entrada> {
        return if (opciones.soloFavoritos) entradas.filter { it.favorito } else entradas
    }

    fun extraerDatosEntrada(e: Entrada, incluirSecretos: Boolean): GeneradorKitEmergencia.DatosKitEntrada {
        val campos = e.camposPersonalizados
        val etiquetasUsadas = mutableSetOf<String>()

        fun valor(clave: String): String {
            val v = GestorCamposBase.valorDeCampo(campos, clave)
            if (v.isNotBlank()) etiquetasUsadas.add(clave.lowercase())
            return v
        }

        var iden = ""
        var sec = ""

        when (e.tipo) {
            TipoEntrada.WIFI -> {
                val wifi = e.wifiEfectivo
                val ssid = wifi?.ssid?.ifBlank { valor("Nombre de red (SSID)").ifBlank { valor("SSID") } } ?: valor("Nombre de red (SSID)").ifBlank { valor("SSID") }
                val seguridad = wifi?.tipoSeguridad?.ifBlank { valor("Tipo de seguridad").ifBlank { valor("Seguridad") } } ?: valor("Tipo de seguridad").ifBlank { valor("Seguridad") }
                val claveWifi = wifi?.contrasena?.ifBlank { valor("Contraseña Wi-Fi").ifBlank { valor("Contraseña") } }?.ifBlank { e.contrasena } ?: e.contrasena
                iden = when {
                    ssid.isNotBlank() && seguridad.isNotBlank() -> "SSID: $ssid ($seguridad)"
                    ssid.isNotBlank() -> "SSID: $ssid"
                    seguridad.isNotBlank() -> "Seguridad: $seguridad"
                    else -> "Red Wi-Fi"
                }
                sec = claveWifi
            }
            TipoEntrada.TARJETA -> {
                val tar = e.tarjetaEfectiva
                val titular = tar?.titular?.ifBlank { valor("Titular") } ?: valor("Titular")
                val numero = tar?.numero?.ifBlank { valor("Número de tarjeta").ifBlank { valor("Número") } } ?: valor("Número de tarjeta").ifBlank { valor("Número") }
                val venc = tar?.vencimiento?.ifBlank { valor("Vencimiento") } ?: valor("Vencimiento")
                val cvv = tar?.cvv?.ifBlank { valor("CVV") } ?: valor("CVV")
                val pin = tar?.pin?.ifBlank { valor("PIN de tarjeta").ifBlank { valor("PIN") } } ?: valor("PIN de tarjeta").ifBlank { valor("PIN") }
                iden = listOfNotNull(
                    titular.takeIf { it.isNotBlank() }?.let { "Titular: $it" },
                    numero.takeIf { it.isNotBlank() }?.let { "Nº: $it" }
                ).joinToString("\n").ifBlank { "Tarjeta bancaria" }
                sec = listOfNotNull(
                    venc.takeIf { it.isNotBlank() }?.let { "Venc: $it" },
                    cvv.takeIf { it.isNotBlank() }?.let { "CVV: $it" },
                    pin.takeIf { it.isNotBlank() }?.let { "PIN: $it" }
                ).joinToString(" • ").ifBlank { e.contrasena }
            }
            TipoEntrada.CUENTA_BANCARIA -> {
                val cta = e.cuentaBancariaEfectiva
                val banco = cta?.banco?.ifBlank { valor("Banco / Entidad").ifBlank { valor("Banco") } } ?: valor("Banco / Entidad").ifBlank { valor("Banco") }
                val titular = cta?.titular?.ifBlank { valor("Titular de la cuenta").ifBlank { valor("Titular") } } ?: valor("Titular de la cuenta").ifBlank { valor("Titular") }
                val cuenta = cta?.numeroCuenta?.ifBlank { valor("Número de cuenta / IBAN").ifBlank { valor("Número de cuenta") }.ifBlank { valor("IBAN") } } ?: valor("Número de cuenta / IBAN")
                val swift = cta?.swiftBic?.ifBlank { valor("SWIFT / CBU / CLABE").ifBlank { valor("SWIFT") }.ifBlank { valor("CBU") } } ?: valor("SWIFT / CBU / CLABE")
                iden = listOfNotNull(
                    banco.takeIf { it.isNotBlank() }?.let { "Banco: $it" },
                    titular.takeIf { it.isNotBlank() }?.let { "Titular: $it" }
                ).joinToString("\n").ifBlank { "Cuenta bancaria" }
                sec = listOfNotNull(
                    cuenta.takeIf { it.isNotBlank() }?.let { "Cuenta/IBAN: $it" },
                    swift.takeIf { it.isNotBlank() }?.let { "SWIFT: $it" }
                ).joinToString(" • ").ifBlank { e.contrasena }
            }
            TipoEntrada.IDENTIDAD -> {
                val idenDoc = e.identidadEfectiva
                val tipoDoc = idenDoc?.tipoDocumento?.ifBlank { valor("Tipo de documento").ifBlank { valor("Tipo") } } ?: valor("Tipo de documento").ifBlank { valor("Tipo") }
                val nombre = idenDoc?.nombreCompleto?.ifBlank { valor("Nombre completo").ifBlank { valor("Nombre") } } ?: valor("Nombre completo").ifBlank { valor("Nombre") }
                val numDoc = idenDoc?.numero?.ifBlank { valor("Número de documento").ifBlank { valor("Número") } } ?: valor("Número de documento").ifBlank { valor("Número") }
                val caducidad = idenDoc?.caducidad?.ifBlank { valor("Caducidad") } ?: valor("Caducidad")
                val pais = idenDoc?.paisEmisor?.ifBlank { valor("País emisor").ifBlank { valor("País") } } ?: valor("País emisor").ifBlank { valor("País") }
                iden = listOfNotNull(
                    tipoDoc.takeIf { it.isNotBlank() }?.let { "Tipo: $it" },
                    nombre.takeIf { it.isNotBlank() }?.let { "Nombre: $it" }
                ).joinToString("\n").ifBlank { "Documento de identidad" }
                sec = listOfNotNull(
                    numDoc.takeIf { it.isNotBlank() }?.let { "Nº: $it" },
                    caducidad.takeIf { it.isNotBlank() }?.let { "Cad: $it" },
                    pais.takeIf { it.isNotBlank() }?.let { "País: $it" }
                ).joinToString(" • ").ifBlank { e.contrasena }
            }
            TipoEntrada.SERVIDOR -> {
                val serv = e.servidorEfectivo
                val host = serv?.host?.ifBlank { valor("Host o IP").ifBlank { valor("Host") }.ifBlank { valor("IP") } } ?: valor("Host o IP")
                val puerto = serv?.puerto?.ifBlank { valor("Puerto") } ?: valor("Puerto")
                val userSsh = serv?.usuario?.ifBlank { valor("Usuario SSH").ifBlank { valor("Usuario") } }?.ifBlank { e.usuario } ?: e.usuario
                val pass = serv?.clavePrivada?.ifBlank { valor("Clave privada / Password").ifBlank { valor("Password") }.ifBlank { valor("Clave privada") } }?.ifBlank { e.contrasena } ?: e.contrasena
                iden = when {
                    userSsh.isNotBlank() && host.isNotBlank() -> "$userSsh@$host${if (puerto.isNotBlank()) ":$puerto" else ""}"
                    host.isNotBlank() -> "$host${if (puerto.isNotBlank()) ":$puerto" else ""}"
                    userSsh.isNotBlank() -> "Usuario: $userSsh"
                    else -> "Servidor"
                }
                sec = pass
            }
            TipoEntrada.WALLET -> {
                val wal = e.walletEfectiva
                val red = wal?.red?.ifBlank { valor("Red / Blockchain").ifBlank { valor("Red") } } ?: valor("Red / Blockchain").ifBlank { valor("Red") }
                val dir = wal?.direccionPublica?.ifBlank { valor("Dirección pública").ifBlank { valor("Dirección") } } ?: valor("Dirección pública").ifBlank { valor("Dirección") }
                val semilla = wal?.fraseSemilla?.ifBlank { valor("Frase semilla (Seed phrase)").ifBlank { valor("Frase semilla") }.ifBlank { valor("Seed phrase") } } ?: valor("Frase semilla (Seed phrase)")
                val priv = wal?.clavePrivada?.ifBlank { valor("Clave privada") } ?: valor("Clave privada")
                iden = listOfNotNull(
                    red.takeIf { it.isNotBlank() }?.let { "Red: $it" },
                    dir.takeIf { it.isNotBlank() }?.let { "Dir: $it" }
                ).joinToString("\n").ifBlank { "Cripto Wallet" }
                sec = listOfNotNull(
                    semilla.takeIf { it.isNotBlank() }?.let { "Semilla: $it" },
                    priv.takeIf { it.isNotBlank() }?.let { "Clave priv: $it" }
                ).joinToString("\n").ifBlank { e.contrasena }
            }
            TipoEntrada.PASSKEY -> {
                val rp = e.passkey?.rpName?.ifBlank { e.passkey?.rpId } ?: ""
                val u = e.passkey?.usuario?.ifBlank { e.usuario } ?: e.usuario
                iden = listOfNotNull(
                    u.takeIf { it.isNotBlank() }?.let { "Usuario: $it" },
                    rp.takeIf { it.isNotBlank() }?.let { "Sitio (RP): $it" }
                ).joinToString("\n").ifBlank { "Passkey FIDO2" }
                sec = "Credencial FIDO2 / WebAuthn (Protegida por hardware)"
            }
            TipoEntrada.NOTA -> {
                iden = "—"
                sec = e.notas.ifBlank { "(sin contenido)" }
            }
            TipoEntrada.CONTACTO -> {
                iden = e.usuario.ifBlank { "Contacto" }
                sec = e.notas.ifBlank { "—" }
            }
            TipoEntrada.LOGIN -> {
                iden = e.usuario.ifBlank { e.urls.firstOrNull() ?: "—" }
                sec = e.contrasena
            }
        }

        val extras = campos.filter { c ->
            c.valor.isNotBlank() && etiquetasUsadas.none { c.etiqueta.contains(it, ignoreCase = true) || it.contains(c.etiqueta, ignoreCase = true) }
        }.mapNotNull { c ->
            if (c.esSensibleEfectivo && !incluirSecretos) {
                null
            } else {
                c.etiqueta to c.valor
            }
        }

        return GeneradorKitEmergencia.DatosKitEntrada(
            identificador = iden.ifBlank { "—" },
            secreto = sec.ifBlank { "—" },
            detallesExtras = extras
        )
    }
}
