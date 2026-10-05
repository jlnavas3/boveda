package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Representa una Identidad o perfil de usuario (ej. Personal, Trabajo, Compras)
 * vinculado a una dirección de correo principal y alias/correos secundarios.
 */
@Serializable
data class Identidad(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val correoPrincipal: String,
    val correosSecundarios: List<String> = emptyList(),
    val colorHex: String? = null,
    val icono: String = "person",
    val creadaEn: Long = System.currentTimeMillis(),
    val modificadaEn: Long = System.currentTimeMillis()
)
