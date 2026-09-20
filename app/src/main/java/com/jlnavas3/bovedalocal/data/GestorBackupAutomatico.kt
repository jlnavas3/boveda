package com.jlnavas3.bovedalocal.data

import android.content.Context
import android.os.Environment
import com.jlnavas3.bovedalocal.crypto.Zeroizar
import com.jlnavas3.bovedalocal.util.Diagnostico
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GestorBackupAutomatico {

    fun obtenerDirectorioBackups(context: Context): File {
        val dirPublico = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "BovedaLocal/Backups"
        )
        if (dirPublico.exists() || dirPublico.mkdirs()) {
            return dirPublico
        }
        val dirInterno = File(context.getExternalFilesDir(null), "Backups")
        if (!dirInterno.exists()) dirInterno.mkdirs()
        return dirInterno
    }

    fun debeEjecutar(ajustes: AjustesApp): Boolean {
        return debeEjecutar(
            frecuenciaDias = ajustes.backupAutoFrecuenciaDias,
            password = ajustes.backupAutoPasswordCifrado,
            ultimaEjecucion = ajustes.backupAutoUltimaEjecucion
        )
    }

    fun debeEjecutar(
        frecuenciaDias: Int,
        password: String,
        ultimaEjecucion: Long,
        ahora: Long = System.currentTimeMillis()
    ): Boolean {
        if (frecuenciaDias <= 0) return false
        if (password.isBlank()) return false
        val intervaloMs = frecuenciaDias * 24L * 60L * 60L * 1000L
        return (ahora - ultimaEjecucion) >= intervaloMs
    }

    fun ejecutar(context: Context, repositorio: VaultRepository): Pair<Boolean, String> {
        val ajustes = repositorio.ajustes.actual
        val password = ajustes.backupAutoPasswordCifrado
        if (password.isBlank()) {
            return Pair(false, "No hay contraseña configurada para el respaldo automático")
        }
        if (!repositorio.estaDesbloqueada) {
            return Pair(false, "La bóveda debe estar abierta")
        }

        val chars = password.toCharArray()
        return try {
            val datosCifrados = repositorio.exportar(chars)
            val dir = obtenerDirectorioBackups(context)
            val siguienteSecuencia = calcularSiguienteSecuencia(dir, ajustes.backupAutoSecuencia)
            val nombreArchivo = resolverNombreArchivo(ajustes.backupAutoPatronNombre, siguienteSecuencia)
            val archivoDestino = File(dir, nombreArchivo)

            archivoDestino.writeBytes(datosCifrados)

            val maxCopias = ajustes.backupAutoMaxCopias.coerceAtLeast(1)
            rotarBackups(dir, maxCopias)

            repositorio.ajustes.actualizar {
                it.copy(
                    backupAutoUltimaEjecucion = System.currentTimeMillis(),
                    backupAutoSecuencia = siguienteSecuencia
                )
            }

            Diagnostico.apuntar("backup", "Copia automática rotativa creada: $nombreArchivo")
            Pair(true, "Copia guardada: $nombreArchivo")
        } catch (e: Exception) {
            Diagnostico.apuntar("backup", "Error en backup automático: ${e.message}")
            Pair(false, e.message ?: "Error al generar copia")
        } finally {
            Zeroizar.borrar(chars)
        }
    }

    fun calcularSiguienteSecuencia(dir: File, secuenciaActual: Int): Int {
        var maxEncontrado = secuenciaActual
        val archivos = dir.listFiles { _, name ->
            name.endsWith(".boveda", ignoreCase = true) || name.endsWith(".bvda", ignoreCase = true)
        }
        if (archivos != null) {
            val regex = Regex("^(\\d+)")
            for (f in archivos) {
                val m = regex.find(f.name)
                if (m != null) {
                    val n = m.groupValues[1].toIntOrNull() ?: 0
                    if (n > maxEncontrado) maxEncontrado = n
                }
            }
        }
        return if (maxEncontrado <= 0) 1 else maxEncontrado + 1
    }

    fun rotarBackups(dir: File, maxCopias: Int) {
        val archivos = dir.listFiles { _, name -> name.endsWith(".boveda", ignoreCase = true) || name.endsWith(".bvda", ignoreCase = true) } ?: return
        if (archivos.size > maxCopias) {
            archivos.sortBy { it.lastModified() }
            val aBorrar = archivos.take(archivos.size - maxCopias)
            for (f in aBorrar) {
                f.delete()
            }
        }
    }

    fun resolverNombreArchivo(
        patron: String,
        secuencia: Int = 1,
        fecha: Date = Date()
    ): String {
        val formatoFecha = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val marca = formatoFecha.format(fecha)
        var limpio = patron.trim().ifBlank { "{99}-boveda-auto-{FECHA}" }

        limpio = when {
            limpio.contains("{999}") -> limpio.replace("{999}", String.format(Locale.US, "%03d", secuencia))
            limpio.contains("{001}") -> limpio.replace("{001}", String.format(Locale.US, "%03d", secuencia))
            limpio.contains("{99}") -> limpio.replace("{99}", String.format(Locale.US, "%02d", secuencia))
            limpio.contains("{01}") -> limpio.replace("{01}", String.format(Locale.US, "%02d", secuencia))
            limpio.contains("{NUM}", ignoreCase = true) -> limpio.replace(Regex("(?i)\\{NUM\\}"), String.format(Locale.US, "%02d", secuencia))
            limpio.contains("{9}") -> limpio.replace("{9}", secuencia.toString())
            else -> limpio
        }

        val res = if (limpio.contains("{FECHA}", ignoreCase = true)) {
            limpio.replace(Regex("(?i)\\{FECHA\\}"), marca)
        } else if (limpio.contains("{TIMESTAMP}", ignoreCase = true)) {
            limpio.replace(Regex("(?i)\\{TIMESTAMP\\}"), marca)
        } else if (limpio.contains("{DATE}", ignoreCase = true)) {
            limpio.replace(Regex("(?i)\\{DATE\\}"), marca)
        } else {
            "${limpio}_$marca"
        }
        return if (res.endsWith(".bvda", ignoreCase = true)) res
               else if (res.endsWith(".boveda", ignoreCase = true)) res
               else "$res.bvda"
    }
}
