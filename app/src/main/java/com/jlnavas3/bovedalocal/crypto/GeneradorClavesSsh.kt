/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.crypto

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPublicKey

/**
 * Algoritmos criptográficos soportados para la generación de pares de claves SSH.
 */
enum class AlgoritmoSsh(val etiqueta: String, val descripcion: String) {
    ED25519("Ed25519", "Moderno, compacto y recomendado (256 bits)"),
    RSA_4096("RSA 4096", "Máxima seguridad y compatibilidad con servidores clásicos"),
    RSA_2048("RSA 2048", "Compatible y rápido"),
    ECDSA_P256("ECDSA P-256", "Curvas elípticas estándar NIST")
}

/**
 * Modelo inmutable que representa un par de claves SSH generado.
 */
data class ParClavesSsh(
    val algoritmo: AlgoritmoSsh,
    val clavePublicaSsh: String,
    val clavePrivadaPem: String,
    val huellaSha256: String
)

/**
 * Generador offline de pares de claves SSH compatibles con OpenSSH (Ed25519, RSA, ECDSA)
 * y codificación estándar PKCS#8 PEM para clientes SSH, Git y servidores.
 */
object GeneradorClavesSsh {

    fun generar(
        algoritmo: AlgoritmoSsh = AlgoritmoSsh.ED25519,
        comentario: String = "boveda@local"
    ): ParClavesSsh {
        val (keyPair, algoReal) = try {
            when (algoritmo) {
                AlgoritmoSsh.ED25519 -> {
                    val kpg = KeyPairGenerator.getInstance("Ed25519")
                    kpg.generateKeyPair() to AlgoritmoSsh.ED25519
                }
                AlgoritmoSsh.RSA_4096 -> {
                    val kpg = KeyPairGenerator.getInstance("RSA")
                    kpg.initialize(4096)
                    kpg.generateKeyPair() to AlgoritmoSsh.RSA_4096
                }
                AlgoritmoSsh.RSA_2048 -> {
                    val kpg = KeyPairGenerator.getInstance("RSA")
                    kpg.initialize(2048)
                    kpg.generateKeyPair() to AlgoritmoSsh.RSA_2048
                }
                AlgoritmoSsh.ECDSA_P256 -> {
                    val kpg = KeyPairGenerator.getInstance("EC")
                    kpg.initialize(256)
                    kpg.generateKeyPair() to AlgoritmoSsh.ECDSA_P256
                }
            }
        } catch (_: Exception) {
            // Fallback para dispositivos que no soportan Ed25519 de forma nativa en JCA
            val kpg = KeyPairGenerator.getInstance("RSA")
            kpg.initialize(2048)
            kpg.generateKeyPair() to AlgoritmoSsh.RSA_2048
        }

        val wireBytes = codificarClavePublicaWire(keyPair, algoReal)
        val base64Pub = java.util.Base64.getEncoder().encodeToString(wireBytes)
        val nombreTipoSsh = when (algoReal) {
            AlgoritmoSsh.ED25519 -> "ssh-ed25519"
            AlgoritmoSsh.RSA_4096, AlgoritmoSsh.RSA_2048 -> "ssh-rsa"
            AlgoritmoSsh.ECDSA_P256 -> "ecdsa-sha2-nistp256"
        }
        val comentarioLimpio = comentario.trim().ifBlank { "boveda@local" }
        val clavePublicaSsh = "$nombreTipoSsh $base64Pub $comentarioLimpio"

        val privBase64 = java.util.Base64.getEncoder().encodeToString(keyPair.private.encoded)
        val privLineas = privBase64.chunked(64).joinToString("\n")
        val clavePrivadaPem = "-----BEGIN PRIVATE KEY-----\n$privLineas\n-----END PRIVATE KEY-----"

        val digest = MessageDigest.getInstance("SHA-256").digest(wireBytes)
        val huellaBase64 = java.util.Base64.getEncoder().encodeToString(digest).trimEnd('=')
        val huellaSha256 = "SHA256:$huellaBase64"

        return ParClavesSsh(
            algoritmo = algoReal,
            clavePublicaSsh = clavePublicaSsh,
            clavePrivadaPem = clavePrivadaPem,
            huellaSha256 = huellaSha256
        )
    }

    private fun codificarClavePublicaWire(keyPair: KeyPair, algo: AlgoritmoSsh): ByteArray {
        val out = ByteArrayOutputStream()
        val dos = DataOutputStream(out)
        when (algo) {
            AlgoritmoSsh.ED25519 -> {
                escribirCadenaSsh(dos, "ssh-ed25519")
                val encoded = keyPair.public.encoded
                // Los últimos 32 bytes de la SubjectPublicKeyInfo ASN.1 de Ed25519 contienen la clave pública pura
                val rawPublic = if (encoded.size >= 32) encoded.takeLast(32).toByteArray() else encoded
                escribirBytesSsh(dos, rawPublic)
            }
            AlgoritmoSsh.RSA_4096, AlgoritmoSsh.RSA_2048 -> {
                val rsaPub = keyPair.public as RSAPublicKey
                escribirCadenaSsh(dos, "ssh-rsa")
                escribirMpintSsh(dos, rsaPub.publicExponent)
                escribirMpintSsh(dos, rsaPub.modulus)
            }
            AlgoritmoSsh.ECDSA_P256 -> {
                val ecPub = keyPair.public as ECPublicKey
                escribirCadenaSsh(dos, "ecdsa-sha2-nistp256")
                escribirCadenaSsh(dos, "nistp256")
                val puntoW = ecPub.w
                val x = puntoW.affineX.toByteArray().normalizar32Bytes()
                val y = puntoW.affineY.toByteArray().normalizar32Bytes()
                val puntoUncompressed = byteArrayOf(0x04) + x + y
                escribirBytesSsh(dos, puntoUncompressed)
            }
        }
        dos.flush()
        return out.toByteArray()
    }

    private fun escribirCadenaSsh(dos: DataOutputStream, texto: String) {
        val bytes = texto.toByteArray(Charsets.US_ASCII)
        escribirBytesSsh(dos, bytes)
    }

    private fun escribirBytesSsh(dos: DataOutputStream, bytes: ByteArray) {
        dos.writeInt(bytes.size)
        dos.write(bytes)
    }

    private fun escribirMpintSsh(dos: DataOutputStream, valor: BigInteger) {
        val bytes = valor.toByteArray()
        escribirBytesSsh(dos, bytes)
    }

    private fun ByteArray.normalizar32Bytes(): ByteArray {
        return when {
            size == 32 -> this
            size > 32 -> takeLast(32).toByteArray()
            else -> ByteArray(32 - size) { 0 } + this
        }
    }
}
