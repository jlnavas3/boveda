/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.hud

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Gestor reactivo para proyectar y temporizar el HUD de acciones en la base de la pantalla.
 *
 * Mantiene la emisión durante 2.5 segundos de forma automática, renovando el temporizador
 * si el usuario pulsa otro botón o descartando si se solicita un cierre explícito.
 */
class GestorHudAccion(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
) {
    private val _estado = MutableStateFlow<DatosHudAccion?>(null)
    val estado: StateFlow<DatosHudAccion?> = _estado.asStateFlow()

    private var trabajoCierre: Job? = null

    /**
     * Muestra la pastilla HUD con la etiqueta y el icono especificados, reiniciando
     * el contador de visibilidad a [duracionMs] milisegundos (2.5 segundos por defecto).
     */
    fun mostrar(
        etiqueta: String,
        icono: ImageVector? = null,
        duracionMs: Long = 2500L
    ) {
        if (etiqueta.isBlank()) return

        trabajoCierre?.cancel()
        val emision = DatosHudAccion(
            etiqueta = etiqueta,
            icono = icono,
            id = System.nanoTime()
        )
        _estado.value = emision

        trabajoCierre = scope.launch {
            delay(duracionMs)
            if (_estado.value?.id == emision.id) {
                _estado.value = null
            }
        }
    }

    /**
     * Oculta inmediatamente el HUD y cancela cualquier temporizador activo.
     */
    fun ocultar() {
        trabajoCierre?.cancel()
        trabajoCierre = null
        _estado.value = null
    }
}
