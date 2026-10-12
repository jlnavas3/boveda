/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HealthAndSafety
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.util.MedidorFuerza

/**
 * Define los elementos de menú del Hub de Ajustes para la lista de cuentas.
 */
fun crearElementosListaAjustes(
    ajustes: AjustesApp,
    vm: VaultViewModel
): List<ElementoMenuAjustes> {
    val entradas = vm.repositorio.entradas()
    val conteosPorTipo = entradas.groupingBy { it.tipo }.eachCount()
    val numLogin = conteosPorTipo[TipoEntrada.LOGIN] ?: 0
    val numPasskeys = entradas.count { it.passkey != null }
    val numTotp = entradas.count { !it.secretoTotp.isNullOrBlank() }
    val numNota = conteosPorTipo[TipoEntrada.NOTA] ?: 0
    val numTarjeta = conteosPorTipo[TipoEntrada.TARJETA] ?: 0
    val numContacto = conteosPorTipo[TipoEntrada.CONTACTO] ?: 0
    val numWifi = conteosPorTipo[TipoEntrada.WIFI] ?: 0
    val numCuentaBancaria = conteosPorTipo[TipoEntrada.CUENTA_BANCARIA] ?: 0
    val numIdentidad = conteosPorTipo[TipoEntrada.IDENTIDAD] ?: 0
    val numServidor = conteosPorTipo[TipoEntrada.SERVIDOR] ?: 0
    val numWallet = conteosPorTipo[TipoEntrada.WALLET] ?: 0

    val numIdentidades = vm.repositorio.identidades().size
    val numPapelera = vm.repositorio.papelera().size
    val numCategorias = vm.repositorio.categorias().size
    val numDuplicadas = AnalizadorDuplicados.analizar(entradas).sumOf { it.entradasSecundarias.size }

    val clavesSalud = entradas.filter { it.tipo == TipoEntrada.LOGIN && it.contrasena.isNotBlank() && !it.ignoradaEnSalud }
    val clavesRepetidas = clavesSalud.groupBy { it.contrasena }.filter { it.value.size > 1 }.values.flatten().toSet()
    val clavesDebiles = clavesSalud.filter { MedidorFuerza.medir(it.contrasena).puntuacion <= 1 }.toSet()
    val numProblemasSalud = (clavesRepetidas + clavesDebiles).size

    return listOf(
        ElementoMenuAjustes(
            titulo = "Contraseñas",
            subtitulo = "Inicios de sesión, credenciales web y aplicaciones",
            icono = Icons.Filled.Lock,
            colorIcono = Color(0xFF1E88E5),
            idEtiqueta = "03-LST-LOG",
            grupo = "Lista de cuentas",
            palabrasClave = "login contrasenas credenciales usuarios claves cuentas web",
            valorTexto = "$numLogin",
            alPulsar = { vm.ir(Pantalla.EntradasTipo(TipoEntrada.LOGIN)) }
        ),
        ElementoMenuAjustes(
            titulo = "Llaves de paso",
            subtitulo = "Credenciales FIDO2 WebAuthn y passkeys locales",
            icono = Icons.Filled.Fingerprint,
            colorIcono = Color(0xFF10B981),
            idEtiqueta = "03-LST-PAS",
            grupo = "Lista de cuentas",
            palabrasClave = "passkeys llaves fido2 webauthn biometria sin contrasena credenciales",
            valorTexto = "$numPasskeys",
            alPulsar = { vm.ir(Pantalla.Passkeys) }
        ),
        ElementoMenuAjustes(
            titulo = "Autenticador 2FA",
            subtitulo = "Códigos TOTP temporales de un solo uso",
            icono = Icons.Filled.Timer,
            colorIcono = Color(0xFFF97316),
            idEtiqueta = "03-LST-2FA",
            grupo = "Lista de cuentas",
            palabrasClave = "autenticador totp 2fa codigos temporales doble factor segundo factor otp",
            valorTexto = "$numTotp",
            alPulsar = { vm.ir(Pantalla.Autenticador) }
        ),
        ElementoMenuAjustes(
            titulo = "Notas seguras",
            subtitulo = "Anotaciones confidenciales, códigos y textos privados",
            icono = Icons.Filled.Description,
            colorIcono = Color(0xFF00ACC1),
            idEtiqueta = "03-LST-NOT",
            grupo = "Lista de cuentas",
            palabrasClave = "notas seguras anotaciones privadas textos secretos",
            valorTexto = "$numNota",
            alPulsar = { vm.ir(Pantalla.EntradasTipo(TipoEntrada.NOTA)) }
        ),
        ElementoMenuAjustes(
            titulo = "Tarjetas bancarias",
            subtitulo = "Tarjetas de crédito, débito y prepago",
            icono = Icons.Filled.CreditCard,
            colorIcono = Color(0xFF43A047),
            idEtiqueta = "03-LST-TAR",
            grupo = "Lista de cuentas",
            palabrasClave = "tarjetas bancarias credito debito visa mastercard cvv pin",
            valorTexto = "$numTarjeta",
            alPulsar = { vm.ir(Pantalla.EntradasTipo(TipoEntrada.TARJETA)) }
        ),
        ElementoMenuAjustes(
            titulo = "Contactos seguros",
            subtitulo = "Información de contactos privados y vCards",
            icono = Icons.Filled.Person,
            colorIcono = Color(0xFF8E24AA),
            idEtiqueta = "03-LST-CON",
            grupo = "Lista de cuentas",
            palabrasClave = "contactos seguros personas agenda vcard telefonos",
            valorTexto = "$numContacto",
            alPulsar = { vm.ir(Pantalla.EntradasTipo(TipoEntrada.CONTACTO)) }
        ),
        ElementoMenuAjustes(
            titulo = "Redes Wi-Fi",
            subtitulo = "Claves de acceso a redes inalámbricas y routers",
            icono = Icons.Filled.Wifi,
            colorIcono = Color(0xFF00897B),
            idEtiqueta = "03-LST-WIF",
            grupo = "Lista de cuentas",
            palabrasClave = "wifi redes inalambricas ssid contrasena wifi routers",
            valorTexto = "$numWifi",
            alPulsar = { vm.ir(Pantalla.EntradasTipo(TipoEntrada.WIFI)) }
        ),
        ElementoMenuAjustes(
            titulo = "Cuentas bancarias",
            subtitulo = "Cuentas corrientes, IBAN, SWIFT y entidades",
            icono = Icons.Filled.AccountBalance,
            colorIcono = Color(0xFFFB8C00),
            idEtiqueta = "03-LST-BNC",
            grupo = "Lista de cuentas",
            palabrasClave = "cuentas bancarias banco iban swift bic finanzas",
            valorTexto = "$numCuentaBancaria",
            alPulsar = { vm.ir(Pantalla.EntradasTipo(TipoEntrada.CUENTA_BANCARIA)) }
        ),
        ElementoMenuAjustes(
            titulo = "Documentos de identidad",
            subtitulo = "DNI, pasaportes, licencias de conducir y licencias",
            icono = Icons.Filled.Badge,
            colorIcono = Color(0xFF0284C7),
            idEtiqueta = "03-LST-IDN",
            grupo = "Lista de cuentas",
            palabrasClave = "identidad documentos dni pasaporte licencia nie carnet",
            valorTexto = "$numIdentidad",
            alPulsar = { vm.ir(Pantalla.EntradasTipo(TipoEntrada.IDENTIDAD)) }
        ),
        ElementoMenuAjustes(
            titulo = "Servidores / SSH",
            subtitulo = "Hosts, direcciones IP, claves SSH y accesos remotos",
            icono = Icons.Filled.Dns,
            colorIcono = Color(0xFF546E7A),
            idEtiqueta = "03-LST-SRV",
            grupo = "Lista de cuentas",
            palabrasClave = "servidores ssh host ip puerto remoto vps ftp",
            valorTexto = "$numServidor",
            alPulsar = { vm.ir(Pantalla.EntradasTipo(TipoEntrada.SERVIDOR)) }
        ),
        ElementoMenuAjustes(
            titulo = "Cripto Wallets",
            subtitulo = "Billeteras cripto, frases semilla y direcciones de red",
            icono = Icons.Filled.AccountBalanceWallet,
            colorIcono = Color(0xFFF59E0B),
            idEtiqueta = "03-LST-WAL",
            grupo = "Lista de cuentas",
            palabrasClave = "cripto wallets billeteras seed phrase semilla bitcoin ethereum",
            valorTexto = "$numWallet",
            alPulsar = { vm.ir(Pantalla.EntradasTipo(TipoEntrada.WALLET)) }
        ),
        ElementoMenuAjustes(
            titulo = "Identidades",
            subtitulo = "Perfiles de correo y vinculación inteligente",
            icono = Icons.Filled.AccountCircle,
            colorIcono = Color(0xFF0284C7),
            idEtiqueta = "03-LST-IDE",
            grupo = "Lista de cuentas",
            palabrasClave = "identidades cuentas perfiles correo email alias inteligente vincular personas",
            valorTexto = "$numIdentidades",
            alPulsar = { vm.ir(Pantalla.Identidades("03-LST-IDE")) }
        ),
        ElementoMenuAjustes(
            titulo = "Salud de la bóveda",
            subtitulo = "Auditoría de contraseñas débiles, repetidas y vulneradas",
            icono = Icons.Filled.HealthAndSafety,
            colorIcono = Color(0xFF10B981),
            idEtiqueta = "03-LST-SLD",
            grupo = "Lista de cuentas",
            palabrasClave = "salud seguridad debiles repetidas antiguas analisis auditoria",
            valorTexto = "$numProblemasSalud",
            alPulsar = { vm.ir(Pantalla.SaludBoveda("03-LST-SLD")) }
        ),
        ElementoMenuAjustes(
            titulo = "Cuentas duplicadas",
            subtitulo = "Detección y resolución de entradas repetidas",
            icono = Icons.Filled.ContentCopy,
            colorIcono = Color(0xFFF59E0B),
            idEtiqueta = "03-LST-DUP",
            grupo = "Lista de cuentas",
            palabrasClave = "duplicados repetidas clones limpieza",
            valorTexto = "$numDuplicadas",
            alPulsar = { vm.ir(Pantalla.Duplicados("03-LST-DUP")) }
        ),
        ElementoMenuAjustes(
            titulo = "Papelera de reciclaje",
            subtitulo = "Recuperación y purga definitiva de cuentas eliminadas",
            icono = Icons.Filled.Delete,
            colorIcono = Color(0xFFEF4444),
            idEtiqueta = "03-LST-PAP",
            grupo = "Lista de cuentas",
            palabrasClave = "papelera borrados recuperar restaurar eliminar purga",
            valorTexto = "$numPapelera",
            alPulsar = { vm.ir(Pantalla.Papelera("03-LST-PAP")) }
        ),
        ElementoMenuAjustes(
            titulo = "Categorías de cuentas",
            subtitulo = "Organizar entradas por carpetas, etiquetas y grupos",
            icono = Icons.Filled.Folder,
            colorIcono = Color(0xFF10B981),
            idEtiqueta = "03-LST-CAT",
            grupo = "Lista de cuentas",
            palabrasClave = "categorias carpetas etiquetas clasificar grupos",
            valorTexto = "$numCategorias",
            alPulsar = { vm.ir(Pantalla.Categorias("03-LST-CAT")) }
        )
    )
}
