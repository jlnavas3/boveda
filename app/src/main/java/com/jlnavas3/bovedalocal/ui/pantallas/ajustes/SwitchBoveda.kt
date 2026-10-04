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
