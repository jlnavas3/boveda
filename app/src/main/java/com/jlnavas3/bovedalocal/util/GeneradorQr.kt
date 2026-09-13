package com.jlnavas3.bovedalocal.util

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.GestorCamposBase

object GeneradorQr {

    private val HINTS: Map<EncodeHintType, Any> = mapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8",
        EncodeHintType.MARGIN to 1,
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M
    )

    /**
     * Genera una matriz booleana para representar el código QR.
     * Pura e independiente del framework de Android para poder probarse en la JVM.
     */
    fun generarMatriz(texto: String, tamano: Int = 512): BitMatrix {
        require(texto.isNotBlank()) { "El contenido del código QR no puede estar vacío" }
        val writer = QRCodeWriter()
        return writer.encode(texto, BarcodeFormat.QR_CODE, tamano, tamano, HINTS)
    }

    /**
     * Genera un [Bitmap] de Android a partir del texto y tamaño especificados.
     */
    fun generarBitmap(
        texto: String,
        tamano: Int = 512,
        colorPunto: Int = 0xFF000000.toInt(),
        colorFondo: Int = 0xFFFFFFFF.toInt()
    ): Bitmap {
        val matrix = generarMatriz(texto, tamano)
        val ancho = matrix.width
        val alto = matrix.height
        val pixeles = IntArray(ancho * alto)

        for (y in 0 until alto) {
            val filaOffset = y * ancho
            for (x in 0 until ancho) {
                pixeles[filaOffset + x] = if (matrix.get(x, y)) colorPunto else colorFondo
            }
        }

        val bitmap = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixeles, 0, ancho, 0, 0, ancho, alto)
        return bitmap
    }

    /**
     * Genera la URI estándar otpauth:// para compartir el doble factor con cualquier app de autenticación.
     */
    fun uriTotp(entrada: Entrada): String? {
        val secreto = entrada.secretoTotp?.takeIf { it.isNotBlank() } ?: return null
        val emisor = entrada.totpEmisor.ifBlank { entrada.titulo }.trim()
        val cuenta = entrada.usuario.ifBlank { entrada.titulo }.trim()
        val emisorCodificado = java.net.URLEncoder.encode(emisor, "UTF-8").replace("+", "%20")
        val cuentaCodificada = java.net.URLEncoder.encode(cuenta, "UTF-8").replace("+", "%20")
        val label = if (emisorCodificado.isNotBlank()) "$emisorCodificado:$cuentaCodificada" else cuentaCodificada

        return buildString {
            append("otpauth://totp/$label")
            append("?secret=$secreto")
            if (emisorCodificado.isNotBlank()) append("&issuer=$emisorCodificado")
            append("&digits=${entrada.totpDigitos}")
            append("&period=${entrada.totpPeriodo}")
            if (entrada.totpAlgoritmo.isNotBlank() && entrada.totpAlgoritmo != "HmacSHA1") {
                val alg = entrada.totpAlgoritmo.removePrefix("Hmac")
                append("&algorithm=$alg")
            }
        }
    }

    /**
     * Escapa caracteres reservados en el formato Wi-Fi de ZXing:
     * '\', ';', ',', ':', '"' se prefijan con una barra invertida '\'.
     */
    fun escaparWifi(valor: String): String = buildString {
        for (c in valor) {
            if (c == '\\' || c == ';' || c == ',' || c == ':' || c == '"') {
                append('\\')
            }
            append(c)
        }
    }

    /**
     * Normaliza el tipo de seguridad Wi-Fi según el estándar:
     * "WPA" (compatible con WPA, WPA2, WPA3, WPA-PSK, etc. en Android e iOS), "WEP", o "nopass" para redes abiertas.
     */
    fun normalizarSeguridadWifi(tipoSeguridad: String, tienePassword: Boolean): String {
        val limpia = tipoSeguridad.trim().uppercase()
        return when {
            !tienePassword || limpia.contains("NOPASS") || limpia.contains("ABIERTA") || limpia.contains("OPEN") || limpia.contains("NINGUNA") || limpia == "SIN SEGURIDAD" -> "nopass"
            limpia.contains("WEP") -> "WEP"
            limpia.contains("WPA") || limpia.contains("SAE") || limpia.contains("PSK") || limpia.contains("AES") -> "WPA"
            else -> if (tienePassword) "WPA" else "nopass"
        }
    }

    /**
     * Genera la cadena de configuración Wi-Fi estándar según la especificación ZXing / Android / iOS:
     * WIFI:T:WPA;S:SPIDER2;P:La contraseña;;
     * o para redes abiertas:
     * WIFI:T:nopass;S:SPIDER2;;
     */
    fun textoConfiguracionWifi(
        ssid: String,
        clave: String = "",
        tipoSeguridad: String = "WPA",
        oculta: Boolean = false
    ): String {
        val ssidEscapado = escaparWifi(ssid.trim())
        val segNormalizada = normalizarSeguridadWifi(tipoSeguridad, tienePassword = clave.isNotBlank())
        return buildString {
            append("WIFI:")
            append("T:").append(segNormalizada).append(";")
            append("S:").append(ssidEscapado).append(";")
            if (segNormalizada != "nopass" && clave.isNotBlank()) {
                append("P:").append(escaparWifi(clave)).append(";")
            }
            if (oculta) {
                append("H:true;")
            }
            append(";")
        }
    }

    /**
     * Extrae de forma inteligente el SSID, contraseña y tipo de seguridad de una [Entrada]
     * y genera el código QR estándar que los teléfonos reconocen de forma nativa para conectarse.
     */
    fun textoWifiDesdeEntrada(entrada: Entrada): String {
        val ssid = GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Nombre de red (SSID)")
            .ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "SSID") }
            .ifBlank { entrada.titulo }
            .trim()

        val clave = GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Contraseña Wi-Fi")
            .ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Contraseña") }
            .ifBlank { entrada.contrasena }

        val seguridad = GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Tipo de seguridad")
            .ifBlank { GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Seguridad") }
            .ifBlank { "WPA" }

        val esOculta = GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Red oculta")
            .equals("true", ignoreCase = true) ||
            GestorCamposBase.valorDeCampo(entrada.camposPersonalizados, "Oculta")
                .equals("true", ignoreCase = true)

        return textoConfiguracionWifi(ssid = ssid, clave = clave, tipoSeguridad = seguridad, oculta = esOculta)
    }

    /**
     * Formatea los datos de la entrada en un formato claro para compartir pantalla a pantalla.
     */
    fun textoCredencialCompleta(entrada: Entrada): String = buildString {
        appendLine("--- CREDENCIAL BÓVEDA LOCAL ---")
        if (entrada.titulo.isNotBlank()) appendLine("Título: ${entrada.titulo}")
        if (entrada.tipo != TipoEntrada.LOGIN) appendLine("Tipo: ${entrada.tipo.etiqueta}")
        if (entrada.usuario.isNotBlank()) appendLine("Usuario: ${entrada.usuario}")
        if (entrada.contrasena.isNotBlank()) appendLine("Contraseña: ${entrada.contrasena}")
        if (entrada.urls.isNotEmpty()) appendLine("URL: ${entrada.urls.first()}")
        entrada.camposPersonalizados.forEach { campo ->
            if (campo.valor.isNotBlank()) {
                appendLine("${campo.etiqueta}: ${campo.valor}")
            }
        }
        if (entrada.notas.isNotBlank()) appendLine("Notas: ${entrada.notas}")
    }.trim()
}
