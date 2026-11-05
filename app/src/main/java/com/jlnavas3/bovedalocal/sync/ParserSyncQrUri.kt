/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

/**
 * Analizador y serializador canónico de URIs de sincronización P2P (`bvda-sync://`).
 * Espejo canónico de `src/sync/p2p/sync_uri.rs` en Rust.
 *
 * Formato del contrato:
 * `bvda-sync://192.168.1.50,10.0.0.15:54321?k=<64_hex_chars>&t=<64_hex_chars>&v=2`
 *
 * Valida de forma rigurosa:
 * - Esquema `bvda-sync://`
 * - Cada IP listada debe ser obligatoriamente IPv4 privada según RFC 1918.
 * - Rechazo total de nombres de host, dominios, loopback o IPs públicas.
 * - Puerto en rango no privilegiado (1024..=65535).
 * - Clave pública `k` de 32 bytes (64 caracteres hex).
 * - Token PSK `t` de 32 bytes (64 caracteres hex).
 * - Versión `v` >= 2.
 */
object ParserSyncQrUri {

    const val ESQUEMA = "bvda-sync://"

    /**
     * Comprueba de forma rápida si una cadena inicia con el esquema `bvda-sync://`.
     */
    fun esUriSync(texto: String): Boolean {
        return texto.trim().startsWith(ESQUEMA, ignoreCase = true)
    }

    /**
     * Parsea una cadena URI y retorna un [Result] con el [SyncQrPayload] verificado o una excepción descriptiva.
     */
    fun parsear(texto: String): Result<SyncQrPayload> = runCatching {
        val limpio = texto.trim()
        require(esUriSync(limpio)) { "Esquema inválido. Debe comenzar con '$ESQUEMA'" }

        val sinEsquema = limpio.substring(ESQUEMA.length)
        val partes = sinEsquema.split('?', limit = 2)
        require(partes.size == 2) { "URI malformada: falta el separador de parámetros '?'" }

        val autoridad = partes[0].trim()
        val query = partes[1].trim()

        // 1. Parseo de autoridad (hosts:puerto)
        val colonIdx = autoridad.lastIndexOf(':')
        require(colonIdx > 0 && colonIdx < autoridad.length - 1) {
            "Autoridad malformada. Debe contener al menos una IP y un puerto separado por ':'"
        }

        val hostsStr = autoridad.substring(0, colonIdx)
        val puertoStr = autoridad.substring(colonIdx + 1)

        val puerto = puertoStr.toIntOrNull()
            ?: throw IllegalArgumentException("Puerto inválido: '$puertoStr'")
        require(puerto in 1024..65535) {
            "Puerto fuera del rango permitido (1024..65535): $puerto"
        }

        val ips = hostsStr.split(',').map { it.trim() }.filter { it.isNotEmpty() }
        require(ips.isNotEmpty()) { "Debe especificarse al menos una dirección IP." }

        for (ip in ips) {
            require(ValidadorRfc1918.esIpPrivadaRfc1918(ip)) {
                "Dirección IP no permitida (debe ser IPv4 privada RFC 1918): '$ip'"
            }
        }

        // 2. Parseo de query params (k, t, v, d)
        var kHex: String? = null
        var tHex: String? = null
        var version: Int? = null
        var aliasDispositivo: String? = null

        val parametros = query.split('&')
        for (param in parametros) {
            val par = param.split('=', limit = 2)
            if (par.size == 2) {
                val clave = par[0].trim()
                val valor = par[1].trim()
                when (clave.lowercase()) {
                    "k" -> kHex = valor
                    "t" -> tHex = valor
                    "v" -> version = valor.toIntOrNull()
                    "d" -> aliasDispositivo = try {
                        java.net.URLDecoder.decode(valor, "UTF-8")
                    } catch (_: Exception) {
                        valor
                    }
                }
            }
        }

        require(!kHex.isNullOrBlank()) { "Parámetro obligatorio 'k' (clave pública) ausente." }
        require(kHex.length == 64 && kHex.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }) {
            "Parámetro 'k' debe contener exactamente 64 caracteres hexadecimales (32 bytes)."
        }

        require(!tHex.isNullOrBlank()) { "Parámetro obligatorio 't' (token PSK) ausente." }
        require(tHex.length == 64 && tHex.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }) {
            "Parámetro 't' debe contener exactamente 64 caracteres hexadecimales (32 bytes)."
        }

        require(version != null && version >= 1) {
            "Versión de protocolo no soportada o ausente (se requiere v>=1): '$version'"
        }

        val clavePublicaBytes = CalculadorHashEntrada.hexABytes(kHex.lowercase())
        val tokenPskBytes = CalculadorHashEntrada.hexABytes(tHex.lowercase())

        SyncQrPayload(
            ips = ips,
            puerto = puerto,
            clavePublicaDesktop = clavePublicaBytes,
            tokenPsk = tokenPskBytes,
            version = version,
            aliasDispositivo = aliasDispositivo
        )
    }

    /**
     * Serializa un [SyncQrPayload] al formato canónico de URI QR.
     */
    fun serializar(payload: SyncQrPayload): String {
        val ipsStr = payload.ips.joinToString(",")
        val base = "$ESQUEMA$ipsStr:${payload.puerto}?k=${payload.clavePublicaHex}&t=${payload.tokenPskHex}&v=${payload.version}"
        return if (!payload.aliasDispositivo.isNullOrBlank()) {
            val encodedAlias = java.net.URLEncoder.encode(payload.aliasDispositivo, "UTF-8")
            "$base&d=$encodedAlias"
        } else {
            base
        }
    }
}
