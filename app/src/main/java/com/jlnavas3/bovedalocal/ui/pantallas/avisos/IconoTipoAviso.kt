/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.avisos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.TipoAvisoInterno

/**
 * Retorna el icono representativo para un tipo de aviso o recomendación interna.
 */
fun iconoParaTipoAviso(tipo: TipoAvisoInterno): ImageVector = when (tipo) {
    TipoAvisoInterno.TITULOS_SIN_NORMALIZAR -> Icons.Filled.Spellcheck
    TipoAvisoInterno.DUPLICADOS_DETECTADOS -> Icons.AutoMirrored.Filled.CompareArrows
    TipoAvisoInterno.CONTRASENAS_VULNERABLES -> Icons.Filled.Security
    TipoAvisoInterno.RESPALDO_PENDIENTE -> Icons.Filled.Backup
}
