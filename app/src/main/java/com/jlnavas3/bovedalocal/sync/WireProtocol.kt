/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import kotlinx.serialization.json.Json
import java.io.EOFException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Protocolo de tramas de red (Framing) y serialización binaria canónica para sincronización P2P.
 * Espejo canónico de `src/sync/p2p/wire_protocol.rs` en Rust.
 *
 * Contrato:
 * - Prefijo de longitud u32 little-endian: `[len_le (4B)] || [payload (len bytes)]`.
 * - Límite de seguridad DoS/OOM: 64 MB.
 * - Formato de mensaje en claro: `[tipo (1B)] || [cuerpo]`.
 */
object WireProtocol {

    const val MAX_TRAMA_TAMANO_DEFECTO: Int = 64 * 1024 * 1024

    const val TIPO_MSG_SYNC_PAYLOAD: Byte = 0x01
    const val TIPO_MSG_COMMIT_ACK: Byte = 0x02

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    /**
     * Lee una trama con prefijo de longitud `u32` little-endian desde un [InputStream].
     */
    fun leerTrama(reader: InputStream, maxBytes: Int = MAX_TRAMA_TAMANO_DEFECTO): ByteArray {
        val lenBuf = ByteArray(4)
        leerExacto(reader, lenBuf, 0, 4)

        val len = ByteBuffer.wrap(lenBuf).order(ByteOrder.LITTLE_ENDIAN).int
        require(len in 0..maxBytes) {
            "Trama rechazada por exceder tamaño máximo permitido: $len bytes (máximo $maxBytes)."
        }

        val payload = ByteArray(len)
        leerExacto(reader, payload, 0, len)
        return payload
    }

    /**
     * Escribe una trama con prefijo de longitud `u32` little-endian en un [OutputStream].
     */
    fun escribirTrama(writer: OutputStream, payload: ByteArray) {
        val lenBuf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(payload.size).array()
        writer.write(lenBuf)
        writer.write(payload)
        writer.flush()
    }

    /**
     * Serializa un [MensajeP2P] al formato canónico: `[tipo (1B)] || [cuerpo]`.
     */
    fun serializarMensaje(msg: MensajeP2P): ByteArray {
        return when (msg) {
            is MensajeP2P.SyncPayload -> {
                val jsonString = json.encodeToString(ContenidoBoveda.serializer(), msg.boveda)
                val jsonBytes = jsonString.toByteArray(Charsets.UTF_8)
                val out = ByteArray(1 + jsonBytes.size)
                out[0] = TIPO_MSG_SYNC_PAYLOAD
                System.arraycopy(jsonBytes, 0, out, 1, jsonBytes.size)
                out
            }
            is MensajeP2P.CommitAck -> {
                val ackBytes = msg.ack.toByteArray(Charsets.UTF_8)
                val out = ByteArray(1 + ackBytes.size)
                out[0] = TIPO_MSG_COMMIT_ACK
                System.arraycopy(ackBytes, 0, out, 1, ackBytes.size)
                out
            }
        }
    }

    /**
     * Deserializa un [MensajeP2P] desde el formato canónico.
     */
    fun deserializarMensaje(bytes: ByteArray): MensajeP2P {
        require(bytes.isNotEmpty()) { "Mensaje P2P vacío." }
        val tipo = bytes[0]
        return when (tipo) {
            TIPO_MSG_SYNC_PAYLOAD -> {
                val jsonString = String(bytes, 1, bytes.size - 1, Charsets.UTF_8)
                val boveda = json.decodeFromString(ContenidoBoveda.serializer(), jsonString)
                MensajeP2P.SyncPayload(boveda)
            }
            TIPO_MSG_COMMIT_ACK -> {
                val ack = String(bytes, 1, bytes.size - 1, Charsets.UTF_8)
                MensajeP2P.CommitAck(ack)
            }
            else -> throw IllegalArgumentException("Tipo de mensaje P2P desconocido: 0x%02x".format(tipo))
        }
    }

    private fun leerExacto(reader: InputStream, buffer: ByteArray, offset: Int, length: Int) {
        var leidos = 0
        while (leidos < length) {
            val n = reader.read(buffer, offset + leidos, length - leidos)
            if (n < 0) {
                throw EOFException("Fin inesperado del flujo al leer $length bytes (leídos $leidos).")
            }
            leidos += n
        }
    }
}
