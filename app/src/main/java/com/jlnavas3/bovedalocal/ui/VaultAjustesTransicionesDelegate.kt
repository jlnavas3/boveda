/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.AjustesTransiciones
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.util.Diagnostico

/**
 * Sub-delegado especializado en la configuración de animaciones y transiciones de pantalla.
 */
interface VaultAjustesTransicionesDelegate {
    val repositorio: VaultRepository

    fun ajustarEstiloTransicion(estilo: String) {
        repositorio.ajustes.actualizar { it.actualizarTransiciones { t -> t.copy(estilo = estilo) } }
        Diagnostico.apuntar("apariencia", "Estilo de transición configurado en: $estilo")
    }

    fun ajustarDuracionTransicionMs(duracion: Int) {
        val acotada = duracion.coerceIn(120, 380)
        repositorio.ajustes.actualizar { it.actualizarTransiciones { t -> t.copy(duracionMs = acotada) } }
    }

    fun ajustarSpringHabilitado(habilitado: Boolean) {
        repositorio.ajustes.actualizar { it.actualizarTransiciones { t -> t.copy(springHabilitado = habilitado) } }
        Diagnostico.apuntar("apariencia", "Física elástica (Spring) ${if (habilitado) "habilitada" else "deshabilitada"}")
    }

    fun ajustarSpringAmortiguacion(nivel: String) {
        repositorio.ajustes.actualizar { it.actualizarTransiciones { t -> t.copy(springAmortiguacion = nivel) } }
    }

    fun ajustarDesbloqueoFadeLimpio(habilitado: Boolean) {
        repositorio.ajustes.actualizar { it.actualizarTransiciones { t -> t.copy(desbloqueoFadeLimpio = habilitado) } }
    }

    fun ajustarGestoPredictivoHabilitado(habilitado: Boolean) {
        repositorio.ajustes.actualizar { it.actualizarTransiciones { t -> t.copy(gestoPredictivoHabilitado = habilitado) } }
        Diagnostico.apuntar("apariencia", "Gesto predictivo ${if (habilitado) "habilitado" else "deshabilitado"}")
    }

    fun restablecerAjustesTransiciones() {
        repositorio.ajustes.actualizar { it.copy(transiciones = AjustesTransiciones()) }
        Diagnostico.apuntar("apariencia", "Ajustes de transiciones restablecidos a valores por defecto")
    }
}
