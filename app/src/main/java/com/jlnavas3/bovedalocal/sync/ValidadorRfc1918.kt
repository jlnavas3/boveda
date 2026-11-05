/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

/**
 * Guardián de seguridad de red RFC 1918 para sincronización P2P local.
 *
 * Valida de forma estricta que una dirección IP sea una IPv4 privada canónica:
 * - 10.0.0.0/8 (10.0.0.0 - 10.255.255.255)
 * - 172.16.0.0/12 (172.16.0.0 - 172.31.255.255)
 * - 192.168.0.0/16 (192.168.0.0 - 192.168.255.255)
 *
 * Rechaza tajantemente:
 * - Nombres de dominio (previene DNS Rebinding)
 * - Direcciones loopback (127.0.0.0/8)
 * - Direcciones link-local (169.254.0.0/16)
 * - Direcciones multicast (224.0.0.0/4)
 * - Broadcast (255.255.255.255)
 * - IPs públicas de internet
 * - Notaciones ambiguas con ceros a la izquierda (ej. 010.0.0.1)
 */
object ValidadorRfc1918 {

    /**
     * Determina si la cadena dada representa una IPv4 privada estricta según RFC 1918.
     */
    fun esIpPrivadaRfc1918(ip: String): Boolean {
        val partes = ip.trim().split('.')
        if (partes.size != 4) return false

        val octetos = IntArray(4)
        for (i in 0 until 4) {
            val seg = partes[i]
            if (seg.isEmpty() || seg.length > 3) return false
            // Prohibir ceros a la izquierda para evitar interpretación octal ambigua
            if (seg.length > 1 && seg.startsWith('0')) return false
            val num = seg.toIntOrNull() ?: return false
            if (num !in 0..255) return false
            octetos[i] = num
        }

        val o0 = octetos[0]
        val o1 = octetos[1]

        // 10.0.0.0/8
        if (o0 == 10) return true

        // 172.16.0.0/12 (172.16.0.0 a 172.31.255.255)
        if (o0 == 172 && o1 in 16..31) return true

        // 192.168.0.0/16
        if (o0 == 192 && o1 == 168) return true

        return false
    }
}
