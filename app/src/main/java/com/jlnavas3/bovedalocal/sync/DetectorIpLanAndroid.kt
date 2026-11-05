/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Collections

/**
 * Microcomponente puro para detectar las direcciones IPv4 privadas (RFC 1918)
 * de las interfaces de red de área local (Wi-Fi, Hotspot/Punto de acceso, Tethering USB y Ethernet)
 * disponibles en el dispositivo Android para el rol de Servidor / Anfitrión P2P.
 */
object DetectorIpLanAndroid {

    private val PREFIJOS_EXCLUIDOS = listOf(
        "rmnet", "ccmni", "pdp", "tun", "tap", "wg", "dummy", "lo"
    )

    /**
     * Retorna la lista priorizada de IPs RFC 1918 activas en el dispositivo.
     * Prioridad: Wi-Fi (`wlan*`), Hotspot (`ap*`, `swlan*`), USB Tethering (`rndis*`), Ethernet (`eth*`).
     */
    fun obtenerIpsLanPrivadas(): List<String> {
        val interfaces = try {
            Collections.list(NetworkInterface.getNetworkInterfaces())
        } catch (_: Exception) {
            emptyList<NetworkInterface>()
        }

        val ipsWifi = mutableListOf<String>()
        val ipsHotspot = mutableListOf<String>()
        val ipsOtros = mutableListOf<String>()

        for (iface in interfaces) {
            try {
                if (!iface.isUp || iface.isLoopback) continue

                val nombre = iface.name.lowercase()
                if (PREFIJOS_EXCLUIDOS.any { nombre.startsWith(it) }) continue

                for (addr in Collections.list(iface.inetAddresses)) {
                    if (addr is Inet4Address && !addr.isLoopbackAddress) {
                        val host = addr.hostAddress ?: continue
                        if (ValidadorRfc1918.esIpPrivadaRfc1918(host)) {
                            when {
                                nombre.startsWith("wlan") -> ipsWifi.add(host)
                                nombre.startsWith("ap") || nombre.startsWith("swlan") || nombre.startsWith("rndis") -> ipsHotspot.add(host)
                                else -> ipsOtros.add(host)
                            }
                        }
                    }
                }
            } catch (_: Exception) {
                // Continuar con siguientes interfaces si hay permisos restringidos en alguna
            }
        }

        return (ipsWifi + ipsHotspot + ipsOtros).distinct()
    }
}
