/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

/**
 * Representa una acción de acceso directo o autocompletado pendiente por ejecutar tras desbloquear la bóveda.
 */
data class AccionShortcutPendiente(
    val accion: String,
    val tituloInicial: String = "",
    val urlInicial: String = ""
)
