package com.jlnavas3.bovedalocal.util

/**
 * Representa un dominio desglosado con su host original,
 * su dominio base registrable (eTLD+1) y la lista de hosts candidatos
 * ordenados por prioridad para resolución de App Links.
 */
data class DominioNormalizado(
    val hostOriginal: String,
    val dominioRaiz: String?,
    val hostsCandidatos: List<String>
)
