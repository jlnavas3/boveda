package com.jlnavas3.bovedalocal.util

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.jlnavas3.bovedalocal.data.Entrada

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
     * Formatea los datos de la entrada en un formato claro para compartir pantalla a pantalla.
     */
    fun textoCredencialCompleta(entrada: Entrada): String = buildString {
        appendLine("--- CREDENCIAL BÓVEDA LOCAL ---")
        if (entrada.titulo.isNotBlank()) appendLine("Título: ${entrada.titulo}")
        if (entrada.usuario.isNotBlank()) appendLine("Usuario: ${entrada.usuario}")
        if (entrada.contrasena.isNotBlank()) appendLine("Contraseña: ${entrada.contrasena}")
        if (entrada.urls.isNotEmpty()) appendLine("URL: ${entrada.urls.first()}")
    }.trim()
}
