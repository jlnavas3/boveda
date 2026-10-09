package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.util.UUID

/**
 * Microcomponente puro para parsear enlaces de transferencia estructurada
 * offline entre dispositivos Bóveda Local (bovedalocal://importar?payload=...).
 */
object ParserBovedaQr {

    private val jsonSeguro = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun esBovedaTransfer(texto: String): Boolean {
        val t = texto.trim()
        return t.startsWith("bovedalocal://importar", ignoreCase = true) ||
            t.startsWith("boveda://importar", ignoreCase = true)
    }

    fun parsear(texto: String): Entrada? {
        if (!esBovedaTransfer(texto)) return null

        try {
            val queryIdx = texto.indexOf('?')
            if (queryIdx < 0) return null

            val params = texto.substring(queryIdx + 1).split('&')
            val payloadParam = params.firstOrNull { it.startsWith("payload=", ignoreCase = true) } ?: return null
            val payloadEncoded = payloadParam.substringAfter("payload=")
            val json = URLDecoder.decode(payloadEncoded, "UTF-8")

            val deserializada = jsonSeguro.decodeFromString(Entrada.serializer(), json)
            val ahora = System.currentTimeMillis()
            return deserializada.copy(
                id = UUID.randomUUID().toString(),
                creadaEn = ahora,
                modificadaEn = ahora
            )
        } catch (_: Exception) {
            return null
        }
    }
}
