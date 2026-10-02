package com.jlnavas3.bovedalocal.cxf

import java.net.URI

/**
 * Normaliza cadenas de direcciones URL o dominios para asegurar que contengan un esquema
 * válido (http://, https:// o android://) requerido por la especificación FIDO CXF y los
 * analizadores de importación de Google y otros gestores.
 */
object CxfNormalizadorUrl {

    private val REGEX_IP = Regex("""^(\d{1,3}\.){3}\d{1,3}(:\d+)?(/.*)?$""")

    fun normalizar(urlBruta: String): String {
        val limpia = urlBruta.trim()
        if (limpia.isBlank()) return ""

        val conEsquema = when {
            limpia.startsWith("http://", ignoreCase = true) ||
            limpia.startsWith("https://", ignoreCase = true) ||
            limpia.startsWith("android://", ignoreCase = true) ||
            limpia.startsWith("androidapp://", ignoreCase = true) -> limpia

            REGEX_IP.matches(limpia) -> "http://$limpia"
            else -> "https://$limpia"
        }

        return try {
            val uri = URI(conEsquema)
            if (uri.host.isNullOrBlank() && !conEsquema.startsWith("android://")) {
                "https://$limpia"
            } else {
                conEsquema
            }
        } catch (_: Exception) {
            "https://${limpia.replace(" ", "")}"
        }
    }
}
