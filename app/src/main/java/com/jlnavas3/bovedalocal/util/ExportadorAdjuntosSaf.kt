/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.net.Uri
import com.jlnavas3.bovedalocal.data.AdjuntoBoveda
import java.util.Base64

/**
 * Utilidad desacoplada para exportar y guardar archivos adjuntos hacia el almacenamiento
 * del usuario mediante SAF de forma segura y sin ninguna interacción de red.
 */
object ExportadorAdjuntosSaf {

    fun guardarEnUri(contexto: Context, adjunto: AdjuntoBoveda, uriDestino: Uri): Result<Unit> {
        return runCatching {
            val bytes = Base64.getDecoder().decode(adjunto.contenidoBase64)
            contexto.contentResolver.openOutputStream(uriDestino)?.use { output ->
                output.write(bytes)
                output.flush()
            } ?: throw IllegalStateException("No se pudo escribir en el destino seleccionado")
        }
    }
}
