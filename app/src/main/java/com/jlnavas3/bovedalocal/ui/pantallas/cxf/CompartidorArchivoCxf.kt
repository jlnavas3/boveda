/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import android.content.Context
import android.content.Intent

/**
 * Utilidad modular para compartir el documento de credenciales FIDO (.cxf)
 * mediante el selector de aplicaciones estándar de Android.
 */
object CompartidorArchivoCxf {

    fun compartir(contexto: Context, json: String) {
        try {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, json)
                putExtra(Intent.EXTRA_TITLE, "boveda_credenciales.cxf")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Exportar credenciales FIDO CXF")
            contexto.startActivity(shareIntent)
        } catch (_: Exception) {
        }
    }
}
