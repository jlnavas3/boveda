package com.jlnavas3.bovedalocal.cxf

import com.jlnavas3.bovedalocal.data.Entrada

data class ResultadoConversionCxf(
    val entradas: List<Entrada>,
    val totalPasskeys: Int,
    val totalContrasenas: Int,
    val totalTotp: Int,
    val exportador: String? = null
)
