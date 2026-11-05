/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.transiciones

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.EstiloTransicionPantalla
import com.jlnavas3.bovedalocal.data.NivelReboteSpring
import com.jlnavas3.bovedalocal.ui.Pantalla

/**
 * Genera el [ContentTransform] adecuado para las transiciones entre pantallas
 * según las preferencias activas del usuario.
 */
fun construirTransicionNavegacion(
    ajustes: AjustesApp,
    esRetroceso: Boolean,
    initialState: Pantalla,
    targetState: Pantalla
): ContentTransform {
    // 1. Regla de desbloqueo/bloqueo de seguridad: siempre desvanecimiento limpio si está activo
    val esDesbloqueoOBloqueo = initialState is Pantalla.Desbloqueo || targetState is Pantalla.Desbloqueo ||
            initialState is Pantalla.Onboarding || targetState is Pantalla.Onboarding

    if (esDesbloqueoOBloqueo && ajustes.desbloqueoFadeLimpio) {
        return (fadeIn(animationSpec = tween(durationMillis = 220, easing = LinearOutSlowInEasing)))
            .togetherWith(fadeOut(animationSpec = tween(durationMillis = 160, easing = FastOutLinearInEasing)))
    }

    val estilo = EstiloTransicionPantalla.desdeId(ajustes.estiloTransicionPantalla)

    // Modo instantáneo
    if (estilo == EstiloTransicionPantalla.INSTANTANEO) {
        return EnterTransition.None togetherWith ExitTransition.None
    }

    val duracion = ajustes.duracionTransicionMs.coerceIn(120, 380)
    val usaSpring = ajustes.springTransicionHabilitado
    val rebote = NivelReboteSpring.desdeId(ajustes.springAmortiguacion).dampingRatio

    val animOffset: FiniteAnimationSpec<IntOffset> = if (usaSpring) {
        spring(dampingRatio = rebote, stiffness = Spring.StiffnessMedium)
    } else {
        tween(durationMillis = duracion, easing = FastOutSlowInEasing)
    }

    val animFloat: FiniteAnimationSpec<Float> = if (usaSpring) {
        spring(dampingRatio = rebote, stiffness = Spring.StiffnessMedium)
    } else {
        tween(durationMillis = duracion, easing = FastOutSlowInEasing)
    }

    val animFadeSalida: FiniteAnimationSpec<Float> = if (usaSpring) {
        spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
    } else {
        tween(durationMillis = (duracion * 0.85f).toInt(), easing = FastOutLinearInEasing)
    }

    return when (estilo) {
        EstiloTransicionPantalla.PARALLAX -> {
            if (esRetroceso) {
                val entrada = slideInHorizontally(animOffset) { ancho -> -ancho / 4 } + fadeIn(animFloat)
                val salida = slideOutHorizontally(animOffset) { ancho -> ancho } + fadeOut(animFadeSalida)
                (entrada togetherWith salida).apply { targetContentZIndex = -1f }
            } else {
                val entrada = slideInHorizontally(animOffset) { ancho -> ancho } + fadeIn(animFloat)
                val salida = slideOutHorizontally(animOffset) { ancho -> -ancho / 4 } + fadeOut(animFadeSalida)
                (entrada togetherWith salida).apply { targetContentZIndex = 1f }
            }
        }

        EstiloTransicionPantalla.STACK -> {
            if (esRetroceso) {
                val entrada = scaleIn(animFloat, initialScale = 0.92f) + fadeIn(animFloat)
                val salida = slideOutHorizontally(animOffset) { ancho -> ancho } + fadeOut(animFadeSalida)
                (entrada togetherWith salida).apply { targetContentZIndex = -1f }
            } else {
                val entrada = slideInHorizontally(animOffset) { ancho -> ancho } + fadeIn(animFloat)
                val salida = scaleOut(animFloat, targetScale = 0.92f) + fadeOut(animFadeSalida)
                (entrada togetherWith salida).apply { targetContentZIndex = 1f }
            }
        }

        EstiloTransicionPantalla.FADE -> {
            val entrada = fadeIn(animationSpec = tween(durationMillis = duracion, easing = LinearOutSlowInEasing))
            val salida = fadeOut(animationSpec = tween(durationMillis = (duracion * 0.8f).toInt(), easing = FastOutLinearInEasing))
            (entrada togetherWith salida).apply {
                targetContentZIndex = if (esRetroceso) -1f else 1f
            }
        }

        EstiloTransicionPantalla.ZOOM -> {
            if (esRetroceso) {
                val entrada = scaleIn(animFloat, initialScale = 0.88f) + fadeIn(animFloat)
                val salida = scaleOut(animFloat, targetScale = 1.12f) + fadeOut(animFadeSalida)
                (entrada togetherWith salida).apply { targetContentZIndex = -1f }
            } else {
                val entrada = scaleIn(animFloat, initialScale = 1.12f) + fadeIn(animFloat)
                val salida = scaleOut(animFloat, targetScale = 0.88f) + fadeOut(animFadeSalida)
                (entrada togetherWith salida).apply { targetContentZIndex = 1f }
            }
        }

        EstiloTransicionPantalla.SHEET -> {
            if (esRetroceso) {
                val entrada = fadeIn(animFloat)
                val salida = slideOutVertically(animOffset) { alto -> alto } + fadeOut(animFadeSalida)
                (entrada togetherWith salida).apply { targetContentZIndex = -1f }
            } else {
                val entrada = slideInVertically(animOffset) { alto -> alto } + fadeIn(animFloat)
                val salida = fadeOut(animFadeSalida)
                (entrada togetherWith salida).apply { targetContentZIndex = 1f }
            }
        }

        EstiloTransicionPantalla.WIPE -> {
            if (esRetroceso) {
                val entrada = EnterTransition.None
                val salida = slideOutHorizontally(animOffset) { ancho -> ancho }
                (entrada togetherWith salida).apply { targetContentZIndex = -1f }
            } else {
                val entrada = slideInHorizontally(animOffset) { ancho -> ancho }
                val salida = ExitTransition.None
                (entrada togetherWith salida).apply { targetContentZIndex = 1f }
            }
        }

        EstiloTransicionPantalla.INSTANTANEO -> {
            EnterTransition.None togetherWith ExitTransition.None
        }
    }
}
