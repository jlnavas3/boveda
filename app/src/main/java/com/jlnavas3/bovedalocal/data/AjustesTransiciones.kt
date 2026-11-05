/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable

/**
 * Agrupa la configuración de animaciones y transiciones de pantalla en la app.
 * Encapsulado para mantener los slots del constructor de [AjustesApp] dentro de los límites del JVM.
 */
@Immutable
data class AjustesTransiciones(
    val estilo: String = AjustesDefaults.Animacion.ESTILO_TRANSICION,
    val duracionMs: Int = AjustesDefaults.Animacion.DURACION_TRANSICION_MS,
    val springHabilitado: Boolean = AjustesDefaults.Animacion.SPRING_HABILITADO,
    val springAmortiguacion: String = AjustesDefaults.Animacion.SPRING_AMORTIGUACION,
    val desbloqueoFadeLimpio: Boolean = AjustesDefaults.Animacion.DESBLOQUEO_FADE_LIMPIO,
    val gestoPredictivoHabilitado: Boolean = AjustesDefaults.Animacion.GESTO_PREDICTIVO_HABILITADO
)
