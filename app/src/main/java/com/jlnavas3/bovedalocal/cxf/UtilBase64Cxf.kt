package com.jlnavas3.bovedalocal.cxf

import java.util.Base64

/**
 * Utilidades para decodificación y codificación Base64 / Base64URL sin dependencia
 * de android.util.Base64 para compatibilidad total en tests JVM y Android (API 26+).
 */
object UtilBase64Cxf {

    /**
     * Decodifica una cadena en Base64 o Base64URL, con o sin relleno '='.
     */
    fun decodificar(entrada: String): ByteArray {
        val limpia = entrada.trim()
            .replace("\n", "")
            .replace("\r", "")
            .replace(" ", "")
            .replace('-', '+')
            .replace('_', '/')

        val conRelleno = when (limpia.length % 4) {
            2 -> "$limpia=="
            3 -> "$limpia="
            else -> limpia
        }

        return Base64.getDecoder().decode(conRelleno)
    }

    /**
     * Codifica bytes a Base64URL sin relleno '=' (estándar FIDO/WebAuthn).
     */
    fun aBase64Url(bytes: ByteArray): String {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    /**
     * Codifica bytes a Base64 estándar.
     */
    fun aBase64(bytes: ByteArray): String {
        return Base64.getEncoder().encodeToString(bytes)
    }
}
