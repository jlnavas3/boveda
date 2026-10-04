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
