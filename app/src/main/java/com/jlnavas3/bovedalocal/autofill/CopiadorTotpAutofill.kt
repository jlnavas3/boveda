/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Portapapeles

/**
 * Utilidad desacoplada para copiar automáticamente el código TOTP / 2FA al portapapeles
 * tras realizar un autocompletado en una app o navegador, facilitando el paso del segundo factor.
 */
object CopiadorTotpAutofill {

    /**
     * Copia el código TOTP de la entrada al portapapeles si la configuración está activa
     * y la entrada tiene un secreto 2FA válido configurado.
     * Programa la limpieza automática según el temporizador de portapapeles en ajustes.
     */
    fun copiarSiAplica(
        contexto: Context,
        entrada: Entrada?,
        ajustes: AjustesApp
    ): Boolean {
        if (entrada == null) return false
        if (!ajustes.autofillCopiarTotpAutomatico) return false
        val codigo = AutofillOtpUtiles.obtenerCodigoTotp(entrada) ?: return false

        Portapapeles.copiarSensible(contexto, "Código de verificación", codigo)
        Diagnostico.apuntar("autofill", "Código TOTP de '${entrada.titulo}' copiado automáticamente tras autofill")

        Handler(Looper.getMainLooper()).post {
            try {
                Toast.makeText(contexto.applicationContext, "Código 2FA copiado al portapapeles", Toast.LENGTH_SHORT).show()
            } catch (_: Exception) {}
        }

        val segundos = ajustes.portapapelesSegundos
        if (segundos > 0) {
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    Portapapeles.limpiarSiCoincide(contexto.applicationContext, codigo)
                    Diagnostico.apuntar("portapapeles", "Portapapeles limpiado tras autofill ($segundos s)")
                } catch (_: Exception) {}
            }, segundos * 1000L)
        }
        return true
    }
}
