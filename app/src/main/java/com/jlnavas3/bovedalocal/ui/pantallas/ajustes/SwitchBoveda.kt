/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Switch estándar para toda la app con diseño nativo Honor MagicOS / Samsung One UI.
 */
@Composable
fun SwitchBoveda(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorActivo: Color = com.jlnavas3.bovedalocal.ui.theme.ColorAcento,
    colorInactivoTrack: Color = ColorCampoAjustes,
    colorInactivoThumb: Color = ColorAjusteGris
) {
    com.jlnavas3.bovedalocal.ui.componentes.SwitchBoveda(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colorActivo = colorActivo,
        colorInactivoTrack = colorInactivoTrack,
        colorInactivoThumb = colorInactivoThumb
    )
}
