package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable
import java.util.UUID

/** Registro en el historial temporal de contraseñas generadas recientemente. */
@Serializable
data class RegistroClaveGenerada(
    val id: String = UUID.randomUUID().toString(),
    val clave: String,
    val generadaEn: Long = System.currentTimeMillis(),
    val origen: String = "Generador Rápido"
)
