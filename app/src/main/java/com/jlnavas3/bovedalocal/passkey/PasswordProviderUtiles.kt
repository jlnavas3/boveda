/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.passkey

import androidx.credentials.provider.CallingAppInfo
import com.jlnavas3.bovedalocal.util.Dominios

fun objetivoSolicitante(paquete: String, origen: String?): String =
    origen?.takeIf { it.isNotBlank() }?.let { Dominios.raiz(it) } ?: paquete

fun objetivoSolicitante(info: CallingAppInfo?): String {
    if (info == null) return ""
    val origen = runCatching {
        if (info.isOriginPopulated()) info.getOrigin(info.packageName) else null
    }.getOrNull()
    return objetivoSolicitante(info.packageName, origen)
}