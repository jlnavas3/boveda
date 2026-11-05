/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

data class SegmentoIndicadorDato(
    val activo: Boolean,
    val color: Color,
    val icono: ImageVector,
    val etiqueta: String
)

@Composable
fun recordarSegmentosEntrada(entrada: Entrada): List<SegmentoIndicadorDato> {
    return remember(
        entrada.usuario,
        entrada.contrasena,
        entrada.secretoTotp,
        entrada.passkey,
        entrada.urls,
        entrada.tipo,
        entrada.datosTarjeta,
        entrada.datosWifi,
        entrada.datosCuentaBancaria,
        entrada.datosIdentidad,
        entrada.datosServidor,
        entrada.datosWallet,
        ColorDatosUsuario,
        ColorDatosContrasena,
        ColorDatos2FA,
        ColorDatosPasskey,
        ColorDatosWeb,
        ColorDatosApp
    ) {
        var tieneWeb = false
        var tieneApp = false
        for (u in entrada.urls) {
            if (tieneWeb && tieneApp) break
            val paquete = LanzadorEnlaces.extraerPaquete(u)
            if (paquete != null) {
                tieneApp = true
            } else if (u.contains(".") || u.startsWith("http", ignoreCase = true)) {
                tieneWeb = true
            }
        }

        when (entrada.tipo) {
            TipoEntrada.WIFI -> {
                val wifi = entrada.wifiEfectivo
                val tieneContrasenaWifi = wifi?.contrasena?.isNotBlank() == true
                val tieneSeguridad = wifi?.tipoSeguridad?.isNotBlank() == true && !wifi.tipoSeguridad.equals("abierta", ignoreCase = true)
                val tieneSsid = wifi?.ssid?.isNotBlank() == true

                listOf(
                    SegmentoIndicadorDato(false, ColorDatosUsuario, Icons.Filled.Person, "Usuario"),
                    SegmentoIndicadorDato(tieneContrasenaWifi, ColorDatosContrasena, Icons.Filled.Key, "Contraseña Wi-Fi"),
                    SegmentoIndicadorDato(tieneSeguridad, ColorDatos2FA, Icons.Filled.Security, "Seguridad Wi-Fi"),
                    SegmentoIndicadorDato(false, ColorDatosPasskey, Icons.Filled.Fingerprint, "Passkey"),
                    SegmentoIndicadorDato(tieneSsid, ColorDatosWeb, Icons.Filled.Wifi, "Red Wi-Fi (SSID)"),
                    SegmentoIndicadorDato(tieneApp, ColorDatosApp, Icons.Filled.Android, "App vinculada")
                )
            }
            TipoEntrada.TARJETA -> {
                val tarjeta = entrada.tarjetaEfectiva
                val tieneTitular = tarjeta?.titular?.isNotBlank() == true
                val tieneSecretos = tarjeta?.cvv?.isNotBlank() == true || tarjeta?.pin?.isNotBlank() == true
                val tieneVencimiento = tarjeta?.vencimiento?.isNotBlank() == true
                val tieneBanco = tarjeta?.marca?.isNotBlank() == true || tarjeta?.tipo?.isNotBlank() == true || tieneWeb

                listOf(
                    SegmentoIndicadorDato(tieneTitular, ColorDatosUsuario, Icons.Filled.Person, "Titular de tarjeta"),
                    SegmentoIndicadorDato(tieneSecretos, ColorDatosContrasena, Icons.Filled.Key, "CVV / PIN"),
                    SegmentoIndicadorDato(tieneVencimiento, ColorDatos2FA, Icons.Filled.CalendarMonth, "Vencimiento"),
                    SegmentoIndicadorDato(false, ColorDatosPasskey, Icons.Filled.Fingerprint, "Passkey"),
                    SegmentoIndicadorDato(tieneBanco, ColorDatosWeb, Icons.Filled.CreditCard, "Emisor / Banco"),
                    SegmentoIndicadorDato(tieneApp, ColorDatosApp, Icons.Filled.Android, "App bancaria")
                )
            }
            TipoEntrada.CUENTA_BANCARIA -> {
                val cuenta = entrada.cuentaBancariaEfectiva
                val tieneTitular = cuenta?.titular?.isNotBlank() == true
                val tieneBanco = cuenta?.banco?.isNotBlank() == true || tieneWeb

                listOf(
                    SegmentoIndicadorDato(tieneTitular, ColorDatosUsuario, Icons.Filled.Person, "Titular de cuenta"),
                    SegmentoIndicadorDato(false, ColorDatosContrasena, Icons.Filled.Key, "PIN de acceso"),
                    SegmentoIndicadorDato(false, ColorDatos2FA, Icons.Filled.Timer, "2FA"),
                    SegmentoIndicadorDato(false, ColorDatosPasskey, Icons.Filled.Fingerprint, "Passkey"),
                    SegmentoIndicadorDato(tieneBanco, ColorDatosWeb, Icons.Filled.AccountBalance, "Banco / Entidad"),
                    SegmentoIndicadorDato(tieneApp, ColorDatosApp, Icons.Filled.Android, "App bancaria")
                )
            }
            TipoEntrada.IDENTIDAD -> {
                val id = entrada.identidadEfectiva
                val tieneNombre = id?.nombreCompleto?.isNotBlank() == true
                val tieneVencimiento = id?.caducidad?.isNotBlank() == true
                val tienePais = id?.paisEmisor?.isNotBlank() == true || tieneWeb

                listOf(
                    SegmentoIndicadorDato(tieneNombre, ColorDatosUsuario, Icons.Filled.Person, "Titular del documento"),
                    SegmentoIndicadorDato(false, ColorDatosContrasena, Icons.Filled.Key, "Secreto"),
                    SegmentoIndicadorDato(tieneVencimiento, ColorDatos2FA, Icons.Filled.CalendarMonth, "Vencimiento documento"),
                    SegmentoIndicadorDato(false, ColorDatosPasskey, Icons.Filled.Fingerprint, "Passkey"),
                    SegmentoIndicadorDato(tienePais, ColorDatosWeb, Icons.Filled.Public, "País emisor"),
                    SegmentoIndicadorDato(tieneApp, ColorDatosApp, Icons.Filled.Android, "App")
                )
            }
            TipoEntrada.SERVIDOR -> {
                val servidor = entrada.servidorEfectivo
                val tieneUsuario = servidor?.usuario?.isNotBlank() == true || entrada.usuario.isNotBlank()
                val tieneClavePrivada = servidor?.clavePrivada?.isNotBlank() == true || entrada.contrasena.isNotBlank()
                val tiene2FA = !entrada.secretoTotp.isNullOrBlank()
                val tieneClavePublica = servidor?.clavePublica?.isNotBlank() == true
                val tieneHost = servidor?.host?.isNotBlank() == true || tieneWeb

                listOf(
                    SegmentoIndicadorDato(tieneUsuario, ColorDatosUsuario, Icons.Filled.Person, "Usuario SSH"),
                    SegmentoIndicadorDato(tieneClavePrivada, ColorDatosContrasena, Icons.Filled.Key, "Clave privada / Password"),
                    SegmentoIndicadorDato(tiene2FA, ColorDatos2FA, Icons.Filled.Timer, "2FA SSH"),
                    SegmentoIndicadorDato(tieneClavePublica, ColorDatosPasskey, Icons.Filled.Fingerprint, "Clave pública SSH"),
                    SegmentoIndicadorDato(tieneHost, ColorDatosWeb, Icons.Filled.Dns, "Host / Servidor"),
                    SegmentoIndicadorDato(tieneApp, ColorDatosApp, Icons.Filled.Android, "App vinculada")
                )
            }
            TipoEntrada.WALLET -> {
                val wallet = entrada.walletEfectiva
                val tienePrivada = wallet?.clavePrivada?.isNotBlank() == true
                val tiene2FA = !entrada.secretoTotp.isNullOrBlank()
                val tieneSemilla = wallet?.fraseSemilla?.isNotBlank() == true
                val tieneDireccion = wallet?.direccionPublica?.isNotBlank() == true || tieneWeb

                listOf(
                    SegmentoIndicadorDato(false, ColorDatosUsuario, Icons.Filled.Person, "Titular"),
                    SegmentoIndicadorDato(tienePrivada, ColorDatosContrasena, Icons.Filled.Key, "Clave privada"),
                    SegmentoIndicadorDato(tiene2FA, ColorDatos2FA, Icons.Filled.Timer, "2FA"),
                    SegmentoIndicadorDato(tieneSemilla, ColorDatosPasskey, Icons.Filled.Fingerprint, "Frase semilla"),
                    SegmentoIndicadorDato(tieneDireccion, ColorDatosWeb, Icons.Filled.AccountBalanceWallet, "Dirección pública"),
                    SegmentoIndicadorDato(tieneApp, ColorDatosApp, Icons.Filled.Android, "App wallet")
                )
            }
            else -> {
                val tieneUsuario = entrada.usuario.isNotBlank() ||
                    entrada.tarjetaEfectiva?.titular?.isNotBlank() == true ||
                    entrada.cuentaBancariaEfectiva?.titular?.isNotBlank() == true ||
                    entrada.identidadEfectiva?.nombreCompleto?.isNotBlank() == true ||
                    entrada.servidorEfectivo?.usuario?.isNotBlank() == true

                val tieneContrasena = entrada.contrasena.isNotBlank() ||
                    entrada.wifiEfectivo?.contrasena?.isNotBlank() == true ||
                    entrada.tarjetaEfectiva?.cvv?.isNotBlank() == true ||
                    entrada.tarjetaEfectiva?.pin?.isNotBlank() == true ||
                    entrada.servidorEfectivo?.clavePrivada?.isNotBlank() == true ||
                    entrada.walletEfectiva?.clavePrivada?.isNotBlank() == true

                val tiene2FA = !entrada.secretoTotp.isNullOrBlank()

                val tienePasskey = entrada.passkey != null ||
                    entrada.walletEfectiva?.fraseSemilla?.isNotBlank() == true ||
                    entrada.servidorEfectivo?.clavePublica?.isNotBlank() == true

                if (!tieneWeb && (entrada.servidorEfectivo?.host?.isNotBlank() == true || entrada.walletEfectiva?.direccionPublica?.isNotBlank() == true)) {
                    tieneWeb = true
                }

                listOf(
                    SegmentoIndicadorDato(tieneUsuario, ColorDatosUsuario, Icons.Filled.Person, "Usuario / Correo"),
                    SegmentoIndicadorDato(tieneContrasena, ColorDatosContrasena, Icons.Filled.Key, "Contraseña"),
                    SegmentoIndicadorDato(tiene2FA, ColorDatos2FA, Icons.Filled.Timer, "2FA"),
                    SegmentoIndicadorDato(tienePasskey, ColorDatosPasskey, Icons.Filled.Fingerprint, "Passkey"),
                    SegmentoIndicadorDato(tieneWeb, ColorDatosWeb, Icons.Filled.Language, "Sitio Web"),
                    SegmentoIndicadorDato(tieneApp, ColorDatosApp, Icons.Filled.Android, "App Android")
                )
            }
        }
    }
}

/**
 * Convierte una [com.jlnavas3.bovedalocal.sync.MascaraCampos] de sincronización en la lista
 * de segmentos indicadores para pintar en tarjetas de reporte con los mismos estilos de la app.
 */
@Composable
fun recordarSegmentosDiff(
    mascara: com.jlnavas3.bovedalocal.sync.MascaraCampos,
    tipo: TipoEntrada = TipoEntrada.LOGIN
): List<SegmentoIndicadorDato> {
    return remember(
        mascara,
        tipo,
        ColorDatosUsuario,
        ColorDatosContrasena,
        ColorDatos2FA,
        ColorDatosPasskey,
        ColorDatosWeb,
        ColorDatosApp
    ) {
        when (tipo) {
            TipoEntrada.WIFI -> listOf(
                SegmentoIndicadorDato(false, ColorDatosUsuario, Icons.Filled.Person, "Usuario"),
                SegmentoIndicadorDato(mascara.clave, ColorDatosContrasena, Icons.Filled.Key, "Contraseña Wi-Fi"),
                SegmentoIndicadorDato(mascara.totp, ColorDatos2FA, Icons.Filled.Security, "Seguridad Wi-Fi"),
                SegmentoIndicadorDato(false, ColorDatosPasskey, Icons.Filled.Fingerprint, "Passkey"),
                SegmentoIndicadorDato(mascara.web, ColorDatosWeb, Icons.Filled.Wifi, "Red Wi-Fi (SSID)"),
                SegmentoIndicadorDato(mascara.app, ColorDatosApp, Icons.Filled.Android, "App vinculada")
            )
            TipoEntrada.TARJETA -> listOf(
                SegmentoIndicadorDato(mascara.usuario, ColorDatosUsuario, Icons.Filled.Person, "Titular de tarjeta"),
                SegmentoIndicadorDato(mascara.clave, ColorDatosContrasena, Icons.Filled.Key, "CVV / PIN"),
                SegmentoIndicadorDato(mascara.totp, ColorDatos2FA, Icons.Filled.CalendarMonth, "Vencimiento"),
                SegmentoIndicadorDato(false, ColorDatosPasskey, Icons.Filled.Fingerprint, "Passkey"),
                SegmentoIndicadorDato(mascara.submodelos || mascara.web, ColorDatosWeb, Icons.Filled.CreditCard, "Emisor / Banco"),
                SegmentoIndicadorDato(mascara.app, ColorDatosApp, Icons.Filled.Android, "App bancaria")
            )
            else -> listOf(
                SegmentoIndicadorDato(mascara.usuario, ColorDatosUsuario, Icons.Filled.Person, "Usuario / Correo"),
                SegmentoIndicadorDato(mascara.clave, ColorDatosContrasena, Icons.Filled.Key, "Contraseña"),
                SegmentoIndicadorDato(mascara.totp, ColorDatos2FA, Icons.Filled.Timer, "2FA"),
                SegmentoIndicadorDato(mascara.passkey, ColorDatosPasskey, Icons.Filled.Fingerprint, "Passkey"),
                SegmentoIndicadorDato(mascara.web, ColorDatosWeb, Icons.Filled.Language, "Sitio Web"),
                SegmentoIndicadorDato(mascara.app || mascara.submodelos || mascara.camposExtra, ColorDatosApp, Icons.Filled.Android, "App / Datos extra")
            )
        }
    }
}
