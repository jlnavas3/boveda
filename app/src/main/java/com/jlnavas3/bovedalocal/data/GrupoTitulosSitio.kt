package com.jlnavas3.bovedalocal.data

data class GrupoTitulosSitio(
    val dominioClave: String,
    val nombreSugerido: String,
    val nombrePersonalizado: String = nombreSugerido,
    val entradas: List<Entrada> = emptyList(),
    val tieneColision: Boolean = entradas.size > 1
) {
    val nombreEfectivo: String
        get() = nombrePersonalizado.ifBlank { nombreSugerido }
}
