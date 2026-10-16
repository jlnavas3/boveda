/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.jlnavas3.bovedalocal.data.AdjuntoBoveda
import java.util.Base64
import java.util.UUID

/**
 * Utilidad desacoplada para leer archivos locales mediante Storage Access Framework (SAF)
 * y convertirlos de forma segura en un [AdjuntoBoveda] cifrable.
 * Cumple con una arquitectura estrictamente offline y con límite de tamaño prudente (2 MB).
 */
object LectorAdjuntosSaf {

    /** Límite de 2 MB por adjunto para evitar consumo desmedido de RAM y garantizar fluidez. */
    const val LIMITE_BYTES: Long = 2 * 1024 * 1024L

    fun leerAdjunto(contexto: Context, uri: Uri): Result<AdjuntoBoveda> {
        return runCatching {
            var nombre = "archivo_${System.currentTimeMillis()}"
            var tamanoAproximado = 0L

            try {
                contexto.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIdx = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (cursor.moveToFirst()) {
                        if (nameIdx != -1) {
                            val n = cursor.getString(nameIdx)
                            if (!n.isNullOrBlank()) nombre = n
                        }
                        if (sizeIdx != -1) {
                            tamanoAproximado = cursor.getLong(sizeIdx)
                        }
                    }
                }
            } catch (_: Exception) {}

            if (tamanoAproximado > LIMITE_BYTES) {
                throw IllegalArgumentException("El archivo excede el límite máximo de 2 MB (${formatearTamano(tamanoAproximado)})")
            }

            val bytes = contexto.contentResolver.openInputStream(uri)?.use { input ->
                input.readBytes()
            } ?: throw IllegalStateException("No se pudo leer el archivo seleccionado")

            if (bytes.size > LIMITE_BYTES) {
                throw IllegalArgumentException("El archivo excede el límite máximo de 2 MB (${formatearTamano(bytes.size.toLong())})")
            }

            val mime = contexto.contentResolver.getType(uri) ?: deducirMimeType(nombre)
            val b64 = Base64.getEncoder().encodeToString(bytes)

            AdjuntoBoveda(
                id = UUID.randomUUID().toString(),
                nombre = nombre,
                mimeType = mime,
                tamanoBytes = bytes.size.toLong(),
                contenidoBase64 = b64,
                fechaSubida = System.currentTimeMillis()
            )
        }
    }

    fun deducirMimeType(nombre: String): String {
        val extension = nombre.substringAfterLast('.', "").lowercase()
        return when (extension) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            "gif" -> "image/gif"
            "pdf" -> "application/pdf"
            "txt", "log", "conf", "cfg", "env" -> "text/plain"
            "json" -> "application/json"
            "xml" -> "application/xml"
            "key", "pem", "pub", "crt", "cer" -> "application/x-pem-file"
            "zip" -> "application/zip"
            else -> "application/octet-stream"
        }
    }

    fun formatearTamano(bytes: Long): String {
        if (bytes < 1024) return "$bytes B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(1024.0)).toInt()
        val pre = "KMGTPE"[exp - 1]
        val valor = bytes / Math.pow(1024.0, exp.toDouble())
        return String.format(java.util.Locale.US, "%.1f %cB", valor, pre)
    }
}
