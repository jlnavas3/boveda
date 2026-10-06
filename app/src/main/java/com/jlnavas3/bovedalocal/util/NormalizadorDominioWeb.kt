package com.jlnavas3.bovedalocal.util

import com.google.common.net.InternetDomainName
import java.net.URI

/**
 * Normaliza URLs y dominios extrayendo el host limpio, el dominio registrable efectivo (eTLD+1)
 * con Guava InternetDomainName y los candidatos ordenados para resolución jerárquica.
 */
object NormalizadorDominioWeb {

    fun normalizar(rawUrlODominio: String): DominioNormalizado? {
        val host = extraerHost(rawUrlODominio) ?: return null
        return try {
            val domainName = InternetDomainName.from(host)
            if (!domainName.isPublicSuffix && domainName.hasPublicSuffix()) {
                val topPrivate = domainName.topPrivateDomain().toString()
                val candidatos = linkedSetOf<String>().apply {
                    add(host)
                    add(topPrivate)
                    add("www.$topPrivate")
                }.toList()
                DominioNormalizado(
                    hostOriginal = host,
                    dominioRaiz = topPrivate,
                    hostsCandidatos = candidatos
                )
            } else {
                DominioNormalizado(host, null, listOf(host))
            }
        } catch (_: Exception) {
            val candidatos = linkedSetOf<String>().apply {
                add(host)
                val partes = host.split(".")
                if (partes.size >= 2) {
                    val raiz = partes.takeLast(2).joinToString(".")
                    add(raiz)
                    add("www.$raiz")
                }
            }.toList()
            DominioNormalizado(host, null, candidatos)
        }
    }

    private fun extraerHost(raw: String): String? {
        val limpio = raw.trim().lowercase()
        if (limpio.isBlank()) return null
        val conEsquema = if (!limpio.startsWith("http://") && !limpio.startsWith("https://")) {
            "https://$limpio"
        } else {
            limpio
        }
        return try {
            val uri = URI(conEsquema)
            uri.host?.removePrefix("www.")?.ifEmpty { null } ?: uri.host
        } catch (_: Exception) {
            limpio.removePrefix("https://").removePrefix("http://")
                .substringBefore("/")
                .substringBefore(":")
                .removePrefix("www.")
                .ifEmpty { null }
        }
    }
}
