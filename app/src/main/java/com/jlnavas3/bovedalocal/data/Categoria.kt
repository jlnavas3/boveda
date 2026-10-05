package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Representa una Categoria o carpeta tematica definida por el usuario
 * (ej. Finanzas, Trabajo, Entretenimiento, Redes Sociales).
 */
@Serializable
data class Categoria(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val icono: String = "carpeta",
    val colorHex: String? = null,
    val creadaEn: Long = System.currentTimeMillis(),
    val modificadaEn: Long = System.currentTimeMillis()
)
