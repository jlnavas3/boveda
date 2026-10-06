package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContenidoBoveda(
    val version: Int = 1,
    val entradas: List<Entrada> = emptyList(),
    /** Entradas borradas pero aún recuperables; se vacían solas pasado un tiempo. */
    val papelera: List<Entrada> = emptyList(),
    @SerialName("colecciones")
    val categorias: List<Categoria> = emptyList(),
    val identidades: List<Identidad> = emptyList()
) {
    /** Alias de compatibilidad hacia atrás */
    val colecciones: List<Categoria> get() = categorias
}
