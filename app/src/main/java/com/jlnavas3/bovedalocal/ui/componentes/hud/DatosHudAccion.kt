/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.hud

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Carga de datos que representa una acción mostrada en el HUD flotante inferior.
 *
 * @param etiqueta Texto descriptivo de la acción (ej. "Compartir", "Renombrar", "Eliminar").
 * @param icono Icono representativo opcional que acompaña a la etiqueta.
 * @param id Identificador temporal para diferenciar emisiones consecutivas y reiniciar animaciones.
 */
@Immutable
data class DatosHudAccion(
    val etiqueta: String,
    val icono: ImageVector? = null,
    val id: Long = System.currentTimeMillis()
)
