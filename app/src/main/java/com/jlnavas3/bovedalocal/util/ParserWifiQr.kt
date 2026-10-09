package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.data.TipoEntrada
import java.util.UUID

/**
 * Microcomponente puro para parsear códigos QR de configuración Wi-Fi
 * estándar (WIFI:T:WPA;S:Red;P:Clave;;) a una [Entrada] de tipo [TipoEntrada.WIFI].
 */
object ParserWifiQr {

    fun esWifi(texto: String): Boolean {
        return texto.trim().startsWith("WIFI:", ignoreCase = true)
    }

    fun parsear(texto: String): Entrada? {
        if (!esWifi(texto)) return null

        val contenido = texto.trim().substringAfter("WIFI:", "")
        var ssid = ""
        var pass = ""
        var tipo = "WPA"
        var oculta = false

        // Extraer claves separadas por ';' respetando escapes '\;'
        val partes = mutableListOf<String>()
        val sb = StringBuilder()
        var escapado = false

        for (c in contenido) {
            when {
                escapado -> {
                    sb.append(c)
                    escapado = false
                }
                c == '\\' -> {
                    escapado = true
                }
                c == ';' -> {
                    if (sb.isNotEmpty()) {
                        partes.add(sb.toString())
                        sb.clear()
                    }
                }
                else -> sb.append(c)
            }
        }
        if (sb.isNotEmpty()) partes.add(sb.toString())

        partes.forEach { parte ->
            val dosPuntos = parte.indexOf(':')
            if (dosPuntos > 0) {
                val clave = parte.substring(0, dosPuntos).uppercase()
                val valor = parte.substring(dosPuntos + 1)
                when (clave) {
                    "S" -> ssid = valor
                    "P" -> pass = valor
                    "T" -> tipo = valor
                    "H" -> oculta = valor.equals("true", ignoreCase = true)
                }
            }
        }

        if (ssid.isBlank() && pass.isBlank()) return null

        val campos = mutableListOf<CampoPersonalizado>()
        campos.add(
            CampoPersonalizado(
                id = UUID.randomUUID().toString(),
                etiqueta = "Nombre de red (SSID)",
                valor = ssid,
                tipo = TipoCampo.TEXTO
            )
        )
        if (pass.isNotBlank()) {
            campos.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = "Contraseña Wi-Fi",
                    valor = pass,
                    tipo = TipoCampo.TEXTO,
                    esSensible = true
                )
            )
        }
        campos.add(
            CampoPersonalizado(
                id = UUID.randomUUID().toString(),
                etiqueta = "Tipo de seguridad",
                valor = tipo,
                tipo = TipoCampo.TEXTO
            )
        )
        if (oculta) {
            campos.add(
                CampoPersonalizado(
                    id = UUID.randomUUID().toString(),
                    etiqueta = "Red oculta",
                    valor = "true",
                    tipo = TipoCampo.TEXTO
                )
            )
        }

        val ahora = System.currentTimeMillis()
        return Entrada(
            id = UUID.randomUUID().toString(),
            tipo = TipoEntrada.WIFI,
            titulo = ssid.ifBlank { "Red Wi-Fi" },
            contrasena = pass,
            camposPersonalizados = campos,
            creadaEn = ahora,
            modificadaEn = ahora
        )
    }
}
