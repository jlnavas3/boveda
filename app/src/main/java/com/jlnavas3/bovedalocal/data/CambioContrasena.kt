package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

/** Una contraseña anterior de una entrada, con la fecha en la que dejó de usarse. */
@Serializable
data class CambioContrasena(val contrasena: String, val cambiadaEn: Long)
