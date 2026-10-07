package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlantillaCamposPersonalizada(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String = "",
    val campos: List<CampoPersonalizado> = emptyList()
)
