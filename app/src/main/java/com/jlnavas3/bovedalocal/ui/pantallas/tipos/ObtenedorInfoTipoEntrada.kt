/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Resuelve la metadata y recursos visuales para cada [TipoEntrada].
 */
fun obtenerInfoTipoEntrada(tipo: TipoEntrada): InfoTipoEntrada = when (tipo) {
    TipoEntrada.LOGIN -> InfoTipoEntrada(
        tituloPlural = "Contraseñas",
        tituloSingular = "Contraseña",
        subtitulo = "Inicios de sesión, credenciales web y aplicaciones",
        icono = Icons.Filled.Lock,
        color = Color(0xFF1E88E5),
        idEtiqueta = "03-LST-LOG",
        descripcionCrear = "Nueva contraseña",
        textoVacioTotal = "Todavía no tienes contraseñas guardadas.",
        palabrasClave = listOf("login", "contrasenas", "credenciales", "usuarios", "claves", "cuentas", "web")
    )
    TipoEntrada.NOTA -> InfoTipoEntrada(
        tituloPlural = "Notas seguras",
        tituloSingular = "Nota segura",
        subtitulo = "Anotaciones confidenciales, códigos y textos privados",
        icono = Icons.Filled.Description,
        color = Color(0xFF00ACC1),
        idEtiqueta = "03-LST-NOT",
        descripcionCrear = "Nueva nota segura",
        textoVacioTotal = "Todavía no tienes notas seguras guardadas.",
        palabrasClave = listOf("notas", "seguras", "anotaciones", "privadas", "textos", "secretos")
    )
    TipoEntrada.TARJETA -> InfoTipoEntrada(
        tituloPlural = "Tarjetas bancarias",
        tituloSingular = "Tarjeta bancaria",
        subtitulo = "Tarjetas de crédito, débito y prepago",
        icono = Icons.Filled.CreditCard,
        color = Color(0xFF43A047),
        idEtiqueta = "03-LST-TAR",
        descripcionCrear = "Nueva tarjeta bancaria",
        textoVacioTotal = "Todavía no tienes tarjetas bancarias guardadas.",
        palabrasClave = listOf("tarjetas", "bancarias", "credito", "debito", "visa", "mastercard", "cvv", "pin")
    )
    TipoEntrada.CONTACTO -> InfoTipoEntrada(
        tituloPlural = "Contactos seguros",
        tituloSingular = "Contacto seguro",
        subtitulo = "Información de contactos privados y vCards",
        icono = Icons.Filled.Person,
        color = Color(0xFF8E24AA),
        idEtiqueta = "03-LST-CON",
        descripcionCrear = "Nuevo contacto seguro",
        textoVacioTotal = "Todavía no tienes contactos seguros guardados.",
        palabrasClave = listOf("contactos", "seguros", "personas", "agenda", "vcard", "telefonos")
    )
    TipoEntrada.WIFI -> InfoTipoEntrada(
        tituloPlural = "Redes Wi-Fi",
        tituloSingular = "Red Wi-Fi",
        subtitulo = "Claves de acceso a redes inalámbricas y routers",
        icono = Icons.Filled.Wifi,
        color = Color(0xFF00897B),
        idEtiqueta = "03-LST-WIF",
        descripcionCrear = "Nueva red Wi-Fi",
        textoVacioTotal = "Todavía no tienes redes Wi-Fi guardadas.",
        palabrasClave = listOf("wifi", "redes", "inalambricas", "ssid", "contrasena wifi", "routers")
    )
    TipoEntrada.CUENTA_BANCARIA -> InfoTipoEntrada(
        tituloPlural = "Cuentas bancarias",
        tituloSingular = "Cuenta bancaria",
        subtitulo = "Cuentas corrientes, IBAN, SWIFT y entidades",
        icono = Icons.Filled.AccountBalance,
        color = Color(0xFFFB8C00),
        idEtiqueta = "03-LST-BNC",
        descripcionCrear = "Nueva cuenta bancaria",
        textoVacioTotal = "Todavía no tienes cuentas bancarias guardadas.",
        palabrasClave = listOf("cuentas", "bancarias", "banco", "iban", "swift", "bic", "finanzas")
    )
    TipoEntrada.IDENTIDAD -> InfoTipoEntrada(
        tituloPlural = "Documentos de identidad",
        tituloSingular = "Documento de identidad",
        subtitulo = "DNI, pasaportes, licencias de conducir y licencias",
        icono = Icons.Filled.Badge,
        color = Color(0xFF0284C7),
        idEtiqueta = "03-LST-IDN",
        descripcionCrear = "Nuevo documento",
        textoVacioTotal = "Todavía no tienes documentos de identidad guardados.",
        palabrasClave = listOf("identidad", "documentos", "dni", "pasaporte", "licencia", "nie", "carnet")
    )
    TipoEntrada.SERVIDOR -> InfoTipoEntrada(
        tituloPlural = "Servidores / SSH",
        tituloSingular = "Servidor / SSH",
        subtitulo = "Hosts, direcciones IP, claves SSH y accesos remotos",
        icono = Icons.Filled.Dns,
        color = Color(0xFF546E7A),
        idEtiqueta = "03-LST-SRV",
        descripcionCrear = "Nuevo servidor",
        textoVacioTotal = "Todavía no tienes servidores guardados.",
        palabrasClave = listOf("servidores", "ssh", "host", "ip", "puerto", "remoto", "vps", "ftp")
    )
    TipoEntrada.WALLET -> InfoTipoEntrada(
        tituloPlural = "Cripto Wallets",
        tituloSingular = "Cripto Wallet",
        subtitulo = "Billeteras cripto, frases semilla y direcciones de red",
        icono = Icons.Filled.AccountBalanceWallet,
        color = Color(0xFFF59E0B),
        idEtiqueta = "03-LST-WAL",
        descripcionCrear = "Nueva wallet",
        textoVacioTotal = "Todavía no tienes wallets guardadas.",
        palabrasClave = listOf("cripto", "wallets", "billeteras", "seed phrase", "semilla", "bitcoin", "ethereum")
    )
    TipoEntrada.PASSKEY -> InfoTipoEntrada(
        tituloPlural = "Llaves de paso",
        tituloSingular = "Llave de paso",
        subtitulo = "Credenciales FIDO2 WebAuthn y passkeys locales",
        icono = Icons.Filled.Fingerprint,
        color = Color(0xFF10B981),
        idEtiqueta = "03-LST-PAS",
        descripcionCrear = "Nueva llave de paso",
        textoVacioTotal = "Todavía no tienes llaves de paso guardadas.",
        palabrasClave = listOf("passkeys", "llaves", "fido2", "webauthn", "biometria", "sin contrasena")
    )
}
