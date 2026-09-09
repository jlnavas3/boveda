package com.pepotech.pepoboveda.util

import com.google.common.net.InternetDomainName

object Dominios {

    fun host(entrada: String): String {
        val texto = entrada.trim().lowercase()
        if (texto.isEmpty()) return ""
        val sinEsquema = texto.substringAfter("://", texto)
        val sinRuta = sinEsquema.substringBefore('/').substringBefore('?').substringBefore('#')
        val sinCredenciales = sinRuta.substringAfterLast('@')
        return sinCredenciales.substringBefore(':').removePrefix("www.")
    }

    /** Dominio registrable ("raíz") de una URL o host. */
    fun raiz(entrada: String): String {
        val h = host(entrada)
        if (h.isEmpty() || !h.contains('.')) return h
        if (h.split('.').all { it.toIntOrNull() != null }) return h
        return runCatching {
            InternetDomainName.from(h).topPrivateDomain().toString()
        }.getOrDefault(h)
    }

    /**
     * Nombre base de una marca para agrupar dominios equivalentes con distinto TLD:
     * amazon.com, amazon.com.mx y amazon.co.uk producen "amazon".
     */
    fun marca(entrada: String): String {
        val raiz = raiz(entrada)
        return raiz.substringBefore('.').ifBlank { raiz }
    }

    /** Coincidencia conservadora: mismo dominio raíz o mismo paquete de aplicación. */
    fun coincide(guardado: String, solicitado: String): Boolean {
        if (guardado.isBlank() || solicitado.isBlank()) return false
        val g = guardado.trim().lowercase()
        val s = solicitado.trim().lowercase()
        if (g == s) return true
        val esPaquete = { valor: String -> !valor.contains('/') && valor.count { it == '.' } >= 1 && !valor.contains(' ') }
        if (esPaquete(g) && esPaquete(s) && g == s) return true
        val raizGuardado = raiz(g)
        val raizSolicitado = raiz(s)
        return raizGuardado.isNotEmpty() && raizGuardado == raizSolicitado
    }

    /** Deriva un dominio candidato desde un nombre de paquete (com.ejemplo.app -> ejemplo.com). */
    fun dominioDePaquete(paquete: String): String {
        val partes = paquete.split('.')
        return if (partes.size >= 2) "${partes[1]}.${partes[0]}" else paquete
    }
}
