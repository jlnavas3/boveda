package com.jlnavas3.bovedalocal.util

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

    /**
     * Identificador de agrupación que preserva subdominios significativos pero normaliza TLDs y prefijo www:
     * - account.xiaomi.com -> "account.xiaomi"
     * - xiaomi.com -> "xiaomi"
     * - www.xiaomi.com -> "xiaomi"
     * - aws.amazon.com -> "aws.amazon"
     * - amazon.co.uk -> "amazon"
     */
    fun sitioAgrupacion(entrada: String): String {
        val h = host(entrada)
        if (h.isEmpty() || !h.contains('.')) return h.ifBlank { entrada.trim().lowercase() }
        if (h.split('.').all { it.toIntOrNull() != null }) return h
        return runCatching {
            val idn = InternetDomainName.from(h)
            val suffix = idn.publicSuffix()?.toString()
            if (!suffix.isNullOrEmpty() && h.endsWith(".$suffix")) {
                h.removeSuffix(".$suffix")
            } else {
                h
            }
        }.getOrDefault(h)
    }

    /** Coincidencia inteligente: mismo dominio raíz, mismo paquete de aplicación o equivalencia de catálogo/marca. */
    fun coincide(guardado: String, solicitado: String, mapeoPersonalizado: Map<String, String> = emptyMap()): Boolean =
        ComparadorPaqueteDominio.coincide(guardado, solicitado, mapeoPersonalizado)

    /** Deriva un dominio candidato desde un nombre de paquete (com.ejemplo.app -> ejemplo.com). */
    fun dominioDePaquete(paquete: String): String {
        val partes = paquete.split('.')
        return if (partes.size >= 2) "${partes[1]}.${partes[0]}" else paquete
    }
}
