package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.jlnavas3.bovedalocal.crypto.VaultCrypto
import com.jlnavas3.bovedalocal.data.GestorBackupAutomatico
import com.jlnavas3.bovedalocal.util.Diagnostico
import java.io.File

/**
 * Guarda los bytes de un archivo de respaldo cifrado en MediaStore (Android 10+)
 * o en el directorio de respaldos con escritura atómica.
 */
fun guardarBackupEnAlmacenamiento(
    contexto: Context,
    nombreArchivo: String,
    bytes: ByteArray
): Boolean {
    var guardadoExitoso = false

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        try {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, nombreArchivo)
                put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/BovedaLocal/Backups")
                put(MediaStore.Downloads.IS_PENDING, 1)
            }
            val uri = contexto.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
            )
            if (uri != null) {
                contexto.contentResolver.openOutputStream(uri)?.use { stream ->
                    stream.write(bytes)
                }
                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                contexto.contentResolver.update(uri, values, null, null)
                guardadoExitoso = true
            }
        } catch (e: Exception) {
            Diagnostico.apuntar("backup", "Fallo al guardar copia selectiva con MediaStore: ${e.message}")
            guardadoExitoso = false
        }
    }

    if (!guardadoExitoso) {
        try {
            val carpetaAuto = GestorBackupAutomatico.obtenerDirectorioBackups(contexto)
            val destino = File(carpetaAuto, nombreArchivo)
            VaultCrypto.escribirAtomico(destino, bytes)
            MediaScannerConnection.scanFile(contexto, arrayOf(destino.absolutePath), null, null)
            guardadoExitoso = true
        } catch (e: Exception) {
            Diagnostico.apuntar("backup", "Fallo al guardar copia selectiva con File: ${e.message}")
        }
    }

    return guardadoExitoso
}
