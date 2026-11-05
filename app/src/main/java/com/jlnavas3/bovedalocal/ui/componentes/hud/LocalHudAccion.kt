/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.hud

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Proveedor de contexto Compose para interactuar con el HUD global de acciones
 * desde cualquier botón, icono o barra en cualquier nivel del árbol de vistas.
 */
val LocalHudAccion = staticCompositionLocalOf<GestorHudAccion?> {
    null
}
