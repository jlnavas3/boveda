/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

data class InfoRedLocal(
    val esRedPrivada: Boolean,
    val esRouterOPuertaEnlace: Boolean,
    val hostOIp: String,
    val puerto: String? = null,
    val servicioDetectado: String? = null
)
