package com.jlnavas3.bovedalocal.util

import android.net.Uri
import android.util.Base64
import com.jlnavas3.bovedalocal.crypto.Base32
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URLDecoder
import java.util.UUID

/**
 * Modelo que representa una cuenta extraída de un paquete de migración de Google Authenticator.
 */
data class CuentaGoogleAuth(
    val id: String = UUID.randomUUID().toString(),
    val emisor: String,
    val cuenta: String,
    val secretoBase32: String,
    val digitos: Int = 6,
    val periodo: Int = 30,
    val algoritmo: String = "HmacSHA1",
    val esTotp: Boolean = true,
    var seleccionada: Boolean = true
) {
    val titulo: String get() = emisor.ifBlank { cuenta.ifBlank { "2FA" } }
}

/**
 * Decodificador de URLs y códigos QR de migración masiva de Google Authenticator
 * (formato `otpauth-migration://offline?data=...`).
 *
 * Implementa la lectura de mensajes Protocol Buffer sin dependencias externas ni permisos de red.
 */
object GoogleAuthMigration {

    /**
     * Determina si el texto ingresado es un enlace de migración de Google Authenticator.
     */
    fun esEnlaceMigracion(texto: String): Boolean {
        val limpio = texto.trim()
        return limpio.startsWith("otpauth-migration://", ignoreCase = true)
    }

    /**
     * Decodifica la URL de migración y devuelve la lista de cuentas de doble factor contenidas.
     */
    fun decodificar(url: String): List<CuentaGoogleAuth> {
        val limpio = url.trim()
        if (!esEnlaceMigracion(limpio)) return emptyList()

        val uri = try {
            Uri.parse(limpio)
        } catch (e: Exception) {
            return emptyList()
        }

        val dataParam = uri.getQueryParameter("data") ?: return emptyList()
        val dataLimpio = URLDecoder.decode(dataParam, "UTF-8")

        val bytesPayload = try {
            Base64.decode(dataLimpio, Base64.DEFAULT)
        } catch (e: Exception) {
            try {
                Base64.decode(dataParam, Base64.DEFAULT)
            } catch (e2: Exception) {
                return emptyList()
            }
        }

        return parsearPayloadProtobuf(bytesPayload)
    }

    private fun parsearPayloadProtobuf(bytes: ByteArray): List<CuentaGoogleAuth> {
        val stream = ByteArrayInputStream(bytes)
        val resultado = mutableListOf<CuentaGoogleAuth>()

        while (stream.available() > 0) {
            val cabecera = leerVarint(stream) ?: break
            val numCampo = (cabecera ushr 3).toInt()
            val tipoTipoWire = (cabecera and 0x07).toInt()

            when (numCampo) {
                1 -> { // otp_parameters (Sub-mensaje Protobuf)
                    if (tipoTipoWire == 2) { // Length-delimited
                        val tamano = leerVarint(stream)?.toInt() ?: break
                        val subBytes = ByteArray(tamano)
                        val leidos = stream.read(subBytes)
                        if (leidos == tamano) {
                            parsearParametroOtp(subBytes)?.let { resultado.add(it) }
                        }
                    } else {
                        saltarCampo(stream, tipoTipoWire)
                    }
                }
                else -> saltarCampo(stream, tipoTipoWire)
            }
        }

        return resultado
    }

    private fun parsearParametroOtp(bytes: ByteArray): CuentaGoogleAuth? {
        val stream = ByteArrayInputStream(bytes)
        var secretoBytes: ByteArray? = null
        var nombre = ""
        var emisor = ""
        var algoritmoNum = 1 // 1 = SHA1
        var digitosNum = 1    // 1 = 6 digitos
        var tipoNum = 2       // 2 = TOTP

        while (stream.available() > 0) {
            val cabecera = leerVarint(stream) ?: break
            val numCampo = (cabecera ushr 3).toInt()
            val tipoWire = (cabecera and 0x07).toInt()

            when (numCampo) {
                1 -> { // secret
                    if (tipoWire == 2) {
                        val tam = leerVarint(stream)?.toInt() ?: break
                        secretoBytes = ByteArray(tam)
                        stream.read(secretoBytes)
                    } else saltarCampo(stream, tipoWire)
                }
                2 -> { // name
                    if (tipoWire == 2) {
                        val tam = leerVarint(stream)?.toInt() ?: break
                        val b = ByteArray(tam)
                        stream.read(b)
                        nombre = String(b, Charsets.UTF_8)
                    } else saltarCampo(stream, tipoWire)
                }
                3 -> { // issuer
                    if (tipoWire == 2) {
                        val tam = leerVarint(stream)?.toInt() ?: break
                        val b = ByteArray(tam)
                        stream.read(b)
                        emisor = String(b, Charsets.UTF_8)
                    } else saltarCampo(stream, tipoWire)
                }
                4 -> { // algorithm
                    if (tipoWire == 0) {
                        algoritmoNum = leerVarint(stream)?.toInt() ?: 1
                    } else saltarCampo(stream, tipoWire)
                }
                5 -> { // digits
                    if (tipoWire == 0) {
                        digitosNum = leerVarint(stream)?.toInt() ?: 1
                    } else saltarCampo(stream, tipoWire)
                }
                6 -> { // type (1 = HOTP, 2 = TOTP)
                    if (tipoWire == 0) {
                        tipoNum = leerVarint(stream)?.toInt() ?: 2
                    } else saltarCampo(stream, tipoWire)
                }
                else -> saltarCampo(stream, tipoWire)
            }
        }

        if (secretoBytes == null || secretoBytes.isEmpty()) return null
        val secretoBase32 = Base32.codificar(secretoBytes)

        val emisorEtiqueta = if (nombre.contains(':')) nombre.substringBefore(':').trim() else ""
        val cuentaNombre = if (nombre.contains(':')) nombre.substringAfter(':').trim() else nombre.trim()
        val emisorFinal = emisor.ifBlank { emisorEtiqueta }

        val algoritmoStr = when (algoritmoNum) {
            2 -> "HmacSHA256"
            3 -> "HmacSHA512"
            else -> "HmacSHA1"
        }

        val digitosFinal = when (digitosNum) {
            2 -> 8
            else -> 6
        }

        return CuentaGoogleAuth(
            emisor = emisorFinal,
            cuenta = cuentaNombre,
            secretoBase32 = secretoBase32,
            digitos = digitosFinal,
            periodo = 30,
            algoritmo = algoritmoStr,
            esTotp = (tipoNum != 1)
        )
    }

    private fun leerVarint(stream: InputStream): Long? {
        var valor = 0L
        var desplazamiento = 0
        while (desplazamiento < 64) {
            val b = stream.read()
            if (b == -1) return if (desplazamiento == 0) null else valor
            valor = valor or ((b and 0x7F).toLong() shl desplazamiento)
            if ((b and 0x80) == 0) return valor
            desplazamiento += 7
        }
        return valor
    }

    private fun saltarCampo(stream: InputStream, tipoWire: Int) {
        when (tipoWire) {
            0 -> leerVarint(stream) // Varint
            1 -> stream.skip(8)     // 64-bit
            2 -> {                  // Length-delimited
                val tam = leerVarint(stream)?.toInt() ?: return
                stream.skip(tam.toLong())
            }
            5 -> stream.skip(4)     // 32-bit
        }
    }
}
