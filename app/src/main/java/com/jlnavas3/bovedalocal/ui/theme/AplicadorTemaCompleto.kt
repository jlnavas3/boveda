/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.theme

import com.jlnavas3.bovedalocal.data.AjustesApp

fun aplicarPersonalizacionTemaCompleto(ajustes: AjustesApp) {
    aplicarPersonalizacionColores(ajustes)
    aplicarPersonalizacionFormas(ajustes)
    aplicarPersonalizacionTipografia(ajustes)
    AlumbradoActivo = ajustes.alumbradoActivo
    AlumbradoIntensidad = ajustes.alumbradoIntensidad
    AlumbradoRepeticiones = ajustes.alumbradoRepeticiones
    AlumbradoDuracionMs = ajustes.alumbradoDuracionMs
}
