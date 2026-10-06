package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * El sitio por el que se agrupan varias entradas: aplica las reglas de subdominios,
 * marcas oficiales y redes RFC 1918 para descartar prefijos técnicos (account, accounts, sso, etc.).
 */
fun claveAgrupacionSitio(
    entrada: Entrada,
    prefijosConfigurados: List<String> = emptyList(),
    plantillaRouter: String = "Router ({ip})",
    plantillaServidor: String = "Servidor ({ip})",
    tldsConfigurados: List<String> = emptyList(),
    marcasPersonalizadas: Map<String, String> = emptyMap(),
    puertosConfigurados: Map<String, String> = emptyMap(),
    octetosRouter: List<Int> = listOf(1, 254)
): String? = when (entrada.tipo) {
    TipoEntrada.LOGIN -> {
        val web = entrada.urls.asSequence()
            .filterNot { it.trim().lowercase().startsWith("android://") }
            .firstOrNull { it.isNotBlank() }
        val target = web ?: entrada.titulo.takeIf { it.isNotBlank() }
        if (target != null) {
            NormalizadorTitulosSitios.extraerNombreBase(
                urlODominio = web ?: "",
                rawTitulo = entrada.titulo,
                prefijosConfigurados = prefijosConfigurados,
                plantillaRouter = plantillaRouter,
                plantillaServidor = plantillaServidor,
                tldsConfigurados = tldsConfigurados,
                marcasPersonalizadas = marcasPersonalizadas,
                puertosConfigurados = puertosConfigurados,
                octetosRouter = octetosRouter
            )
        } else null
    }
    TipoEntrada.PASSKEY -> entrada.passkey?.rpId
        ?.takeIf { it.isNotBlank() }
        ?.let {
            NormalizadorTitulosSitios.extraerNombreBase(
                urlODominio = it,
                rawTitulo = entrada.titulo,
                prefijosConfigurados = prefijosConfigurados,
                plantillaRouter = plantillaRouter,
                plantillaServidor = plantillaServidor,
                tldsConfigurados = tldsConfigurados,
                marcasPersonalizadas = marcasPersonalizadas,
                puertosConfigurados = puertosConfigurados,
                octetosRouter = octetosRouter
            )
        }
    else -> null
}
