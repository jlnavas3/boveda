package com.jlnavas3.bovedalocal.util

data class InfoRedLocal(
    val esRedPrivada: Boolean,
    val esRouterOPuertaEnlace: Boolean,
    val hostOIp: String,
    val puerto: String? = null,
    val servicioDetectado: String? = null
)
