/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import com.jlnavas3.bovedalocal.camara.LectorQr
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.sync.ParserSyncQrUri
import com.jlnavas3.bovedalocal.sync.SyncQrPayload
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.GoogleAuthMigration
import com.jlnavas3.bovedalocal.util.ParserBovedaQr
import com.jlnavas3.bovedalocal.util.ParserVCardQr
import com.jlnavas3.bovedalocal.util.ParserWifiQr

sealed interface ResultadoProcesoQr {
    object Exito : ResultadoProcesoQr
    data class EntradaDetectada(val entrada: Entrada) : ResultadoProcesoQr
    data class P2pSyncDetectado(val payload: SyncQrPayload) : ResultadoProcesoQr
    data class TramaOpticaDetectada(val trama: com.jlnavas3.bovedalocal.sync.TramaOpticaQr) : ResultadoProcesoQr
    data class Error(val mensaje: String) : ResultadoProcesoQr
}

/**
 * Valida y procesa el contenido textual decodificado de un código QR (cámara o imagen).
 * Ahora con soporte universal: P2P Sync LAN, Óptico bvda-opt, Google Auth, Passkeys, Transferencia Bóveda, vCard, Wi-Fi y TOTP.
 */
fun procesarLecturaQr(
    texto: String,
    origen: String,
    entradaDestino: String?,
    vm: VaultViewModel
): ResultadoProcesoQr {
    val limpio = texto.trim()

    // 0. Sincronización P2P en red local (bvda-sync://)
    if (ParserSyncQrUri.esUriSync(limpio)) {
        val res = ParserSyncQrUri.parsear(limpio)
        val payload = res.getOrNull()
        if (payload != null) {
            Diagnostico.apuntar("sync", "Escaneo detectó QR de sincronización P2P LAN con huella: ${payload.huellaVisual}")
            return ResultadoProcesoQr.P2pSyncDetectado(payload)
        } else {
            val motivo = res.exceptionOrNull()?.message ?: "formato no válido"
            Diagnostico.apuntar("sync", "Código QR bvda-sync rechazado: $motivo")
            return ResultadoProcesoQr.Error("Código QR P2P no válido: $motivo")
        }
    }

    // 0.1 Sincronización óptica comprimida (bvda-opt://)
    if (com.jlnavas3.bovedalocal.sync.TramaOpticaQr.esUriOptica(limpio)) {
        val trama = com.jlnavas3.bovedalocal.sync.TramaOpticaQr.parsearUri(limpio)
        if (trama != null) {
            return ResultadoProcesoQr.TramaOpticaDetectada(trama)
        } else {
            return ResultadoProcesoQr.Error("Fragmento óptico QR no válido o corrupto.")
        }
    }

    // 1. Migración de Google Authenticator
    if (GoogleAuthMigration.esEnlaceMigracion(limpio)) {
        Diagnostico.apuntar("2fa", "Escaneo detectó migración de Google Authenticator")
        vm.ir(Pantalla.ConfirmarMigracion(limpio))
        return ResultadoProcesoQr.Exito
    }

    // 2. Transferencia Bóveda Local
    if (ParserBovedaQr.esBovedaTransfer(limpio)) {
        val entrada = ParserBovedaQr.parsear(limpio)
        if (entrada != null) {
            Diagnostico.apuntar("qr", "Escaneo detectó transferencia estructurada de Bóveda: «${entrada.titulo}»")
            return ResultadoProcesoQr.EntradaDetectada(entrada)
        }
    }

    // 3. Contacto en formato vCard
    if (ParserVCardQr.esVCard(limpio)) {
        val contacto = ParserVCardQr.parsear(limpio)
        if (contacto != null) {
            Diagnostico.apuntar("qr", "Escaneo detectó contacto vCard: «${contacto.titulo}»")
            return ResultadoProcesoQr.EntradaDetectada(contacto)
        }
    }

    // 4. Configuración Wi-Fi
    if (ParserWifiQr.esWifi(limpio)) {
        val wifi = ParserWifiQr.parsear(limpio)
        if (wifi != null) {
            Diagnostico.apuntar("qr", "Escaneo detectó configuración Wi-Fi: «${wifi.titulo}»")
            return ResultadoProcesoQr.EntradaDetectada(wifi)
        }
    }

    // 5. Passkey
    if (LectorQr.esQrDePasskey(limpio)) {
        Diagnostico.apuntar("qr", "Escaneo detectó un QR de Passkey")
        return ResultadoProcesoQr.Error("Este QR es una Passkey FIDO2 para vincular en el navegador.")
    }

    // 6. Doble Factor (TOTP)
    if (vm.altaTotp(limpio, entradaDestino)) {
        val detalle = if (origen == "la cámara") "cámara inmersiva" else origen
        Diagnostico.apuntar("2fa", "Doble factor añadido exitosamente ($detalle)")
        vm.avisar("Doble factor añadido")
        vm.volverAtras()
        return ResultadoProcesoQr.Exito
    }

    // 7. Formato no compatible
    Diagnostico.apuntar("qr", "Formato de código QR no reconocido ($origen)")
    return ResultadoProcesoQr.Error("Código QR no reconocido o sin formato compatible.")
}
