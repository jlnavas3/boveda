/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Wifi
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.AccionDeslizamiento
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase
import com.jlnavas3.bovedalocal.ui.theme.Azul
import com.jlnavas3.bovedalocal.ui.theme.Menta

/**
 * Microcomponente desacoplado encargado de resolver las acciones de deslizamiento
 * horizontal (swipe-to-action) de forma contextual según el [TipoEntrada].
 */
object ResolvedorAccionesDeslizamientoEntrada {

    fun resolver(
        entrada: Entrada,
        alCopiar: (etiqueta: String, valor: String, sensible: Boolean) -> Unit,
        alAvisar: (mensaje: String) -> Unit = {}
    ): Pair<AccionDeslizamiento?, AccionDeslizamiento?> {
        return when (entrada.tipo) {
            TipoEntrada.IDENTIDAD -> {
                val iden = entrada.identidadEfectiva
                val numDoc = iden?.numero?.ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Número de documento") } ?: ""
                val tipoDoc = iden?.tipoDocumento?.ifBlank { "Documento" } ?: "Documento"
                val titular = iden?.nombreCompleto?.ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Nombre completo") } ?: ""

                val izq = AccionDeslizamiento(
                    texto = "Copiar\n$tipoDoc",
                    icono = Icons.Filled.Badge,
                    color = Menta,
                    alEjecutar = {
                        if (numDoc.isNotBlank()) alCopiar(tipoDoc, numDoc, true)
                        else alAvisar("Sin número de documento")
                    }
                )
                val der = AccionDeslizamiento(
                    texto = "Copiar\nTitular",
                    icono = Icons.Filled.Person,
                    color = Azul,
                    alEjecutar = {
                        if (titular.isNotBlank()) alCopiar("Titular", titular, false)
                        else alAvisar("Sin nombre de titular")
                    }
                )
                Pair(izq, der)
            }

            TipoEntrada.TARJETA -> {
                val tar = entrada.tarjetaEfectiva
                val num = tar?.numero?.ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Número de tarjeta") } ?: ""
                val cvv = tar?.cvv?.ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "CVV") } ?: ""

                val izq = AccionDeslizamiento(
                    texto = "Copiar\nTarjeta",
                    icono = Icons.Filled.CreditCard,
                    color = Menta,
                    alEjecutar = {
                        if (num.isNotBlank()) alCopiar("Número de tarjeta", num, true)
                        else alAvisar("Sin número de tarjeta")
                    }
                )
                val der = AccionDeslizamiento(
                    texto = "Copiar\nCVV",
                    icono = Icons.Filled.Lock,
                    color = Azul,
                    alEjecutar = {
                        if (cvv.isNotBlank()) alCopiar("CVV", cvv, true)
                        else alAvisar("Sin código CVV")
                    }
                )
                Pair(izq, der)
            }

            TipoEntrada.WIFI -> {
                val wifi = entrada.wifiEfectivo
                val ssid = wifi?.ssid?.ifBlank { entrada.titulo } ?: entrada.titulo
                val clave = wifi?.contrasena?.ifBlank { entrada.contrasena } ?: entrada.contrasena

                val izq = AccionDeslizamiento(
                    texto = "Copiar\nRed (SSID)",
                    icono = Icons.Filled.Wifi,
                    color = Menta,
                    alEjecutar = {
                        if (ssid.isNotBlank()) alCopiar("Red Wi-Fi", ssid, false)
                        else alAvisar("Sin nombre de red")
                    }
                )
                val der = AccionDeslizamiento(
                    texto = "Copiar\nContraseña",
                    icono = Icons.Filled.Key,
                    color = Azul,
                    alEjecutar = {
                        if (clave.isNotBlank()) alCopiar("Contraseña Wi-Fi", clave, true)
                        else alAvisar("Sin contraseña Wi-Fi")
                    }
                )
                Pair(izq, der)
            }

            TipoEntrada.CUENTA_BANCARIA -> {
                val cta = entrada.cuentaBancariaEfectiva
                val iban = cta?.numeroCuenta?.ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Número de cuenta / IBAN") } ?: ""
                val titular = cta?.titular?.ifBlank { entrada.usuario } ?: entrada.usuario

                val izq = AccionDeslizamiento(
                    texto = "Copiar\nCuenta / IBAN",
                    icono = Icons.Filled.AccountBalance,
                    color = Menta,
                    alEjecutar = {
                        if (iban.isNotBlank()) alCopiar("Cuenta / IBAN", iban, true)
                        else alAvisar("Sin número de cuenta")
                    }
                )
                val der = AccionDeslizamiento(
                    texto = "Copiar\nTitular",
                    icono = Icons.Filled.Person,
                    color = Azul,
                    alEjecutar = {
                        if (titular.isNotBlank()) alCopiar("Titular", titular, false)
                        else alAvisar("Sin titular de cuenta")
                    }
                )
                Pair(izq, der)
            }

            TipoEntrada.CONTACTO -> {
                val tel = entrada.usuario.ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Teléfono") }
                val nom = entrada.titulo

                val izq = AccionDeslizamiento(
                    texto = "Copiar\nTeléfono",
                    icono = Icons.Filled.Phone,
                    color = Menta,
                    alEjecutar = {
                        if (tel.isNotBlank()) alCopiar("Teléfono", tel, false)
                        else alAvisar("Sin número de teléfono")
                    }
                )
                val der = AccionDeslizamiento(
                    texto = "Copiar\nNombre",
                    icono = Icons.Filled.Person,
                    color = Azul,
                    alEjecutar = {
                        if (nom.isNotBlank()) alCopiar("Nombre", nom, false)
                        else alAvisar("Sin nombre de contacto")
                    }
                )
                Pair(izq, der)
            }

            TipoEntrada.SERVIDOR -> {
                val serv = entrada.servidorEfectivo
                val host = serv?.host?.ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Host o IP") } ?: ""
                val pass = serv?.clavePrivada?.ifBlank { entrada.contrasena } ?: entrada.contrasena

                val izq = AccionDeslizamiento(
                    texto = "Copiar\nHost / IP",
                    icono = Icons.Filled.Dns,
                    color = Menta,
                    alEjecutar = {
                        if (host.isNotBlank()) alCopiar("Host / IP", host, false)
                        else alAvisar("Sin host o IP")
                    }
                )
                val der = AccionDeslizamiento(
                    texto = "Copiar\nClave / Pass",
                    icono = Icons.Filled.Key,
                    color = Azul,
                    alEjecutar = {
                        if (pass.isNotBlank()) alCopiar("Clave / Password", pass, true)
                        else alAvisar("Sin clave o contraseña")
                    }
                )
                Pair(izq, der)
            }

            TipoEntrada.WALLET -> {
                val wal = entrada.walletEfectiva
                val dir = wal?.direccionPublica?.ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Dirección pública") } ?: ""
                val sem = wal?.fraseSemilla?.ifBlank { wal.clavePrivada } ?: ""

                val izq = AccionDeslizamiento(
                    texto = "Copiar\nDirección",
                    icono = Icons.Filled.AccountBalanceWallet,
                    color = Menta,
                    alEjecutar = {
                        if (dir.isNotBlank()) alCopiar("Dirección Wallet", dir, false)
                        else alAvisar("Sin dirección de wallet")
                    }
                )
                val der = AccionDeslizamiento(
                    texto = "Copiar\nSemilla / Clave",
                    icono = Icons.Filled.Lock,
                    color = Azul,
                    alEjecutar = {
                        if (sem.isNotBlank()) alCopiar("Semilla / Clave", sem, true)
                        else alAvisar("Sin semilla ni clave privada")
                    }
                )
                Pair(izq, der)
            }

            TipoEntrada.NOTA -> {
                val izq = AccionDeslizamiento(
                    texto = "Copiar\nNota",
                    icono = Icons.AutoMirrored.Filled.Notes,
                    color = Menta,
                    alEjecutar = {
                        if (entrada.notas.isNotBlank()) alCopiar("Nota", entrada.notas, false)
                        else alAvisar("Nota vacía")
                    }
                )
                Pair(izq, null)
            }

            TipoEntrada.PASSKEY -> {
                val usuario = entrada.usuario
                val rpId = entrada.passkey?.rpId ?: ""

                val izq = AccionDeslizamiento(
                    texto = "Copiar\nUsuario",
                    icono = Icons.Filled.Person,
                    color = Menta,
                    alEjecutar = {
                        if (usuario.isNotBlank()) alCopiar("Usuario", usuario, false)
                        else alAvisar("Sin usuario")
                    }
                )
                val der = AccionDeslizamiento(
                    texto = "Copiar\nServicio",
                    icono = Icons.Filled.Fingerprint,
                    color = Azul,
                    alEjecutar = {
                        if (rpId.isNotBlank()) alCopiar("Servicio", rpId, false)
                        else alAvisar("Sin identificador de servicio")
                    }
                )
                Pair(izq, der)
            }

            TipoEntrada.LOGIN -> {
                val izq = AccionDeslizamiento(
                    texto = "Copiar\nUsuario",
                    icono = Icons.Filled.Person,
                    color = Menta,
                    alEjecutar = {
                        if (entrada.usuario.isNotBlank()) alCopiar("Usuario", entrada.usuario, false)
                        else alAvisar("Sin usuario")
                    }
                )
                val der = AccionDeslizamiento(
                    texto = "Copiar\nContraseña",
                    icono = Icons.Filled.Key,
                    color = Azul,
                    alEjecutar = {
                        if (entrada.contrasena.isNotBlank()) alCopiar("Contraseña", entrada.contrasena, true)
                        else alAvisar("Sin contraseña")
                    }
                )
                Pair(izq, der)
            }
        }
    }
}
