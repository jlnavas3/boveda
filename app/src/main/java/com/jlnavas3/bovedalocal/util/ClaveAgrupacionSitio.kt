package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * El sitio por el que se agrupan varias entradas: preserva subdominios significativos
 * (ej. account.xiaomi vs xiaomi) y normaliza TLDs equivalentes (amazon.com y amazon.es -> amazon).
 */
fun claveAgrupacionSitio(entrada: Entrada): String? = when (entrada.tipo) {
    TipoEntrada.LOGIN -> {
        val web = entrada.urls.asSequence()
            .filterNot { it.trim().lowercase().startsWith("android://") }
            .map { Dominios.sitioAgrupacion(it) }
            .firstOrNull { it.isNotBlank() }
        web ?: entrada.titulo
            .takeIf { it.isNotBlank() }
            ?.let { Dominios.sitioAgrupacion(it).ifBlank { it.trim().lowercase() } }
    }
    TipoEntrada.PASSKEY -> entrada.passkey?.rpId
        ?.let { Dominios.sitioAgrupacion(it) }
        ?.takeIf { it.isNotBlank() }
    else -> null
}
