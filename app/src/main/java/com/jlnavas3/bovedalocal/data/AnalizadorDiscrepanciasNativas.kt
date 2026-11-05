/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Analiza discrepancias específicas entre los submodelos nativos especializados
 * (Wi-Fi, Tarjetas, Cuentas bancarias, Documentos, Servidores, Wallets) para
 * advertir al usuario con claridad antes de una unificación.
 */
object AnalizadorDiscrepanciasNativas {

    fun analizar(entradas: List<Entrada>, principal: Entrada?): List<String> {
        if (entradas.size < 2) return emptyList()
        val advertencias = mutableListOf<String>()

        // 1. Wi-Fi: Detección de claves distintas para redes con el mismo SSID o SSIDs distintos
        val wifis = entradas.mapNotNull { it.wifiEfectivo }
        if (wifis.size > 1) {
            val ssids = wifis.map { it.ssid.trim() }.filter { it.isNotBlank() }.distinct()
            val claves = wifis.map { it.contrasena.trim() }.filter { it.isNotBlank() }.distinct()
            if (ssids.size > 1) {
                advertencias.add("Las redes Wi-Fi tienen nombres (SSID) diferentes: ${ssids.joinToString(", ")}. Se conservará '${principal?.wifiEfectivo?.ssid ?: ssids.first()}' como principal.")
            } else if (claves.size > 1) {
                advertencias.add("Las redes Wi-Fi comparten el mismo SSID pero tienen contraseñas distintas. La contraseña secundaria se resguardará en el historial.")
            }
        }

        // 2. Tarjetas bancarias: Distintos números de tarjeta
        val tarjetas = entradas.mapNotNull { it.tarjetaEfectiva }
        if (tarjetas.size > 1) {
            val numeros = tarjetas.map { it.numero.filter { c -> c.isDigit() } }.filter { it.isNotBlank() }.distinct()
            if (numeros.size > 1) {
                advertencias.add("Las tarjetas seleccionadas tienen números distintos (${numeros.size} plásticos). Los datos de la tarjeta secundaria se archivarán en notas.")
            }
        }

        // 3. Cuentas bancarias: Distintos IBANs o números de cuenta
        val cuentas = entradas.mapNotNull { it.cuentaBancariaEfectiva }
        if (cuentas.size > 1) {
            val ibans = cuentas.map { it.cbuClabe.ifBlank { it.numeroCuenta }.filter { c -> c.isLetterOrDigit() } }.filter { it.isNotBlank() }.distinct()
            if (ibans.size > 1) {
                advertencias.add("Las cuentas bancarias tienen números o IBANs distintos. Los datos secundarios se respaldarán en notas.")
            }
        }

        // 4. Documentos de identidad: Distintos números de documento
        val docs = entradas.mapNotNull { it.identidadEfectiva }
        if (docs.size > 1) {
            val nums = docs.map { it.numero.filter { c -> c.isLetterOrDigit() } }.filter { it.isNotBlank() }.distinct()
            if (nums.size > 1) {
                advertencias.add("Los documentos de identidad tienen números distintos. El documento secundario se respaldará en notas.")
            }
        }

        // 5. Servidores SSH: Distintos hosts o usuarios
        val servidores = entradas.mapNotNull { it.servidorEfectivo }
        if (servidores.size > 1) {
            val hosts = servidores.map { "${it.host.trim()}:${it.puerto.trim()}" }.filter { it != ":" }.distinct()
            if (hosts.size > 1) {
                advertencias.add("Los servidores SSH apuntan a hosts o puertos diferentes: ${hosts.joinToString(", ")}.")
            }
        }

        // 6. Cripto Wallets: Distintas direcciones públicas
        val wallets = entradas.mapNotNull { it.walletEfectiva }
        if (wallets.size > 1) {
            val dirs = wallets.map { it.direccionPublica.trim().lowercase() }.filter { it.isNotBlank() }.distinct()
            if (dirs.size > 1) {
                advertencias.add("Las wallets tienen direcciones públicas distintas en la blockchain.")
            }
        }

        return advertencias
    }
}
