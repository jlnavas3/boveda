/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.runtime.Composable
import kotlin.coroutines.cancellation.CancellationException

/**
 * Gestiona el retroceso del sistema soportando tanto el BackHandler estándar
 * como el PredictiveBackHandler de Android 14+ (API 34+).
 */
@Composable
fun ManejadorRetrocesoApp(
    habilitado: Boolean,
    usarGestoPredictivo: Boolean,
    alProgresoPredictivo: ((Float, Int) -> Unit)? = null,
    alCancelarPredictivo: (() -> Unit)? = null,
    alRetroceder: () -> Unit
) {
    if (usarGestoPredictivo && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        PredictiveBackHandler(enabled = habilitado) { progressFlow ->
            try {
                progressFlow.collect { backEvent ->
                    alProgresoPredictivo?.invoke(backEvent.progress, backEvent.swipeEdge)
                }
                alProgresoPredictivo?.invoke(0f, 0)
                alRetroceder()
            } catch (_: CancellationException) {
                // Gesto cancelado por el usuario
                alCancelarPredictivo?.invoke()
            }
        }
    } else {
        BackHandler(enabled = habilitado) {
            alRetroceder()
        }
    }
}
