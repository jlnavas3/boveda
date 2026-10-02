package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.colorContraste

/**
 * Switch estándar para toda la app con diseño nativo Honor MagicOS / Samsung One UI.
 * Sin bordes duros, con track redondeado y colores calibrados con la paleta de capas sobria.
 */
@Composable
fun SwitchBoveda(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorActivo: Color = ColorAcento,
    colorInactivoTrack: Color = ColorCampoAjustes,
    colorInactivoThumb: Color = ColorAjusteGris
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = colorContraste(colorActivo),
            checkedTrackColor = colorActivo,
            checkedBorderColor = Color.Transparent,
            uncheckedThumbColor = colorInactivoThumb,
            uncheckedTrackColor = colorInactivoTrack,
            uncheckedBorderColor = Color.Transparent,
            disabledCheckedTrackColor = colorActivo.copy(alpha = 0.4f),
            disabledCheckedThumbColor = colorContraste(colorActivo).copy(alpha = 0.6f),
            disabledUncheckedTrackColor = colorInactivoTrack.copy(alpha = 0.4f),
            disabledUncheckedThumbColor = colorInactivoThumb.copy(alpha = 0.4f)
        )
    )
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun SwitchBovedaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            SwitchBoveda(checked = true, onCheckedChange = {})
            SwitchBoveda(checked = false, onCheckedChange = {})
            SwitchBoveda(checked = true, enabled = false, onCheckedChange = {})
        }
    }
}

