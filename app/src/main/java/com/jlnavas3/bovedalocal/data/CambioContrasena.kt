/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

/** Una contraseña anterior de una entrada, con la fecha en la que dejó de usarse. */
@Serializable
data class CambioContrasena(val contrasena: String, val cambiadaEn: Long)
