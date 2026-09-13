package com.jlnavas3.bovedalocal.crypto

import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode

data class KdfParams(
    val memoryKiB: Int = 65_536,
    val iterations: Int = 3,
    val parallelism: Int = 4,
    val hashLength: Int = 32
) {
    companion object {
        val PREDETERMINADOS = KdfParams()
    }
}

enum class PerfilArgon2(
    val clave: String,
    val titulo: String,
    val memoriaKiB: Int,
    val iteraciones: Int,
    val paralelismo: Int,
    val tiempoEstimadoMs: Int,
    val resumen: String,
    val detalle: String
) {
    ESTANDAR(
        clave = "estandar",
        titulo = "Estándar (64 MiB)",
        memoriaKiB = 65_536,
        iteraciones = 3,
        paralelismo = 4,
        tiempoEstimadoMs = 150,
        resumen = "64 MiB RAM · 3 pasadas (~150 ms)",
        detalle = "Equilibrio óptimo entre apertura instantánea y seguridad de grado militar."
    ),
    REFORZADO(
        clave = "reforzado",
        titulo = "Reforzado (128 MiB)",
        memoriaKiB = 131_072,
        iteraciones = 4,
        paralelismo = 4,
        tiempoEstimadoMs = 300,
        resumen = "128 MiB RAM · 4 pasadas (~300 ms)",
        detalle = "Doble coste de hardware contra granjas de ataque masivo por GPU/ASIC."
    ),
    ULTRASEGURO(
        clave = "ultraseguro",
        titulo = "Ultra-Seguro (256 MiB)",
        memoriaKiB = 262_144,
        iteraciones = 6,
        paralelismo = 4,
        tiempoEstimadoMs = 600,
        resumen = "256 MiB RAM · 6 pasadas (~600 ms)",
        detalle = "Cúspide de resistencia criptográfica para dispositivos potentes con 8–12 GB de RAM."
    );

    fun aKdfParams(): KdfParams = KdfParams(
        memoryKiB = memoriaKiB,
        iterations = iteraciones,
        parallelism = paralelismo,
        hashLength = 32
    )

    companion object {
        fun desde(clave: String?): PerfilArgon2 =
            entries.firstOrNull { it.clave.equals(clave, ignoreCase = true) } ?: ESTANDAR

        fun desdeKdfParams(params: KdfParams): PerfilArgon2? =
            entries.firstOrNull { it.memoriaKiB == params.memoryKiB && it.iteraciones == params.iterations }
    }
}

interface Kdf {
    fun derivar(password: CharArray, salt: ByteArray, params: KdfParams): ByteArray
}

object Argon2Kdf : Kdf {

    private val argon2 by lazy { Argon2Kt() }

    override fun derivar(password: CharArray, salt: ByteArray, params: KdfParams): ByteArray {
        val bytes = Zeroizar.aBytes(password)
        try {
            val resultado = argon2.hash(
                mode = Argon2Mode.ARGON2_ID,
                password = bytes,
                salt = salt,
                tCostInIterations = params.iterations,
                mCostInKibibyte = params.memoryKiB,
                parallelism = params.parallelism,
                hashLengthInBytes = params.hashLength
            )
            return resultado.rawHashAsByteArray()
        } finally {
            Zeroizar.borrar(bytes)
        }
    }
}
