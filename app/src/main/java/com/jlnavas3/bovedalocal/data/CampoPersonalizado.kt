package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CampoPersonalizado(
    val id: String = UUID.randomUUID().toString(),
    val etiqueta: String = "",
    val valor: String = "",
    val tipo: TipoCampo = TipoCampo.TEXTO,
    val esSensible: Boolean = false,
    val esObligatorio: Boolean = false,
    val formato: String? = null
) {
    @Suppress("DEPRECATION")
    val esSensibleEfectivo: Boolean
        get() = esSensible || tipo == TipoCampo.PIN || tipo == TipoCampo.OCULTO
}
