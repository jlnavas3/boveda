package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import com.jlnavas3.bovedalocal.camara.LectorQr
import com.jlnavas3.bovedalocal.data.Entrada
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
    data class Error(val mensaje: String) : ResultadoProcesoQr
}

/**
 * Valida y procesa el contenido textual decodificado de un código QR (cámara o imagen).
 * Ahora con soporte universal: Google Auth, Passkeys, Transferencia Bóveda, vCard, Wi-Fi y TOTP.
 */
fun procesarLecturaQr(
    texto: String,
    origen: String,
    entradaDestino: String?,
    vm: VaultViewModel
): ResultadoProcesoQr {
    val limpio = texto.trim()

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
