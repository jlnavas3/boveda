/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import java.util.Base64
import java.util.Locale

/**
 * Representa una trama individual de código QR óptico (`bvda-opt://`).
 *
 * Formato canónico:
 * `bvda-opt://v1/<indice>/<total>/<id_sesion_hex8>/<crc32_total_hex8>?d=<chunk_base64url>`
 */
data class TramaOpticaQr(
    val version: Int = 1,
    val indice: Int,
    val total: Int,
    val idSesionHex: String,
    val crc32TotalHex: String,
    val datosChunk: ByteArray,
    val aliasEmisor: String? = null
) {

    fun serializarUri(): String {
        val b64 = Base64.getUrlEncoder().withoutPadding().encodeToString(datosChunk)
        val idNorm = idSesionHex.uppercase(Locale.US)
        val crcNorm = crc32TotalHex.uppercase(Locale.US)
        val aliasParam = if (!aliasEmisor.isNullOrBlank()) {
            "&a=" + java.net.URLEncoder.encode(aliasEmisor, "UTF-8")
        } else ""
        return "bvda-opt://v$version/$indice/$total/$idNorm/$crcNorm?d=$b64$aliasParam"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TramaOpticaQr) return false
        return version == other.version &&
            indice == other.indice &&
            total == other.total &&
            idSesionHex.equals(other.idSesionHex, ignoreCase = true) &&
            crc32TotalHex.equals(other.crc32TotalHex, ignoreCase = true) &&
            datosChunk.contentEquals(other.datosChunk) &&
            aliasEmisor == other.aliasEmisor
    }

    override fun hashCode(): Int {
        var result = version
        result = 31 * result + indice
        result = 31 * result + total
        result = 31 * result + idSesionHex.uppercase(Locale.US).hashCode()
        result = 31 * result + crc32TotalHex.uppercase(Locale.US).hashCode()
        result = 31 * result + datosChunk.contentHashCode()
        result = 31 * result + (aliasEmisor?.hashCode() ?: 0)
        return result
    }

    companion object {
        const val ESQUEMA = "bvda-opt://"

        fun esUriOptica(uri: String): Boolean {
            return uri.trim().startsWith(ESQUEMA, ignoreCase = true)
        }

        fun parsearUri(uri: String): TramaOpticaQr? {
            val u = uri.trim()
            if (!esUriOptica(u)) return null

            return try {
                val sinEsquema = u.substring(ESQUEMA.length)
                val partesRutaYQuery = sinEsquema.split('?', limit = 2)
                val partesRuta = partesRutaYQuery[0].split('/')

                if (partesRuta.size < 5) return null

                val versionStr = partesRuta[0].removePrefix("v").removePrefix("V")
                val version = versionStr.toIntOrNull() ?: 1
                val indice = partesRuta[1].toIntOrNull() ?: return null
                val total = partesRuta[2].toIntOrNull() ?: return null
                val idSesion = partesRuta[3].uppercase(Locale.US)
                val crc32 = partesRuta[4].uppercase(Locale.US)

                if (indice < 1 || total < 1 || indice > total) return null
                if (idSesion.length != 8 || crc32.length != 8) return null

                val query = partesRutaYQuery.getOrNull(1) ?: return null
                var payloadB64: String? = null
                var aliasEmisor: String? = null
                for (param in query.split('&')) {
                    val kv = param.split('=', limit = 2)
                    if (kv.size == 2) {
                        when (kv[0]) {
                            "d" -> payloadB64 = kv[1]
                            "a" -> aliasEmisor = try {
                                java.net.URLDecoder.decode(kv[1], "UTF-8")
                            } catch (_: Exception) {
                                kv[1]
                            }
                        }
                    }
                }
                if (payloadB64.isNullOrBlank()) return null

                val datosChunk = Base64.getUrlDecoder().decode(payloadB64)
                TramaOpticaQr(
                    version = version,
                    indice = indice,
                    total = total,
                    idSesionHex = idSesion,
                    crc32TotalHex = crc32,
                    datosChunk = datosChunk,
                    aliasEmisor = aliasEmisor
                )
            } catch (_: Exception) {
                null
            }
        }
    }
}
