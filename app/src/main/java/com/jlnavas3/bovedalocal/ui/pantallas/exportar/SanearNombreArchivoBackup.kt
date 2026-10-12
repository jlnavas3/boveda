/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.exportar

/**
 * Sanea y valida el nombre propuesto para un archivo de respaldo .bvda,
 * reemplazando caracteres prohibidos en sistemas de archivos.
 */
fun sanearNombreArchivoBackup(nombre: String): String {
    val limpio = nombre.replace(Regex("[\\\\/:*?\"<>|]"), "_").trim()
    val conExt = if (limpio.endsWith(".bvda", ignoreCase = true)) limpio else "$limpio.bvda"
    return conExt.ifBlank { "01-selectivo-backup.bvda" }
}
