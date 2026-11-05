/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util.comunicacion

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Microlanzador desacoplado para iniciar redacción de correo electrónico mediante ACTION_SENDTO ("mailto:").
 */
object LanzadorCorreoElectronico {

    fun redactar(context: Context, correo: String, asunto: String? = null, cuerpo: String? = null): Boolean {
        val correoLimpio = correo.trim()
        if (correoLimpio.isBlank()) return false

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$correoLimpio")
            if (!asunto.isNullOrBlank()) {
                putExtra(Intent.EXTRA_SUBJECT, asunto)
            }
            if (!cuerpo.isNullOrBlank()) {
                putExtra(Intent.EXTRA_TEXT, cuerpo)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        return try {
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}
