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
 * Microlanzador desacoplado para iniciar llamadas telefónicas mediante ACTION_DIAL,
 * soportando prefijos de número oculto (#31#, *67, etc.) y preservando caracteres '#' y '*'
 * utilizando Uri.fromParts para evitar su truncamiento como fragmentos URL.
 */
object LanzadorLlamadaTelefonica {

    fun llamar(context: Context, numero: String, prefijoOculto: String? = null): Boolean {
        val numLimpio = FormateadorTelefonoInternacional.limpiarParaMarcado(numero)
        if (numLimpio.isBlank()) return false

        val numeroMarcado = if (!prefijoOculto.isNullOrBlank()) {
            "$prefijoOculto$numLimpio"
        } else {
            numLimpio
        }

        return try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.fromParts("tel", numeroMarcado, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}
