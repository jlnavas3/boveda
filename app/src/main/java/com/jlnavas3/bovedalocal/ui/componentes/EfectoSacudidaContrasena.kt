package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.keyframes

/**
 * Dispara una animación de sacudida horizontal de 260ms para indicar error de contraseña.
 */
suspend fun dispararSacudidaHorizontal(sacudida: Animatable<Float, AnimationVector1D>) {
    sacudida.animateTo(
        targetValue = 0f,
        animationSpec = keyframes {
            durationMillis = 260
            0f at 0
            -14f at 40
            12f at 90
            -8f at 140
            5f at 190
            0f at 260
        }
    )
}
