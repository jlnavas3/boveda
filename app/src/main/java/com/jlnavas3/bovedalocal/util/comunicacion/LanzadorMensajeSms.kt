/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util.comunicacion

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jlnavas3.bovedalocal.data.contactos.FormateadorTelefonoInternacional

/**
 * Microlanzador desacoplado para iniciar redacción de SMS mediante ACTION_SENDTO ("smsto:").
 */
object LanzadorMensajeSms {

    fun enviarSms(context: Context, numero: String, mensaje: String? = null): Boolean {
        val numLimpio = FormateadorTelefonoInternacional.limpiarParaMarcado(numero)
        if (numLimpio.isBlank()) return false

        return try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.fromParts("smsto", numLimpio, null)
                if (!mensaje.isNullOrBlank()) {
                    putExtra("sms_body", mensaje)
                }
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}
