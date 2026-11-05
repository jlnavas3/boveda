/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

/**
 * Representación inmutable del contenido verificado de un código QR de emparejamiento LAN P2P.
 * Espejo canónico de `src/sync/p2p/sync_uri.rs` en Rust.
 *
 * @property ips Lista de direcciones IPv4 privadas RFC 1918 de la contraparte.
 * @property puerto Puerto TCP de escucha (1024..=65535).
 * @property clavePublicaDesktop Clave pública efímera X25519 (32 bytes).
 * @property tokenPsk Token efímero Pre-Shared Key (32 bytes).
 * @property version Versión del protocolo de sincronización (predeterminado 2).
 */
data class SyncQrPayload(
    val ips: List<String>,
    val puerto: Int,
    val clavePublicaDesktop: ByteArray,
    val tokenPsk: ByteArray,
    val version: Int = 2,
    val aliasDispositivo: String? = null
) {
    init {
        require(ips.isNotEmpty()) { "La lista de IPs no puede estar vacía." }
        require(puerto in 1024..65535) { "Puerto fuera de rango válido (1024..65535): $puerto" }
        require(clavePublicaDesktop.size == 32) { "Clave pública debe tener 32 bytes." }
        require(tokenPsk.size == 32) { "Token PSK debe tener 32 bytes." }
        require(version >= 1) { "Versión del protocolo no soportada: $version" }
    }

    /** Huella visual determinista de 6 bytes formateada en mayúsculas (ej. "74:5A:63:02:1A:A5"). */
    val huellaVisual: String get() = HuellaVisualP2P.calcular(clavePublicaDesktop, tokenPsk)

    /** Clave pública codificada en 64 caracteres hexadecimales minúsculas. */
    val clavePublicaHex: String get() = CalculadorHashEntrada.bytesAHex(clavePublicaDesktop)

    /** Token PSK codificado en 64 caracteres hexadecimales minúsculas. */
    val tokenPskHex: String get() = CalculadorHashEntrada.bytesAHex(tokenPsk)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SyncQrPayload) return false
        if (ips != other.ips) return false
        if (puerto != other.puerto) return false
        if (!clavePublicaDesktop.contentEquals(other.clavePublicaDesktop)) return false
        if (!tokenPsk.contentEquals(other.tokenPsk)) return false
        if (version != other.version) return false
        if (aliasDispositivo != other.aliasDispositivo) return false
        return true
    }

    override fun hashCode(): Int {
        var result = ips.hashCode()
        result = 31 * result + puerto
        result = 31 * result + clavePublicaDesktop.contentHashCode()
        result = 31 * result + tokenPsk.contentHashCode()
        result = 31 * result + version
        result = 31 * result + (aliasDispositivo?.hashCode() ?: 0)
        return result
    }
}
