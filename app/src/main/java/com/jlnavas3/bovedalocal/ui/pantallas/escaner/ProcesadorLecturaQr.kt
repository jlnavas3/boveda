package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import com.jlnavas3.bovedalocal.camara.LectorQr
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.GoogleAuthMigration

sealed interface ResultadoProcesoQr {
    object Exito : ResultadoProcesoQr
    data class Error(val mensaje: String) : ResultadoProcesoQr
}

/**
 * Valida y procesa el contenido textual decodificado de un código QR (cámara o imagen).
 */
fun procesarLecturaQr(
    texto: String,
    origen: String,
    entradaDestino: String?,
    vm: VaultViewModel
): ResultadoProcesoQr {
    if (GoogleAuthMigration.esEnlaceMigracion(texto)) {
        Diagnostico.apuntar("2fa", "Escaneo detectó migración de Google Authenticator")
        vm.ir(Pantalla.ConfirmarMigracion(texto))
        return ResultadoProcesoQr.Exito
    }
    if (LectorQr.esQrDePasskey(texto)) {
        Diagnostico.apuntar("2fa", "Escaneo detectó un QR de Passkey en lugar de TOTP")
        return ResultadoProcesoQr.Error("Este QR es una Passkey, no un código 2FA.")
    }
    if (!vm.altaTotp(texto, entradaDestino)) {
        Diagnostico.apuntar("2fa", "Formato de clave 2FA no reconocido ($origen)")
        return ResultadoProcesoQr.Error("El código QR no contiene un doble factor válido.")
    }
    val detalle = if (origen == "la cámara") "cámara inmersiva" else origen
    Diagnostico.apuntar("2fa", "Doble factor añadido exitosamente ($detalle)")
    vm.avisar("Doble factor añadido")
    vm.volverAtras()
    return ResultadoProcesoQr.Exito
}
