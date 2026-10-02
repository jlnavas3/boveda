package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Switch estándar para toda la app con diseño nativo Honor MagicOS / Samsung One UI.
 * Sin bordes duros, con track redondeado y colores sólidos.
 */
@Composable
fun SwitchBoveda(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorActivo: Color = Ambar
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = ColorSobreAcento,
            checkedTrackColor = colorActivo,
            checkedBorderColor = Color.Transparent,
            uncheckedThumbColor = ColorAjusteGris,
            uncheckedTrackColor = if (esOscuroActivo) Color(0xFF333238) else Color(0xFFE5E5EA),
            uncheckedBorderColor = Color.Transparent
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

