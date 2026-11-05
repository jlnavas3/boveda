/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Microcomponente desacoplado encargado de resolver el subtítulo de alto valor
 * para una [Entrada] en el listado principal, sin etiquetas ni prefijos técnicos redundantes.
 */
object ResolverSubtituloEntrada {

    fun resolver(
        entrada: Entrada,
        identidadAsociada: Identidad? = null,
        ocultarEmailIdentidad: Boolean = false,
        ajustes: AjustesApp? = null
    ): String {
        return when (entrada.tipo) {
            TipoEntrada.LOGIN -> {
                if (ocultarEmailIdentidad && identidadAsociada != null) {
                    entrada.urls.firstOrNull() ?: entrada.notas.take(25).ifBlank { "Cuenta vinculada" }
                } else {
                    entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" }
                }
            }

            TipoEntrada.PASSKEY -> {
                val passkey = entrada.passkey
                val usuario = entrada.usuario.ifBlank { passkey?.usuario.orEmpty() }
                val rpId = passkey?.rpId.orEmpty()
                when {
                    usuario.isNotBlank() && rpId.isNotBlank() -> "$usuario · $rpId"
                    usuario.isNotBlank() -> usuario
                    rpId.isNotBlank() -> "Passkey · $rpId"
                    else -> "Passkey"
                }
            }

            TipoEntrada.TARJETA -> {
                val tar = entrada.tarjetaEfectiva
                val num = tar?.numero?.filter { it.isDigit() }.orEmpty()
                val ultimos4 = if (num.length >= 4) num.takeLast(4) else num
                val marca = tar?.marca?.trim().orEmpty()
                val titular = tar?.titular?.trim().orEmpty()

                when {
                    ultimos4.isNotBlank() && marca.isNotBlank() -> "$marca · •••• $ultimos4"
                    ultimos4.isNotBlank() -> "•••• $ultimos4"
                    titular.isNotBlank() && marca.isNotBlank() -> "$marca · $titular"
                    titular.isNotBlank() -> titular
                    marca.isNotBlank() -> marca
                    else -> "Tarjeta bancaria"
                }
            }

            TipoEntrada.WIFI -> {
                val wifi = entrada.wifiEfectivo
                val ssid = wifi?.ssid?.trim().orEmpty()
                val seguridad = wifi?.tipoSeguridad?.trim().orEmpty()

                when {
                    ssid.isNotBlank() && seguridad.isNotBlank() && !seguridad.equals("WPA2", ignoreCase = true) -> "$seguridad · $ssid"
                    ssid.isNotBlank() -> ssid
                    else -> entrada.titulo.ifBlank { "Red Wi-Fi" }
                }
            }

            TipoEntrada.CUENTA_BANCARIA -> {
                val cta = entrada.cuentaBancariaEfectiva
                val banco = cta?.banco?.trim().orEmpty()
                val numCuenta = cta?.numeroCuenta?.trim().orEmpty()
                val ultimos4 = if (numCuenta.length >= 4) numCuenta.takeLast(4) else numCuenta
                val tipoCuenta = cta?.tipoCuenta?.trim().orEmpty()

                when {
                    banco.isNotBlank() && ultimos4.isNotBlank() -> "$banco · •••• $ultimos4"
                    banco.isNotBlank() && tipoCuenta.isNotBlank() -> "$banco · $tipoCuenta"
                    banco.isNotBlank() -> banco
                    ultimos4.isNotBlank() -> "•••• $ultimos4"
                    tipoCuenta.isNotBlank() -> tipoCuenta
                    else -> entrada.usuario.ifBlank { "Cuenta bancaria" }
                }
            }

            TipoEntrada.IDENTIDAD -> {
                val iden = entrada.identidadEfectiva
                val numDoc = iden?.numero?.trim().orEmpty()
                val tipoDoc = iden?.tipoDocumento?.trim().orEmpty().ifBlank { "ID" }
                val pais = iden?.paisEmisor?.trim().orEmpty()

                when {
                    numDoc.isNotBlank() && pais.isNotBlank() -> "$tipoDoc · $numDoc ($pais)"
                    numDoc.isNotBlank() -> "$tipoDoc · $numDoc"
                    iden?.nombreCompleto?.isNotBlank() == true -> iden.nombreCompleto
                    else -> "Documento de identidad"
                }
            }

            TipoEntrada.SERVIDOR -> {
                val serv = entrada.servidorEfectivo
                val host = serv?.host?.trim().orEmpty()
                val user = serv?.usuario?.trim().orEmpty().ifBlank { entrada.usuario.trim() }
                val port = serv?.puerto?.trim().orEmpty().ifBlank { "22" }

                when {
                    user.isNotBlank() && host.isNotBlank() && port != "22" -> "$user@$host:$port"
                    user.isNotBlank() && host.isNotBlank() -> "$user@$host"
                    host.isNotBlank() && port != "22" -> "$host:$port"
                    host.isNotBlank() -> host
                    user.isNotBlank() -> user
                    else -> "Servidor SSH"
                }
            }

            TipoEntrada.WALLET -> {
                val wal = entrada.walletEfectiva
                val red = wal?.red?.trim().orEmpty()
                val dir = wal?.direccionPublica?.trim().orEmpty()
                val dirAbreviada = if (dir.length > 12) "${dir.take(6)}...${dir.takeLast(4)}" else dir

                when {
                    red.isNotBlank() && dirAbreviada.isNotBlank() -> "$red · $dirAbreviada"
                    dirAbreviada.isNotBlank() -> dirAbreviada
                    red.isNotBlank() -> red
                    else -> "Cripto Wallet"
                }
            }

            TipoEntrada.NOTA -> {
                val primeraLinea = entrada.notas.lines().firstOrNull { it.isNotBlank() }?.trim().orEmpty()
                if (primeraLinea.isNotBlank()) {
                    if (primeraLinea.length > 35) "${primeraLinea.take(32)}..." else primeraLinea
                } else {
                    "Nota segura"
                }
            }

            TipoEntrada.CONTACTO -> {
                val tel = entrada.usuario.trim()
                val telFormateado = if (ajustes != null && tel.isNotBlank()) {
                    FormateadorCampos.aplicarMascaraTelefono(tel, ajustes.formatoTelefono)
                } else tel

                if (telFormateado.isNotBlank()) {
                    telFormateado
                } else {
                    entrada.urls.firstOrNull() ?: "Contacto"
                }
            }
        }
    }
}
