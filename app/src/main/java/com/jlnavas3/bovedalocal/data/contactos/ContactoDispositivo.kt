package com.jlnavas3.bovedalocal.data.contactos

/**
 * Modelo inmutable para representar un contacto obtenido de la agenda del dispositivo.
 */
data class ContactoDispositivo(
    val id: String,
    val nombre: String,
    val telefonos: List<String> = emptyList(),
    val correos: List<String> = emptyList(),
    val organizacion: String? = null,
    val cargo: String? = null,
    val notas: String? = null
)
