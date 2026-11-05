/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import android.content.Context
import android.provider.Settings

/**
 * Utilidad desacoplada para consultar si el sistema operativo tiene las animaciones
 * desactivadas o reducidas a 0x (escala de duración de animador en 0).
 */
object DetectorAnimacionesSistema {

    /**
     * Devuelve `true` si la escala de duración de animador global del sistema es 0 o menor a 0.05.
     */
    fun animacionesDesactivadas(contexto: Context): Boolean {
        return try {
            val escala = Settings.Global.getFloat(
                contexto.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1.0f
            )
            escala <= 0.05f
        } catch (_: Exception) {
            false
        }
    }
}
