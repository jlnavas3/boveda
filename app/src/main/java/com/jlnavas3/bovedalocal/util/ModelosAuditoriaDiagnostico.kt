package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.crypto.KdfParams
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2

/**
 * Una línea del estado en vivo. [ok] es null para líneas informativas (modelo, versión,
 * motor ajustado...) y true/false para lo que sí es un sí-o-no comprobable ahora mismo,
 * así la pantalla pinta ✔ o ✖ en vez de dejar que el usuario lo adivine leyendo texto.
 */
data class Linea(
    val texto: String,
    val ok: Boolean? = null,
    val indentada: Boolean = false,
    val detalle: String? = null
)

data class DatosAuditoria(
    val fabricante: String,
    val modelo: String,
    val dispositivo: String,
    val placa: String,
    val soc: String?,
    val abis: String,
    val nucleosCpu: Int,
    val versionAndroid: String,
    val apiSdk: Int,
    val parcheSeguridad: String,
    val compilacion: String,
    val ramTotalMb: Long,
    val ramLibreMb: Long,
    val ramBaja: Boolean,
    val heapMaxMb: Long,
    val heapUsadoMb: Long,
    val almacenamientoLibreMb: Long,
    val almacenamientoTotalMb: Long,
    val rutaBoveda: String,
    val tamanoBovedaBytes: Long,
    val tienePermisoInternet: Boolean,
    val flagSecureActivo: Boolean,
    val permisosDeclarados: List<String>,
    val lineasBiometria: List<Linea>,
    val lineasCamara: List<Linea>,
    val perfilArgon2: PerfilArgon2,
    val kdfParams: KdfParams
)
