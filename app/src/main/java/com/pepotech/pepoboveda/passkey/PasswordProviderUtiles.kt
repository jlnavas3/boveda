package com.pepotech.pepoboveda.passkey

import androidx.credentials.provider.CallingAppInfo
import com.pepotech.pepoboveda.util.Dominios

fun objetivoSolicitante(paquete: String, origen: String?): String =
    origen?.takeIf { it.isNotBlank() }?.let { Dominios.raiz(it) } ?: paquete

fun objetivoSolicitante(info: CallingAppInfo?): String {
    if (info == null) return ""
    val origen = runCatching {
        if (info.isOriginPopulated()) info.getOrigin(info.packageName) else null
    }.getOrNull()
    return objetivoSolicitante(info.packageName, origen)
}