package com.jlnavas3.bovedalocal.cxf

import java.math.BigInteger
import java.security.AlgorithmParameters
import java.security.KeyFactory
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECParameterSpec
import java.security.spec.ECPrivateKeySpec

/**
 * Asegura que los bytes representen una clave privada en formato PKCS#8.
 * Si los bytes recibidos son de 32 bytes (el escalar `d` de P-256), los envuelve
 * en la estructura estándar PKCS#8 para secp256r1.
 */
fun asegurarPkcs8(bytes: ByteArray): ByteArray {
    if (bytes.size == 32) {
        return try {
            val kf = KeyFactory.getInstance("EC")
            val params = AlgorithmParameters.getInstance("EC").apply {
                init(ECGenParameterSpec("secp256r1"))
            }.getParameterSpec(ECParameterSpec::class.java)
            val spec = ECPrivateKeySpec(BigInteger(1, bytes), params)
            kf.generatePrivate(spec).encoded
        } catch (_: Exception) {
            bytes
        }
    }
    return bytes
}
