/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util.comunicacion

import android.content.Context
import java.io.File
import java.io.RandomAccessFile

/**
 * Microcomponente de seguridad e higiene forense para sobrescribir con ceros binarios (0x00)
 * y eliminar de forma irrecuperable los archivos temporales generados en la caché de mensajes efímeros.
 */
object PurgadorArchivosEfimeros {

    private const val CARPETA_EFIMERA = "mensajes_efimeros"

    /**
     * Sobrescribe con 0x00 y elimina todos los archivos contenidos en el directorio de mensajes efímeros.
     */
    fun purgar(context: Context) {
        val dir = File(context.cacheDir, CARPETA_EFIMERA)
        if (!dir.exists() || !dir.isDirectory) return

        val archivos = dir.listFiles() ?: return
        for (archivo in archivos) {
            if (archivo.isFile) {
                sobrescribirYEliminar(archivo)
            }
        }
    }

    /**
     * Sobrescribe el contenido de un archivo con bytes 0x00 forzando la sincronización al almacenamiento físico,
     * y posteriormente lo elimina del sistema de archivos.
     */
    fun sobrescribirYEliminar(archivo: File) {
        if (!archivo.exists() || !archivo.isFile) return

        try {
            val tamano = archivo.length()
            if (tamano > 0) {
                RandomAccessFile(archivo, "rws").use { raf ->
                    val bufferCeros = ByteArray(minOf(tamano, 4096L).toInt())
                    var escritos = 0L
                    while (escritos < tamano) {
                        val cantidad = minOf(bufferCeros.size.toLong(), tamano - escritos).toInt()
                        raf.write(bufferCeros, 0, cantidad)
                        escritos += cantidad
                    }
                    raf.fd.sync()
                }
            }
        } catch (_: Exception) {
            // Manejo tolerante
        } finally {
            archivo.delete()
        }
    }
}
