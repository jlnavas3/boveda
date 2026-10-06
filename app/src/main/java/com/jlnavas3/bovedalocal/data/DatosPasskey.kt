package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

@Serializable
data class DatosPasskey(
    val rpId: String,
    val rpName: String,
    val userHandle: String,
    val credId: String,
    val clavePrivada: String,
    val algoritmo: String = "ES256",
    val usuario: String = ""
)
