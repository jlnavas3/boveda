/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Locale
import java.util.zip.CRC32
import java.util.zip.Deflater
import java.util.zip.Inflater
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Paquete empaquetado para transmisión óptica mediante ráfaga o QR estático.
 */
data class PaqueteOpticoGenerado(
    val idSesionHex: String,
    val pinEfimero: String,
    val totalTramas: Int,
    val tramas: List<TramaOpticaQr>,
    val bytesCifradosTotales: Int,
    val esDelta: Boolean
)

/**
 * Códec criptográfico y de compresión para el canal de transporte óptico (`bvda-opt://`).
 *
 * Características:
 * - Selección de Delta reciente (últimas 24h) para transmisión en 1 solo QR estático (<400 bytes).
 * - Compresión con Zlib estándar (RFC 1950) con nivel máximo de compresión.
 * - Cifrado autenticado ChaCha20-Poly1305 protegido por PIN efímero de 6 dígitos.
 * - Validación de integridad CRC32 del paquete completo.
 * - Fragmentación segura en tramas compactas Base64Url sin padding.
 */
object CodecDeltaOptico {

    const val MAX_BYTES_POR_CHUNK_DEFECTO = 300
    private const val LIMITE_DELTA_HORAS_DEFECTO = 6L
    private const val MAX_ENTRADAS_DELTA_DEFECTO = 5

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    /**
     * Genera un paquete óptico completo a partir del estado de la bóveda.
     *
     * @param boveda Bóveda local a exportar ópticamente.
     * @param soloDelta Si es true, filtra únicamente las entradas modificadas/creadas recientemente.
     * @param maxBytesPorChunk Tamaño máximo de carga útil binaria por código QR (por defecto 520B).
     */
    fun empaquetar(
        boveda: ContenidoBoveda,
        soloDelta: Boolean = true,
        maxBytesPorChunk: Int = MAX_BYTES_POR_CHUNK_DEFECTO
    ): PaqueteOpticoGenerado {
        val bovedaBase = if (soloDelta) {
            extraerDeltaReciente(boveda)
        } else {
            boveda
        }
        val bovedaAEnviar = bovedaBase.copy(aliasDispositivo = IdentidadDispositivoLocal.obtenerNombre())

        // 1. Serializar JSON compacto y comprimir con Zlib
        val jsonStr = json.encodeToString(ContenidoBoveda.serializer(), bovedaAEnviar)
        val jsonBytes = jsonStr.toByteArray(Charsets.UTF_8)
        val bytesComprimidos = comprimirZlib(jsonBytes)

        // 2. Generar sesión y PIN efímero de 6 dígitos
        val random = SecureRandom()
        val idSesionBytes = ByteArray(4)
        random.nextBytes(idSesionBytes)
        val idSesionHex = idSesionBytes.joinToString("") { "%02X".format(it) }

        val pinNum = 100_000 + random.nextInt(900_000)
        val pinEfimero = pinNum.toString()

        // 3. Cifrar con ChaCha20-Poly1305
        val bloqueCifrado = cifrarOptico(bytesComprimidos, idSesionHex, pinEfimero)
        val crc32Hex = calcularCrc32Hex(bloqueCifrado)

        // 4. Fragmentar en chunks
        val chunks = mutableListOf<ByteArray>()
        var offset = 0
        while (offset < bloqueCifrado.size) {
            val end = minOf(offset + maxBytesPorChunk, bloqueCifrado.size)
            chunks.add(bloqueCifrado.copyOfRange(offset, end))
            offset = end
        }

        val total = chunks.size
        val aliasLocal = IdentidadDispositivoLocal.obtenerNombre()
        val tramas = chunks.mapIndexed { idx, chunk ->
            TramaOpticaQr(
                version = 1,
                indice = idx + 1,
                total = total,
                idSesionHex = idSesionHex,
                crc32TotalHex = crc32Hex,
                datosChunk = chunk,
                aliasEmisor = aliasLocal
            )
        }

        return PaqueteOpticoGenerado(
            idSesionHex = idSesionHex,
            pinEfimero = pinEfimero,
            totalTramas = total,
            tramas = tramas,
            bytesCifradosTotales = bloqueCifrado.size,
            esDelta = soloDelta
        )
    }

    /**
     * Reensambla, valida CRC32, descifra con el PIN de 6 dígitos y descomprime la bóveda.
     */
    fun desempaquetar(
        bloqueCifradoCompleto: ByteArray,
        idSesionHex: String,
        pin6Digitos: String,
        crc32EsperadoHex: String
    ): ContenidoBoveda {
        // 1. Verificar CRC32 del bloque total
        val crcCalculado = calcularCrc32Hex(bloqueCifradoCompleto)
        require(crcCalculado.equals(crc32EsperadoHex, ignoreCase = true)) {
            "Fallo de integridad CRC32: esperado $crc32EsperadoHex, calculado $crcCalculado"
        }

        // 2. Descifrar con ChaCha20-Poly1305
        val bytesComprimidos = try {
            descifrarOptico(bloqueCifradoCompleto, idSesionHex, pin6Digitos.trim())
        } catch (e: Exception) {
            throw IllegalArgumentException("PIN óptico de 6 dígitos incorrecto o código QR alterado.", e)
        }

        // 3. Descomprimir Zlib
        val jsonBytes = descomprimirZlib(bytesComprimidos)
        val jsonStr = String(jsonBytes, Charsets.UTF_8)

        // 4. Deserializar ContenidoBoveda
        return json.decodeFromString(ContenidoBoveda.serializer(), jsonStr)
    }

    /**
     * Determina si una entrada tiene cambios pendientes de sincronizar:
     * - Si su lastSyncHash es null (entrada nueva nunca sincronizada).
     * - O si su contentHash actual difiere de su lastSyncHash registrado.
     */
    fun tieneCambiosPendientes(entrada: Entrada): Boolean {
        val lastSync = entrada.lastSyncHash ?: return true
        val hashActual = CalculadorHashEntrada.bytesAHex(CalculadorHashEntrada.calcularContentHash(entrada))
        return !hashActual.equals(lastSync, ignoreCase = true)
    }

    /**
     * Filtra las entradas con cambios reales pendientes para formar un Delta mínimo.
     * Si no hay cambios pendientes (todas sincronizadas), selecciona únicamente 1 entrada testigo
     * para garantizar que el modo «Cambios recientes» genere SIEMPRE 1 solo código QR estático (1/1).
     *
     * IMPORTANTE: No arrastra toda la papelera, categorías ni perfiles; solo incluye lo estrictamente
     * referenciado por las entradas del delta.
     */
    fun extraerDeltaReciente(
        boveda: ContenidoBoveda,
        maxEntradas: Int = MAX_ENTRADAS_DELTA_DEFECTO
    ): ContenidoBoveda {
        // 1. Filtrar entradas activas y de papelera con cambios pendientes reales
        val activasPendientes = boveda.entradas.filter { tieneCambiosPendientes(it) }
        val papeleraPendientes = boveda.papelera.filter { tieneCambiosPendientes(it) }

        val todasPendientes = (activasPendientes + papeleraPendientes)
            .sortedByDescending { maxOf(it.modificadaEn, it.creadaEn, it.eliminadaEn) }
            .take(maxEntradas)

        val entradasFinales: List<Entrada>
        val papeleraFinal: List<Entrada>

        if (todasPendientes.isNotEmpty()) {
            val idsPendientes = todasPendientes.map { it.id }.toSet()
            entradasFinales = activasPendientes.filter { it.id in idsPendientes }
            papeleraFinal = papeleraPendientes.filter { it.id in idsPendientes }
        } else {
            // Si todas las entradas están al día, tomamos únicamente la última entrada modificada
            // (1 sola entrada) como testigo para que el QR sea 1/1 estático.
            val testigo = boveda.entradas.maxByOrNull { maxOf(it.modificadaEn, it.creadaEn, it.eliminadaEn) }
                ?: boveda.papelera.maxByOrNull { maxOf(it.modificadaEn, it.creadaEn, it.eliminadaEn) }

            if (testigo != null) {
                if (testigo in boveda.entradas) {
                    entradasFinales = listOf(testigo)
                    papeleraFinal = emptyList()
                } else {
                    entradasFinales = emptyList()
                    papeleraFinal = listOf(testigo)
                }
            } else {
                entradasFinales = emptyList()
                papeleraFinal = emptyList()
            }
        }

        // 2. Extraer ÚNICAMENTE las categorías y perfiles referenciados por las entradas del delta
        val totalDeltaEntradas = entradasFinales + papeleraFinal
        val catIds = totalDeltaEntradas.flatMap { it.categorias }.toSet()
        val categoriasFinales = if (catIds.isEmpty()) {
            emptyList()
        } else {
            boveda.categorias.filter { it.id in catIds || it.nombre in catIds }
        }

        val perfilIds = totalDeltaEntradas.mapNotNull { it.perfilId }.toSet()
        val perfilesFinales = if (perfilIds.isEmpty()) {
            emptyList()
        } else {
            boveda.perfiles.filter { it.id in perfilIds }
        }

        return ContenidoBoveda(
            version = boveda.version,
            entradas = entradasFinales,
            papelera = papeleraFinal,
            categorias = categoriasFinales,
            perfiles = perfilesFinales,
            configuracion = null
        )
    }

    private fun derivarClaveOptica(idSesionHex: String, pin6Digitos: String): ByteArray {
        val msg = "BVDA-OPT-V1:${idSesionHex.uppercase(Locale.US)}:${pin6Digitos.trim()}"
        return MessageDigest.getInstance("SHA-256").digest(msg.toByteArray(Charsets.UTF_8))
    }

    private fun cifrarOptico(datosComprimidos: ByteArray, idSesionHex: String, pin6Digitos: String): ByteArray {
        val clave = derivarClaveOptica(idSesionHex, pin6Digitos)
        val cipher = Cipher.getInstance("ChaCha20-Poly1305")
        val keySpec = SecretKeySpec(clave, "ChaCha20")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(ByteArray(12)))
        cipher.updateAAD("BVDA-OPT-V1:${idSesionHex.uppercase(Locale.US)}".toByteArray(Charsets.UTF_8))
        return cipher.doFinal(datosComprimidos)
    }

    private fun descifrarOptico(bloqueCifrado: ByteArray, idSesionHex: String, pin6Digitos: String): ByteArray {
        val clave = derivarClaveOptica(idSesionHex, pin6Digitos)
        val cipher = Cipher.getInstance("ChaCha20-Poly1305")
        val keySpec = SecretKeySpec(clave, "ChaCha20")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(ByteArray(12)))
        cipher.updateAAD("BVDA-OPT-V1:${idSesionHex.uppercase(Locale.US)}".toByteArray(Charsets.UTF_8))
        return cipher.doFinal(bloqueCifrado)
    }

    private fun comprimirZlib(datos: ByteArray): ByteArray {
        val deflater = Deflater(Deflater.BEST_COMPRESSION)
        deflater.setInput(datos)
        deflater.finish()
        val bos = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        while (!deflater.finished()) {
            val count = deflater.deflate(buf)
            bos.write(buf, 0, count)
        }
        deflater.end()
        return bos.toByteArray()
    }

    private fun descomprimirZlib(datos: ByteArray): ByteArray {
        val inflater = Inflater()
        inflater.setInput(datos)
        val bos = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        while (!inflater.finished()) {
            val count = inflater.inflate(buf)
            if (count == 0 && inflater.needsInput()) break
            bos.write(buf, 0, count)
        }
        inflater.end()
        return bos.toByteArray()
    }

    fun calcularCrc32Hex(datos: ByteArray): String {
        val crc = CRC32()
        crc.update(datos)
        return String.format(Locale.US, "%08X", crc.value)
    }
}
