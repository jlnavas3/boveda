/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Extrae la clave canónica de identidad para detectar duplicados en tipos de entrada especializados.
 * Retorna null si la entrada no es de un tipo nativo especializado o si carece del identificador esencial
 * (para evitar agrupamientos erróneos de registros incompletos o con identificadores distintos).
 */
object ExtractorClaveDuplicadoNativo {

    data class ClaveNativa(
        val identificador: String,
        val secretoOClave: String,
        val etiquetaVisual: String
    )

    fun extraer(entrada: Entrada): ClaveNativa? {
        return when (entrada.tipo) {
            TipoEntrada.WIFI -> {
                val wifi = entrada.wifiEfectivo ?: return null
                val ssidNorm = wifi.ssid.trim().lowercase()
                if (ssidNorm.isBlank()) return null
                val clave = wifi.contrasena.ifBlank { entrada.contrasena }
                ClaveNativa(
                    identificador = "wifi:$ssidNorm",
                    secretoOClave = clave,
                    etiquetaVisual = "Wi-Fi · ${wifi.ssid.trim()}"
                )
            }
            TipoEntrada.TARJETA -> {
                val tarjeta = entrada.tarjetaEfectiva ?: return null
                val numLimpio = tarjeta.numero.filter { it.isDigit() }
                if (numLimpio.length < 8) return null
                val secreto = "${tarjeta.vencimiento.trim()}|||${tarjeta.cvv.trim()}"
                val ultimosCuatro = numLimpio.takeLast(4)
                val marca = tarjeta.marca.ifBlank { tarjeta.tipo }.ifBlank { "Tarjeta" }
                ClaveNativa(
                    identificador = "tarjeta:$numLimpio",
                    secretoOClave = secreto,
                    etiquetaVisual = "$marca · •••• $ultimosCuatro"
                )
            }
            TipoEntrada.CUENTA_BANCARIA -> {
                val cuenta = entrada.cuentaBancariaEfectiva ?: return null
                val numNorm = cuenta.cbuClabe.ifBlank { cuenta.numeroCuenta }.filter { it.isLetterOrDigit() }.uppercase()
                if (numNorm.length < 6) return null
                val ultimosCuatro = numNorm.takeLast(4)
                val banco = cuenta.banco.ifBlank { "Cuenta bancaria" }
                ClaveNativa(
                    identificador = "cuenta:$numNorm",
                    secretoOClave = cuenta.swiftBic.trim(),
                    etiquetaVisual = "$banco · •••• $ultimosCuatro"
                )
            }
            TipoEntrada.IDENTIDAD -> {
                val doc = entrada.identidadEfectiva ?: return null
                val numNorm = doc.numero.filter { it.isLetterOrDigit() }.uppercase()
                if (numNorm.isBlank()) return null
                val tipoDoc = doc.tipoDocumento.ifBlank { "Documento" }
                ClaveNativa(
                    identificador = "identidad:${doc.tipoDocumento.lowercase()}:$numNorm",
                    secretoOClave = doc.caducidad.trim(),
                    etiquetaVisual = "$tipoDoc · $numNorm"
                )
            }
            TipoEntrada.SERVIDOR -> {
                val srv = entrada.servidorEfectivo ?: return null
                val hostNorm = srv.host.trim().lowercase()
                if (hostNorm.isBlank()) return null
                val pto = srv.puerto.trim().ifBlank { "22" }
                val usr = srv.usuario.trim().ifBlank { entrada.usuario.trim() }
                val pass = entrada.contrasena.ifBlank { srv.clavePrivada.trim() }
                ClaveNativa(
                    identificador = "servidor:$hostNorm:$pto:$usr",
                    secretoOClave = pass,
                    etiquetaVisual = if (usr.isNotBlank()) "$usr@$hostNorm:$pto" else "$hostNorm:$pto"
                )
            }
            TipoEntrada.WALLET -> {
                val wallet = entrada.walletEfectiva ?: return null
                val dirNorm = wallet.direccionPublica.trim().lowercase()
                if (dirNorm.isBlank()) return null
                val redNorm = wallet.red.trim().lowercase()
                val visible = if (dirNorm.length > 10) "${dirNorm.take(6)}...${dirNorm.takeLast(4)}" else dirNorm
                ClaveNativa(
                    identificador = "wallet:$redNorm:$dirNorm",
                    secretoOClave = wallet.clavePrivada.ifBlank { wallet.fraseSemilla }.trim(),
                    etiquetaVisual = "${wallet.red} · $visible"
                )
            }
            else -> null
        }
    }

    fun esTipoEspecializado(tipo: TipoEntrada): Boolean {
        return tipo in setOf(
            TipoEntrada.TARJETA,
            TipoEntrada.WIFI,
            TipoEntrada.CUENTA_BANCARIA,
            TipoEntrada.IDENTIDAD,
            TipoEntrada.SERVIDOR,
            TipoEntrada.WALLET
        )
    }
}
